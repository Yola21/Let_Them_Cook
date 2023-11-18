package com.letscook.menu.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
public class CreateScheduleInput {
    private String name;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date start_date;
    private Long cookId;

}
