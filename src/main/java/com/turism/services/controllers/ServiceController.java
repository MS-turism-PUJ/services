package com.turism.services.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turism.services.dtos.AlimentationServiceDTO;
import com.turism.services.dtos.EcoWalkServiceDTO;
import com.turism.services.dtos.HousingServiceDTO;
import com.turism.services.dtos.TransportServiceDTO;
import com.turism.services.models.Service;
import com.turism.services.services.ServiceService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/")
public class ServiceController {
    private final ServiceService serviceSerivce;

    public ServiceController(ServiceService serviceService) {
        this.serviceSerivce = serviceService;
    }

    @GetMapping
    public List<Service> getAllMyServices(@RequestHeader("X-Preferred-Username") String username,
            @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
        log.info("GET / with page: {} and limit: {} for user: {}", page, limit, username);
        return serviceSerivce.getAllMyServices(username, page, limit);
    }

    @PostMapping("/alimentation")
    public Service createAlimentationService(@RequestHeader("X-Preferred-Username") String username,
            @Valid @RequestBody AlimentationServiceDTO alimentationServiceDTO) {
        log.info("POST /alimentation for user: {}", username);
        return serviceSerivce.createService(alimentationServiceDTO.toService(), username);
    }

    @PostMapping("/housing")
    public Service createHousingService(@RequestHeader("X-Preferred-Username") String username,
            @Valid @RequestBody HousingServiceDTO housingServiceDTO) {
        log.info("POST /housing with for user: {}", username);
        return serviceSerivce.createService(housingServiceDTO.toService(), username);
    }

    @PostMapping("/ecowalk")
    public Service createEcoWalkService(@RequestHeader("X-Preferred-Username") String username,
            @Valid @RequestBody EcoWalkServiceDTO ecoWalkServiceDTO) {
        log.info("POST /ecowalk for user: {}", username);
        return serviceSerivce.createService(ecoWalkServiceDTO.toService(), username);
    }

    @PostMapping("/transport")
    public Service createTransportService(@RequestHeader("X-Preferred-Username") String username,
            @Valid @RequestBody TransportServiceDTO transportServiceDTO) {
        log.info("POST /transport for user: {}", username);
        return serviceSerivce.createService(transportServiceDTO.toService(), username);
    }
}
