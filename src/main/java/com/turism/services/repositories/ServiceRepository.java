package com.turism.services.repositories;

import com.turism.services.models.Service;
import com.turism.services.models.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, String> {
    Page<Service> findByContentUser(User user, Pageable pageable);
}
