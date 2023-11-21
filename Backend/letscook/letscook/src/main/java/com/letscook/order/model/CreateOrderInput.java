package com.letscook.order.model;

import com.letscook.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateOrderInput {
    private String type;
    private OrderStatus status;
    private Long customerId;
    private Long mealId;
    private List<MealorderInput> mealorderInputs;
}
