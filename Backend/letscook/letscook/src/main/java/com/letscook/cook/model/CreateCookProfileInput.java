package com.letscook.cook.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateCookProfileInput {
    private Long id;
    private String businessName;
    private String address;
    private MultipartFile profilePhoto;
    private MultipartFile businessDocument;
}
