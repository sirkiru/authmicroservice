package com.k40.authmicroservice.dto;

import com.k40.authmicroservice.model.Client;
import com.k40.authmicroservice.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRegistrationResponse {
    private boolean success;
    private String message;
    private ClientResponse client;
    private UserResponse director;

    public static CompanyRegistrationResponse success(Client client, User director) {
        return new CompanyRegistrationResponse(
            true,
            "Company registered successfully",
            ClientResponse.fromEntity(client),
            UserResponse.fromEntity(director)
        );
    }
}