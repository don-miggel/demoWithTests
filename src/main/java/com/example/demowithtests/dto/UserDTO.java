package com.example.demowithtests.dto;

import com.example.demowithtests.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String email;
    private UserRole role;
}
