package com.alex.practicalTask.service;

import com.alex.practicalTask.dto.UserCreateDTO;
import com.alex.practicalTask.dto.UserDTO;
import com.alex.practicalTask.dto.UserUpdateDTO;
import com.alex.practicalTask.exception.DataNotFoundException;
import com.alex.practicalTask.exception.InvalidDataException;
import com.alex.practicalTask.mapper.UserMapper;
import com.alex.practicalTask.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Value("${user.ageLimit}")
    private int ageLimit;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserDTO> searchUsersByDateOfBirth(LocalDate fromDate, LocalDate toDate) {
        if (fromDate.isAfter(toDate)) {
            throw new InvalidDataException("fromDate must be before toDate");
        }
        return userMapper.toDto(userRepository.findUsersByDateOfBirth(fromDate, toDate));
    }

    public UserDTO createUser(UserCreateDTO userCreateDTO) {
        if (Period.between(userCreateDTO.getDateOfBirth(), LocalDate.now()).getYears() < ageLimit) {
            throw new InvalidDataException("You must be older than " + ageLimit);
        }
        if (userCreateDTO.getDateOfBirth().isAfter(LocalDate.now())) {
            throw new InvalidDataException("Date of birth has invalid value, please check it");
        }


        return userMapper.toDto(userRepository.save(userMapper.toEntity(userCreateDTO)));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new DataNotFoundException("There is no user with id " + id);
        }
        userRepository.deleteById(id);
    }

    public UserDTO updateUser(UserUpdateDTO userUpdateDTO) {
        checkUserDateOfBirthAndId(userUpdateDTO.getDateOfBirth(), userUpdateDTO.getId());

        return userMapper.toDto(userRepository.save(userMapper.toEntity(userUpdateDTO)));
    }

    public List<UserDTO> updateSeveralUsers(List<UserUpdateDTO> userUpdateDTOS) {
        List<UserDTO> userDTOS = new ArrayList<>();
        for (UserUpdateDTO userUpdateDTO : userUpdateDTOS) {
            checkUserDateOfBirthAndId(userUpdateDTO.getDateOfBirth(), userUpdateDTO.getId());

            userDTOS.add(userMapper.toDto(userRepository.save(userMapper.toEntity(userUpdateDTO))));
        }
        return userDTOS;
    }

    private void checkUserDateOfBirthAndId(LocalDate dateOfBirth, Long id) {
        if (!userRepository.existsById(id)) {
            throw new DataNotFoundException("There are no user with id " + id);
        }
        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new InvalidDataException("Date of birth has invalid value, please check it");
        }
        if (Period.between(dateOfBirth, LocalDate.now()).getYears() < ageLimit) {
            throw new InvalidDataException("You must be older than " + ageLimit);
        }
    }
}