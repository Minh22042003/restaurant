package com.example.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    Boolean order_status;

    int total;

    String description;

    LocalDateTime order_time;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToOne
    Payment payment;

    @OneToMany
    List<FoodOrder> listFoodOrder;

    @PrePersist
    protected void onCreate(){
        order_time =  LocalDateTime.now();
    }
}
