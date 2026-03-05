package com.zendrive.backend.repository;

import com.zendrive.backend.entity.Excursion;
import com.zendrive.backend.enums.ExcursionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExcursionRepository extends JpaRepository<Excursion, Long> {
    List<Excursion> findByActiveTrue();
    Page<Excursion> findByActiveTrue(Pageable pageable);
    List<Excursion> findByTypeAndActiveTrue(ExcursionType type);
    long countByActiveTrue();
}
