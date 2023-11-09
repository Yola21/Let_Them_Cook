package com.letscook.order.model;

import com.letscook.cook.model.Cook;
import com.letscook.customer.model.Customer;
import com.letscook.enums.OrderStatus;
import com.letscook.menu.model.Meal;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type", nullable = true)
    private String type;

    @Column(name = "status", nullable = true)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "meal_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Meal meal;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Customer customer ;
}
