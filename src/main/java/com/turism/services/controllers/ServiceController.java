package com.turism.services.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turism.services.models.Service;
import com.turism.services.services.ServiceService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/services")
public class ServiceController {

    private ServiceService serviceSerivce;

    @Autowired
    public ServiceController(ServiceService serviceService) {
        this.serviceSerivce = serviceService;
    }

    @GetMapping
    public List<Service> getAllMyServices(@RequestHeader("X-Preferred-Username") String username, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
        log.info("GET /services with page: {} and limit: {} for user: {}", page, limit, username);
        return serviceSerivce.getAllMyServices(username, page, limit);
    }
}
