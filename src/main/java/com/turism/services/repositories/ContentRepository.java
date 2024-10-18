package com.turism.services.repositories;

import com.turism.services.models.Content;
import com.turism.services.models.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, String> {
    Page<Content> findAllByUser(User user, Pageable pageable);
}
