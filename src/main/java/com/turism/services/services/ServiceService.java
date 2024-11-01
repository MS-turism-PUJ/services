package com.turism.services.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.turism.services.models.Service;
import com.turism.services.models.User;
import com.turism.services.repositories.ServiceRepository;
import com.turism.services.repositories.UserRepository;

@org.springframework.stereotype.Service
public class ServiceService {
    private ServiceRepository serviceRepository;
    private UserRepository userRepository;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository, UserRepository userRepository) {
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
    }

    public List<Service> getAllMyServices(String username, Integer page, Integer limit) {
        User user = userRepository.findByUsername(username);
        Pageable pageable = PageRequest.of(page - 1, limit);
        List<Service> services = serviceRepository.findByUser(user, pageable).getContent();
        return services;
    }

    public Service createService(Service service, String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new Error("User not found");
        }
        service.setUser(user);
        serviceRepository.save(service);
        return service;
    }
}
