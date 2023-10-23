package com.letscook.menu.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UpdateDishInput {
    private Long id;
    private String name;
    private MultipartFile image;
    private Long price;
    private String label;
}
