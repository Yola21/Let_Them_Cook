package com.letscook.menu.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class UpdateMealToScheduleInput {
    private Long id;
    private String name;
    private Long maxOrderLimit;
    private String slot;
    private Date orderDeadline;
    private Date mealDate;
    private String image;
    private Double price;
    private Long scheduleId;
}
