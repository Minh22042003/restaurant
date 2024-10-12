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
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    User user;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "order_id", referencedColumnName = "id"),
            @JoinColumn(name = "tx_time", referencedColumnName = "order_time")
    })
    Order order;

    String tx_notes;

    int tx_amount;
}
