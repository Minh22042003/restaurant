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
public class FoodOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "food_id", referencedColumnName = "id"),
            @JoinColumn(name = "food_name", referencedColumnName = "name"),
            @JoinColumn(name = "food_price", referencedColumnName = "price")
    })
    Food food;

    @ManyToOne
    @JoinColumn(name = "order_id")
    Order order;

    int item_quantity;
}
