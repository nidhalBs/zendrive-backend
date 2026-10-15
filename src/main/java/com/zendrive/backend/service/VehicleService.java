package com.zendrive.backend.service;

import com.zendrive.backend.dto.CreateVehicleRequest;
import com.zendrive.backend.dto.VehicleDTO;
import com.zendrive.backend.entity.Vehicle;
import com.zendrive.backend.enums.VehicleStatus;
import com.zendrive.backend.mapper.VehicleMapper;
import com.zendrive.backend.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    public List<VehicleDTO> getAllActiveVehicles() {
        return vehicleRepository.findByActiveTrue().stream()
                .map(vehicleMapper::toDTO).toList();
    }

    public Page<VehicleDTO> getAllVehicles(Pageable pageable) {
        return vehicleRepository.findAll(pageable).map(vehicleMapper::toDTO);
    }

    public VehicleDTO getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));
        return vehicleMapper.toDTO(vehicle);
    }

    public List<VehicleDTO> getAvailableVehicles(int passengers) {
        return vehicleRepository.findAvailableVehiclesForPassengers(passengers).stream()
                .map(vehicleMapper::toDTO).toList();
    }

    @Transactional
    public VehicleDTO createVehicle(CreateVehicleRequest request) {
        Vehicle vehicle = vehicleMapper.toEntity(request);
        vehicle.setStatus(VehicleStatus.AVAILABLE);
        vehicle.setActive(true);
        return vehicleMapper.toDTO(vehicleRepository.save(vehicle));
    }

    @Transactional
    public VehicleDTO updateVehicle(Long id, CreateVehicleRequest request) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));
        vehicleMapper.updateEntity(request, vehicle);
        return vehicleMapper.toDTO(vehicleRepository.save(vehicle));
    }

    @Transactional
    public VehicleDTO updateVehicleStatus(Long id, VehicleStatus status) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));
        vehicle.setStatus(status);
        return vehicleMapper.toDTO(vehicleRepository.save(vehicle));
    }

    @Transactional
    public void deactivateVehicle(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));
        vehicle.setActive(false);
        vehicle.setStatus(VehicleStatus.RETIRED);
        vehicleRepository.save(vehicle);
    }

    public long countByStatus(VehicleStatus status) {
        return vehicleRepository.countByStatus(status);
    }

    public long countActive() {
        return vehicleRepository.countByActiveTrue();
    }
}
