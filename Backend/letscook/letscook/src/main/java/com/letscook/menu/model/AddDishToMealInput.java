package com.letscook.menu.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class AddDishToMealInput {
    private Long maxOrderLimit;
    private String slot;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date orderDeadline;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date mealDate;
    private Long menuId;
}
