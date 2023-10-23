package com.letscook.menu.model;

import com.letscook.cook.model.Cook;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "menus")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "image", nullable = true)
    private String image;

    @Column(name = "price", nullable = true)
    private Long price;

    @Column(name = "label", nullable = true)
    private String label;

    @ManyToOne
    @JoinColumn(name = "cook_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Cook cook;
}

