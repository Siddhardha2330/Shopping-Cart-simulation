package com.shoppingcartsimulation.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Data
public class Login {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable=false, updatable=false)
    Long loginid;
    String username;
    String password;
    String email;
    String status; // Default value for status
    String role;
    LocalDateTime lastLogin; // Field to store the last login timestamp
}
