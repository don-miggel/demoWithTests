package com.example.demowithtests.service.auth;

import com.example.demowithtests.domain.User;
import com.example.demowithtests.domain.UserRole;
import com.example.demowithtests.dto.AuthDTO;
import com.example.demowithtests.dto.LoginDTO;
import com.example.demowithtests.dto.RegisterDTO;
import com.example.demowithtests.dto.UserDTO;
import com.example.demowithtests.repository.UserRepository;
import com.example.demowithtests.util.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceDefault implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthDTO login(LoginDTO loginDTO) {

        User u = userRepository.findByEmail(loginDTO.getEmail());
//        String encodedPassword = passwordEncoder.encode(loginDTO.getPassword());

        if (u != null) {

            if (passwordEncoder.matches(loginDTO.getPassword(), u.getPassword())) {

                UserDTO loggedInUser =  UserMapper.userToUserDto(u);
                AuthDTO authDTO = new AuthDTO();
                authDTO.setUserDTO(loggedInUser);
                authDTO.setAuthorized(true);
                return authDTO;
            }
            throw new RuntimeException("Passwords mismatch!");
        }
        throw new RuntimeException("User with email: "+ loginDTO.getEmail()+" is not found! ");
    }

    @Override
    public UserDTO register(RegisterDTO registerDTO) {
        User user = new User();
        User foundUser = userRepository.findByEmail(registerDTO.getEmail());
        if(foundUser != null)
            throw new RuntimeException("User with email: "+registerDTO.getEmail()+" already exists! ");
        String role = registerDTO.getRole().toUpperCase();

        if(!checkRole(role))
            throw new RuntimeException("Pointed out role : "+ role+" is not permitted!" +
                    " Please, choose ADMIN or USER! ");

        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRole(UserRole.valueOf(role).toString());
        User savedUser = userRepository.save(user);
        return UserMapper.userToUserDto(savedUser);
    }

    private boolean checkRole(String role){

        Set<String> roles = new HashSet<>();
        for(UserRole ur: UserRole.values())
            roles.add(ur.toString());
        return roles.contains(role);
    }
}
