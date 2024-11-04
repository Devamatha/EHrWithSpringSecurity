package com.techpixe.ehr.repository;

import com.techpixe.ehr.entity.Clients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientsRepository extends JpaRepository<Clients, Long> {
    Optional<Clients> findByEmail(String email);
}
