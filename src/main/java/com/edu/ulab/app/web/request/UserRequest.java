package com.edu.ulab.app.web.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequest {
    private String fullName;
    private String title;
    private int age;
}
