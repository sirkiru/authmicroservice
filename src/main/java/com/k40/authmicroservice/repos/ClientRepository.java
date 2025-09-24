package com.k40.authmicroservice.repos;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.k40.authmicroservice.model.Client;

public interface ClientRepository extends MongoRepository<Client, String> {
    Optional<Client> findByCompanyName(String companyName);
    Optional<Client> findByCompanyNumber(String companyNumber);
    Optional<Client> findByKraPin(String kraPin);
    boolean existsByCompanyNumber(String companyNumber);
    boolean existsByKraPin(String kraPin);
}
