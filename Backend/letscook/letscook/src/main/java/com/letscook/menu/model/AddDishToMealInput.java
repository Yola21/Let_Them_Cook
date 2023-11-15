package com.letscook.menu.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class AddDishToMealInput {
    private String name;
    private String description;
    private String type;
    private MultipartFile image;
    private Long mealId;
}
