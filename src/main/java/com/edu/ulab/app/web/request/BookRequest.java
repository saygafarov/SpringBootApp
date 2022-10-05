package com.edu.ulab.app.web.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookRequest {
    private String title;
    private String author;
    private long pageCount;
}
