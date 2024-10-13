package com.example.restaurant.repository;

import com.example.restaurant.entity.AddressUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressUserRepository extends JpaRepository<AddressUser, String> {
}
