package com.letscook.menu.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UpdateMealToMenuInput {
    private Long id;
    private String name;
    private Long maxOrderLimit;
    private String slot;
    private Date orderDeadline;
    private Date mealDate;
    private Long menuId;
}
