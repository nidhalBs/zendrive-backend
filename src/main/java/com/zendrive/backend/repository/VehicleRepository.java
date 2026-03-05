package com.zendrive.backend.repository;

import com.zendrive.backend.entity.Vehicle;
import com.zendrive.backend.enums.ComfortLevel;
import com.zendrive.backend.enums.VehicleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByStatusAndActiveTrue(VehicleStatus status);
    List<Vehicle> findByActiveTrue();
    Page<Vehicle> findByActiveTrue(Pageable pageable);
    List<Vehicle> findByComfortLevelAndStatusAndActiveTrue(ComfortLevel comfortLevel, VehicleStatus status);

    @Query("SELECT v FROM Vehicle v WHERE v.active = true AND v.status = 'AVAILABLE' AND v.passengerCapacity >= :passengers")
    List<Vehicle> findAvailableVehiclesForPassengers(@Param("passengers") int passengers);

    long countByStatus(VehicleStatus status);
    long countByActiveTrue();
}
