package com.k40.authmicroservice.service;

import com.k40.authmicroservice.dto.RegisterCompanyRequest;
import com.k40.authmicroservice.dto.RegisterUserRequest;
import com.k40.authmicroservice.enums.RegistrationStatus;
import com.k40.authmicroservice.enums.UserType;
import com.k40.authmicroservice.exception.BadRequestException;
import com.k40.authmicroservice.exception.ResourceNotFoundException;
import com.k40.authmicroservice.model.*;
import com.k40.authmicroservice.repos.ClientRepository;
import com.k40.authmicroservice.repos.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public Client registerCompany(RegisterCompanyRequest request) {
        if (clientRepository.existsByCompanyNumber(request.getCompanyNumber())) {
            throw new BadRequestException("Company number is already registered!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered!");
        }

        Client client = new Client();
        client.setCompanyName(request.getCompanyName());
        client.setCompanyNumber(request.getCompanyNumber());
        client.setCompanyType(request.getCompanyType());
        client.setBusinessActivity(request.getBusinessActivity());
        client.setKraPin(request.getKraPin());
        client.setPhysicalAddress(request.getPhysicalAddress());
        client.setCity(request.getCity());
        client.setCountry(request.getCountry());
        client.setStatus(RegistrationStatus.PENDING);

        Client savedClient = clientRepository.save(client);

        // Register the Director User
        RegisterUserRequest userRequest = new RegisterUserRequest();
        userRequest.setFirstName(request.getFirstName());
        userRequest.setLastName(request.getLastName());
        userRequest.setEmail(request.getEmail());
        userRequest.setIdPassportNumber(request.getIdPassportNumber());
        userRequest.setKraPin(request.getUserKraPin());
        userRequest.setNationality("Kenyan");
        userRequest.setPhoneNumber(request.getPhoneNumber());
        userRequest.setPhysicalAddress(request.getPhysicalAddress());
        userRequest.setCity(request.getCity());
        userRequest.setCountry(request.getCountry());
        userRequest.setClientId(savedClient.getId());

        userService.registerUser(userRequest, UserType.DIRECTOR);
        return savedClient;
    }

    public Client getClientById(String id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client updateClient(String id, Client client) {
        Client existingClient = getClientById(id);

        existingClient.setCompanyName(client.getCompanyName());
        existingClient.setBusinessActivity(client.getBusinessActivity());
        existingClient.setPhysicalAddress(client.getPhysicalAddress());
        existingClient.setCity(client.getCity());
        existingClient.setPostalAddress(client.getPostalAddress());
        existingClient.setCountry(client.getCountry());

        return clientRepository.save(existingClient);
    }

    public void deleteClient(String id) {
        Client client = getClientById(id);
        clientRepository.delete(client);
    }
}
