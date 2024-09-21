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
public class OrderHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    User user;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "order_id", referencedColumnName = "id"),
            @JoinColumn(name = "history_status", referencedColumnName = "order_status"),
            @JoinColumn(name = "history_type", referencedColumnName = "order_type"),
            @JoinColumn(name = "history_time", referencedColumnName = "order_time")
    })
    Order order;
}
