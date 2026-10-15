package com.zendrive.backend.repository;

import com.zendrive.backend.entity.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findByActiveTrue();
    List<Route> findByPopularTrueAndActiveTrue();
    Page<Route> findByActiveTrue(Pageable pageable);
}
