package com.example.demo.online.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactRequest {
    private String name;
    private String phone;
    private String email;
    private String message;
}
