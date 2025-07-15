package com.app.User.Model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Request {
    private String username;
    private String email;
    private String password;
}