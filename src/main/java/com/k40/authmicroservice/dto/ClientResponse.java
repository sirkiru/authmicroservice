package com.k40.authmicroservice.dto;

import com.k40.authmicroservice.enums.DocumentReference;
import com.k40.authmicroservice.model.Client;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ClientResponse {
    private String id;
    private String companyName;
    private String companyNumber;
    private String companyType;
    private String businessActivity;
    private String kraPin;
    private String physicalAddress;
    private String city;
    private String postalAddress;
    private String country;
    private List<DocumentReference> supportingDocuments;
    private String status;
    private LocalDateTime registrationDate;

    public static ClientResponse fromEntity(Client client) {
        ClientResponse response = new ClientResponse();
        response.setId(client.getId());
        response.setCompanyName(client.getCompanyName());
        response.setCompanyNumber(client.getCompanyNumber());
        response.setCompanyType(client.getCompanyType());
        response.setBusinessActivity(client.getBusinessActivity());
        response.setKraPin(client.getKraPin());
        response.setPhysicalAddress(client.getPhysicalAddress());
        response.setCity(client.getCity());
        response.setPostalAddress(client.getPostalAddress());
        response.setCountry(client.getCountry());
        response.setSupportingDocuments(client.getSupportingDocuments());
        response.setStatus(client.getStatus() != null ? client.getStatus().toString() : null);
        response.setRegistrationDate(client.getRegistrationDate());
        return response;
    }
}