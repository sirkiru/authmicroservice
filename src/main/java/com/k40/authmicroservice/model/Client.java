package com.k40.authmicroservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.k40.authmicroservice.enums.DocumentReference;
import com.k40.authmicroservice.enums.RegistrationStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "clients")
public class Client {
    @Id
    private String id;
    private String companyName;
    private String companyNumber;
    //private CompanyType companyType;
    private String companyType;
    //private BusinessActivity businessActivity; 
    private String businessActivity; 
    private String kraPin;
    private String physicalAddress;
    private String city;
    private String postalAddress;
    //private Country country; 
    private String country; 
    private List<DocumentReference> supportingDocuments;
    private RegistrationStatus status;
    private LocalDateTime registrationDate;
    
    public Client() {
        this.registrationDate = LocalDateTime.now();
        this.status = RegistrationStatus.PENDING;
    }
}