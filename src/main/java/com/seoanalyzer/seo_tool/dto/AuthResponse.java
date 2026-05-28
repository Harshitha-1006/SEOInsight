package com.seoanalyzer.seo_tool.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private String token;      // JWT token sent back to frontend
    private String email;
    private String fullName;
    private String role;
}