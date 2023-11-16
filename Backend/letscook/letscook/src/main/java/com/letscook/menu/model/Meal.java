package com.letscook.menu.model;

import com.letscook.cook.model.Cook;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "meals")
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "max_order_limit", nullable = false)
    private Long maxOrderLimit;

    @Column(name = "slot", nullable = false)
    private String slot;

    @Column(name = "order_deadline", nullable = false)
    private Date orderDeadline;

    @Column(name = "current_order_count", nullable = true)
    private Long currentOrderCount;

    @Column(name = "meal_date", nullable = false)
    private Date mealDate;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Menu menu;
}