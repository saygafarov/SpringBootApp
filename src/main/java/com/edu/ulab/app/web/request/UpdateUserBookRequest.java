package com.edu.ulab.app.web.request;

import lombok.Data;

import java.util.List;

@Data
public class UpdateUserBookRequest {
    private UpdateUserRequest userRequest;
    private List<UpdateBookRequest> bookRequests;
}
