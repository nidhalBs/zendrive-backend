package com.zendrive.backend.service;

import com.zendrive.backend.dto.RouteDTO;
import com.zendrive.backend.entity.Route;
import com.zendrive.backend.mapper.RouteMapper;
import com.zendrive.backend.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;

    public List<RouteDTO> getActiveRoutes() {
        return routeRepository.findByActiveTrue().stream()
                .map(routeMapper::toDTO).toList();
    }

    public List<RouteDTO> getPopularRoutes() {
        return routeRepository.findByPopularTrueAndActiveTrue().stream()
                .map(routeMapper::toDTO).toList();
    }

    public Page<RouteDTO> getAllRoutes(Pageable pageable) {
        return routeRepository.findAll(pageable).map(routeMapper::toDTO);
    }

    public RouteDTO getRouteById(Long id) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Route not found"));
        return routeMapper.toDTO(route);
    }

    @Transactional
    public RouteDTO createRoute(RouteDTO routeDTO) {
        Route route = routeMapper.toEntity(routeDTO);
        route.setActive(true);
        return routeMapper.toDTO(routeRepository.save(route));
    }

    @Transactional
    public RouteDTO updateRoute(Long id, RouteDTO routeDTO) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Route not found"));
        route.setOriginName(routeDTO.getOriginName());
        route.setOriginLatitude(routeDTO.getOriginLatitude());
        route.setOriginLongitude(routeDTO.getOriginLongitude());
        route.setDestinationName(routeDTO.getDestinationName());
        route.setDestinationLatitude(routeDTO.getDestinationLatitude());
        route.setDestinationLongitude(routeDTO.getDestinationLongitude());
        route.setDistanceKm(routeDTO.getDistanceKm());
        route.setEstimatedDurationMinutes(routeDTO.getEstimatedDurationMinutes());
        route.setBasePrice(routeDTO.getBasePrice());
        route.setPopular(routeDTO.isPopular());
        route.setImageUrl(routeDTO.getImageUrl());
        return routeMapper.toDTO(routeRepository.save(route));
    }

    @Transactional
    public void deleteRoute(Long id) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Route not found"));
        route.setActive(false);
        routeRepository.save(route);
    }
}
