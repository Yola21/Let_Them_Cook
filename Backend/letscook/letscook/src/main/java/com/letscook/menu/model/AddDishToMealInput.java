package com.letscook.menu.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class AddDishToMealInput {
    private Long maxOrderLimit;
    private String slot;
    private Date orderDeadline;
    private Date mealDate;
    private Long menuId;
}
