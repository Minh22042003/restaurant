package com.example.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String name;

    String description;

    int price;

    @Column(columnDefinition = "boolean default false")
    boolean status;

    String imgURL;

    String category;

    @OneToMany(mappedBy = "food")
    Set<FoodOrder> foodOrders;
}
