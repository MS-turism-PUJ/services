package com.turism.services.repositories;

import com.turism.services.models.Service;
import com.turism.services.models.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, String> {
    public List<Service> findAllByUser(User user);
}
