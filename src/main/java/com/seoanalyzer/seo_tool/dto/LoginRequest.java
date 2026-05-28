package com.seoanalyzer.seo_tool.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}