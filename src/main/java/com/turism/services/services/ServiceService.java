package com.turism.services.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

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

    public List<Service> getAllServices(String username) {
        User user = userRepository.findByUsername(username);
        List<Service> services = serviceRepository.findAllByUser(user);
        return services;
    }

    public Service updateService(Service service) {
        Service updatedService = serviceRepository.save(service);
        return updatedService;
    }
}
