package com.example.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "address_user")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String street;

    String district;

    String city;

    String description;

}