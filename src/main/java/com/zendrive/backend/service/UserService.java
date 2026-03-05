package com.zendrive.backend.service;

import com.zendrive.backend.dto.RegisterRequest;
import com.zendrive.backend.dto.UserDTO;
import com.zendrive.backend.entity.LoyaltyAccount;
import com.zendrive.backend.entity.User;
import com.zendrive.backend.enums.UserRole;
import com.zendrive.backend.mapper.UserMapper;
import com.zendrive.backend.repository.LoyaltyAccountRepository;
import com.zendrive.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final LoyaltyAccountRepository loyaltyAccountRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    @Transactional
    public UserDTO registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .preferredLanguage(request.getPreferredLanguage() != null ? request.getPreferredLanguage() : "fr")
                .role(UserRole.CLIENT)
                .enabled(true)
                .verificationToken(UUID.randomUUID().toString())
                .build();

        User savedUser = userRepository.save(user);

        // Create loyalty account
        LoyaltyAccount loyaltyAccount = LoyaltyAccount.builder()
                .user(savedUser)
                .build();
        loyaltyAccountRepository.save(loyaltyAccount);

        return userMapper.toDTO(savedUser);
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return userMapper.toDTO(user);
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return userMapper.toDTO(user);
    }

    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toDTO);
    }

    public Page<UserDTO> getUsersByRole(UserRole role, Pageable pageable) {
        return userRepository.findByRole(role, pageable).map(userMapper::toDTO);
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (userDTO.getFirstName() != null) user.setFirstName(userDTO.getFirstName());
        if (userDTO.getLastName() != null) user.setLastName(userDTO.getLastName());
        if (userDTO.getPhone() != null) user.setPhone(userDTO.getPhone());
        if (userDTO.getProfileImage() != null) user.setProfileImage(userDTO.getProfileImage());
        if (userDTO.getPreferredLanguage() != null) user.setPreferredLanguage(userDTO.getPreferredLanguage());

        return userMapper.toDTO(userRepository.save(user));
    }

    @Transactional
    public UserDTO updateUserRole(Long id, UserRole role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setRole(role);
        return userMapper.toDTO(userRepository.save(user));
    }

    @Transactional
    public void toggleUserEnabled(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setEnabled(!user.isEnabled());
        userRepository.save(user);
    }

    public boolean confirmEmail(String token) {
        User user = userRepository.findByVerificationToken(token).orElse(null);
        if (user == null) return false;
        user.setEnabled(true);
        user.setVerificationToken(null);
        userRepository.save(user);
        return true;
    }

    public long countClients() {
        return userRepository.countByRole(UserRole.CLIENT);
    }

    public long countActiveUsers() {
        return userRepository.countByEnabled(true);
    }
}
