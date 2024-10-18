package com.turism.services.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.turism.services.models.Service;
import com.turism.services.models.User;
import com.turism.services.repositories.ServiceRepository;
import com.turism.services.repositories.UserRepository;

public class ServiceService {
    private ServiceRepository serviceRepository;
    private UserRepository userRepository;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository, UserRepository userRepository) {
        this.serviceRepository = serviceRepository;
        this.userRepository = userRepository;
    }

    public List<Service> getAllServices(String username, Integer page, Integer limit) {
        User user = userRepository.findByUsername(username);
        Pageable pageable = PageRequest.of(page, limit);
        List<Service> services = serviceRepository.findAllByUser(user, pageable).getContent();
        return services;
    }

    public Service createService(Service service) {
        Service createdService = serviceRepository.save(service);
        return createdService;
    }
}
