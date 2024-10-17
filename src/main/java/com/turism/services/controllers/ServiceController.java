package com.turism.services.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turism.services.models.Service;
import com.turism.services.services.ServiceService;

@RestController
@RequestMapping("/services")
public class ServiceController {

    private ServiceService serviceSerivce;

    @Autowired
    public ServiceController(ServiceService serviceService) {
        this.serviceSerivce = serviceService;
    }

    @GetMapping
    public List<Service> getAllMyServices(@RequestHeader("X-Preferred-Username") String username) {
        return serviceSerivce.getAllServices(username);

    }
}
