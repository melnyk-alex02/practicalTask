package com.alex.practicalTask.restcontroller;

import com.alex.practicalTask.dto.UserCreateDTO;
import com.alex.practicalTask.dto.UserDTO;
import com.alex.practicalTask.dto.UserUpdateDTO;
import com.alex.practicalTask.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/search")
    public List<UserDTO> searchUsersByDateOfBirth(
            @RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ) {
        return userService.searchUsersByDateOfBirth(fromDate, toDate);
    }

    @PostMapping("/users")
    public UserDTO createUser(@RequestBody UserCreateDTO userCreateDTO) {
        return userService.createUser(userCreateDTO);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PutMapping("/user")
    public UserDTO updateUser(@RequestBody UserUpdateDTO userUpdateDTO) {
        return userService.updateUser(userUpdateDTO);
    }

    @PutMapping("/users")
    public List<UserDTO> updateSeveralUsers(@RequestBody List<UserUpdateDTO> userUpdateDTOList) {
        return userService.updateSeveralUsers(userUpdateDTOList);
    }
}
