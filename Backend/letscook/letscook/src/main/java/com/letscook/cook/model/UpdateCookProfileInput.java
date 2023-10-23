package com.letscook.cook.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UpdateCookProfileInput {
    private Long id;
    private String address;
    private MultipartFile profilePhoto;
    private MultipartFile businessDocument;
}

