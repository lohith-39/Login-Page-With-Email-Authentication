package com.app.User.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="USERS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String email;

    private String password;
}