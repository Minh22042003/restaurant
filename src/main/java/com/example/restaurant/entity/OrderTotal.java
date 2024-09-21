package com.example.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderTotal {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String name;

    int amount;

    @ManyToOne
    @JoinColumn(name = "order_id")
    Order order;
}
