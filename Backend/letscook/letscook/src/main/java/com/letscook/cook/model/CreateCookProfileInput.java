package com.letscook.cook.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateCookProfileInput {
    private Long userId;
    private String businessName;
    private String address;
    private String profilePhoto;
    private String bannerImage;
    private String businessDocument;
}
