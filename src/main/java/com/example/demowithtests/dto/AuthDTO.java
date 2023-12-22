package com.example.demowithtests.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AuthDTO {

    private UserDTO userDTO;
    private boolean authorized;
}
