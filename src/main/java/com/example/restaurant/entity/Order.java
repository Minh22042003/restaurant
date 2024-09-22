package com.example.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

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

    String customer_mail;

    String customer_name;

    String customer_address;

    String order_type;

    Boolean order_status;

    LocalDateTime order_time;

    @OneToMany(mappedBy = "order")
    Set<FoodOrder> foodOrders;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @PrePersist
    protected void onCreate(){
        order_time =  LocalDateTime.now();
    }
}
