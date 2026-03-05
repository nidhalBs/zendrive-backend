package com.zendrive.backend.service;

import com.zendrive.backend.dto.LoyaltyAccountDTO;
import com.zendrive.backend.dto.LoyaltyTransactionDTO;
import com.zendrive.backend.entity.LoyaltyAccount;
import com.zendrive.backend.entity.LoyaltyTransaction;
import com.zendrive.backend.repository.LoyaltyAccountRepository;
import com.zendrive.backend.repository.LoyaltyTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class LoyaltyService {

    private final LoyaltyAccountRepository loyaltyAccountRepository;
    private final LoyaltyTransactionRepository loyaltyTransactionRepository;

    public LoyaltyAccountDTO getAccountByUserId(Long userId) {
        LoyaltyAccount account = loyaltyAccountRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Loyalty account not found"));
        return mapToDTO(account);
    }

    @Transactional
    public void addPoints(Long userId, int points, String description, String referenceType, Long referenceId) {
        LoyaltyAccount account = loyaltyAccountRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Loyalty account not found"));

        account.setTotalPoints(account.getTotalPoints() + points);
        account.setAvailablePoints(account.getAvailablePoints() + points);
        loyaltyAccountRepository.save(account);

        LoyaltyTransaction transaction = LoyaltyTransaction.builder()
                .loyaltyAccount(account)
                .points(points)
                .description(description)
                .referenceType(referenceType)
                .referenceId(referenceId)
                .build();
        loyaltyTransactionRepository.save(transaction);
    }

    @Transactional
    public void deductPoints(Long userId, int points, String description) {
        LoyaltyAccount account = loyaltyAccountRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Loyalty account not found"));

        if (account.getAvailablePoints() < points) {
            throw new IllegalArgumentException("Insufficient points");
        }

        account.setAvailablePoints(account.getAvailablePoints() - points);
        loyaltyAccountRepository.save(account);

        LoyaltyTransaction transaction = LoyaltyTransaction.builder()
                .loyaltyAccount(account)
                .points(-points)
                .description(description)
                .build();
        loyaltyTransactionRepository.save(transaction);
    }

    public int calculatePointsForAmount(BigDecimal amount) {
        // 1 point per 1 EUR spent
        return amount.intValue();
    }

    public Page<LoyaltyTransactionDTO> getTransactionHistory(Long userId, Pageable pageable) {
        return loyaltyTransactionRepository.findByLoyaltyAccountUserId(userId, pageable)
                .map(this::mapTransactionToDTO);
    }

    private LoyaltyAccountDTO mapToDTO(LoyaltyAccount account) {
        return LoyaltyAccountDTO.builder()
                .id(account.getId())
                .userId(account.getUser().getId())
                .totalPoints(account.getTotalPoints())
                .availablePoints(account.getAvailablePoints())
                .recentTransactions(
                        loyaltyTransactionRepository.findByLoyaltyAccountId(
                                account.getId(), PageRequest.of(0, 10)
                        ).map(this::mapTransactionToDTO).getContent()
                )
                .build();
    }

    private LoyaltyTransactionDTO mapTransactionToDTO(LoyaltyTransaction transaction) {
        return LoyaltyTransactionDTO.builder()
                .id(transaction.getId())
                .points(transaction.getPoints())
                .description(transaction.getDescription())
                .referenceType(transaction.getReferenceType())
                .referenceId(transaction.getReferenceId())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
