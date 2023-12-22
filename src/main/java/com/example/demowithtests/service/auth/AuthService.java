package com.example.demowithtests.service.auth;


import com.example.demowithtests.dto.AuthDTO;
import com.example.demowithtests.dto.LoginDTO;
import com.example.demowithtests.dto.RegisterDTO;
import com.example.demowithtests.dto.UserDTO;

public interface AuthService {

    AuthDTO login(LoginDTO loginDTO);
    UserDTO register(RegisterDTO registerDTO);
}
