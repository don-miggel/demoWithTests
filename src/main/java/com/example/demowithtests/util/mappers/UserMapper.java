package com.example.demowithtests.util.mappers;

import com.example.demowithtests.domain.User;
import com.example.demowithtests.domain.UserRole;
import com.example.demowithtests.dto.UserDTO;

public class UserMapper {

    public static UserDTO userToUserDto(User user){
        return new UserDTO(user.getId(), user.getEmail(), UserRole.valueOf(user.getRole()));
    }
}
