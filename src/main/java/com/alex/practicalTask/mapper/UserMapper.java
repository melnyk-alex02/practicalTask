package com.alex.practicalTask.mapper;

import com.alex.practicalTask.dto.UserCreateDTO;
import com.alex.practicalTask.dto.UserDTO;
import com.alex.practicalTask.dto.UserUpdateDTO;
import com.alex.practicalTask.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<UserDTO> toDto(List<User> userList);

    UserDTO toDto(User user);

    User toEntity(UserCreateDTO createUserDTO);

    User toEntity(UserUpdateDTO userUpdateDTOS);
}
