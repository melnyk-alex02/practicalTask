package com.alex.practicalTask.serviceTests;

import com.alex.practicalTask.dto.UserCreateDTO;
import com.alex.practicalTask.dto.UserDTO;
import com.alex.practicalTask.dto.UserUpdateDTO;
import com.alex.practicalTask.entity.User;
import com.alex.practicalTask.exception.DataNotFoundException;
import com.alex.practicalTask.exception.InvalidDataException;
import com.alex.practicalTask.mapper.UserMapper;
import com.alex.practicalTask.repository.UserRepository;
import com.alex.practicalTask.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;


    @Test
    public void testSearchUsersByDateOfBirth() {
        LocalDate fromDate = LocalDate.of(1999, 1, 1);
        LocalDate toDate = LocalDate.of(2005, 1, 1);

        List<User> userList = createUserList();

        List<UserDTO> userDTOList = createUserDTOList();

        when(userRepository.findUsersByDateOfBirth(fromDate, toDate)).thenReturn(userList);
        when(userMapper.toDto(userList)).thenReturn(userDTOList);

        List<UserDTO> result = userService.searchUsersByDateOfBirth(fromDate, toDate);

        verify(userRepository).findUsersByDateOfBirth(fromDate, toDate);
        verify(userMapper).toDto(userList);

        assertEquals(userDTOList.size(), result.size());
    }

    @Test
    public void testCreateUser() {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setFirstName("First name");
        userCreateDTO.setLastName("Last name");
        userCreateDTO.setDateOfBirth(LocalDate.of(2000, 1, 1));
        userCreateDTO.setEmail("email@exmaple.com");

        User userToSave = new User();
        userToSave.setId(null);
        userToSave.setFirstName("First name");
        userToSave.setLastName("Last name");
        userToSave.setDateOfBirth(LocalDate.of(2000, 1, 1));
        userToSave.setEmail("example@email.com");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setFirstName("First name");
        savedUser.setLastName("Last name");
        savedUser.setDateOfBirth(LocalDate.of(2000, 1, 1));
        savedUser.setEmail("example@email.com");

        UserDTO expectedUserDTO = new UserDTO();
        expectedUserDTO.setId(1L);
        expectedUserDTO.setFirstName("First name");
        expectedUserDTO.setLastName("Last name");
        expectedUserDTO.setDateOfBirth(LocalDate.of(2000, 1, 1));
        expectedUserDTO.setEmail("example@email.com");

        when(userMapper.toEntity(userCreateDTO)).thenReturn(userToSave);
        when(userRepository.save(userToSave)).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(expectedUserDTO);

        UserDTO result = userService.createUser(userCreateDTO);

        verify(userMapper).toEntity(userCreateDTO);
        verify(userRepository).save(userToSave);
        verify(userMapper).toDto(savedUser);

        assertEquals(result.getId(), expectedUserDTO.getId());
    }

    @Test
    public void testDeleteUser() {
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUser(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    public void testUpdateUser() {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setId(1L);
        userUpdateDTO.setFirstName("name");
        userUpdateDTO.setLastName("surname");
        userUpdateDTO.setEmail("example@email.com");
        userUpdateDTO.setDateOfBirth(LocalDate.of(2000, 1, 1));

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setFirstName("name");
        updatedUser.setLastName("surname");
        updatedUser.setEmail("example@email.com");
        updatedUser.setDateOfBirth(LocalDate.of(2000, 1, 1));

        UserDTO expectedUserDTO = new UserDTO();
        expectedUserDTO.setId(1L);
        expectedUserDTO.setFirstName("name");
        expectedUserDTO.setLastName("name");
        expectedUserDTO.setDateOfBirth(LocalDate.of(2000, 1, 1));
        expectedUserDTO.setEmail("example@email.com");

        when(userRepository.existsById(userUpdateDTO.getId())).thenReturn(true);
        when(userRepository.save(any())).thenReturn(updatedUser);
        when(userMapper.toEntity(userUpdateDTO)).thenReturn(updatedUser);
        when(userMapper.toDto(updatedUser)).thenReturn(expectedUserDTO);

        UserDTO result = userService.updateUser(userUpdateDTO);

        verify(userRepository).existsById(userUpdateDTO.getId());
        verify(userRepository).save(any());
        verify(userMapper).toEntity(userUpdateDTO);
        verify(userMapper).toDto(updatedUser);

        assertEquals(result.getId(), expectedUserDTO.getId());
    }

    @Test
    public void updateSeveralUsers() {
        List<UserUpdateDTO> userUpdateDTOS = new ArrayList<>();

        UserUpdateDTO userUpdateDTO1 = new UserUpdateDTO();
        userUpdateDTO1.setId(1L);
        userUpdateDTO1.setFirstName("First name 1");
        userUpdateDTO1.setLastName("Last name 1");
        userUpdateDTO1.setEmail("example1@email.com");
        userUpdateDTO1.setDateOfBirth(LocalDate.of(2000, 1, 1));

        UserUpdateDTO userUpdateDTO2 = new UserUpdateDTO();
        userUpdateDTO2.setId(1L);
        userUpdateDTO2.setFirstName("First name 2");
        userUpdateDTO2.setLastName("Last name 2");
        userUpdateDTO2.setEmail("exampl2e@email.com");
        userUpdateDTO2.setDateOfBirth(LocalDate.of(2001, 1, 1));

        userUpdateDTOS.add(userUpdateDTO1);
        userUpdateDTOS.add(userUpdateDTO2);

        List<User> userList = createUserList();

        List<UserDTO> expectedUserDTOS = createUserDTOList();

        when(userRepository.existsById(userUpdateDTOS.get(0).getId())).thenReturn(true);
        when(userRepository.existsById(userUpdateDTOS.get(1).getId())).thenReturn(true);
        when(userRepository.save(any())).thenReturn(userList.get(0));
        when(userRepository.save(any())).thenReturn(userList.get(1));
        when(userMapper.toEntity(userUpdateDTOS.get(0))).thenReturn(userList.get(0));
        when(userMapper.toEntity(userUpdateDTOS.get(1))).thenReturn(userList.get(1));

        List<UserDTO> result = userService.updateSeveralUsers(userUpdateDTOS);

        verify(userRepository, times(2)).existsById(any());
        verify(userRepository, times(2)).save(any());
        verify(userMapper, times(2)).toEntity(any(UserUpdateDTO.class));
        verify(userMapper, times(2)).toDto(any(User.class));

        assertEquals(result.size(), expectedUserDTOS.size());
    }

    @Test
    public void testSearchUsersByDateOfBirth_whenFormDateIsInvalid_shouldThrowException() {
        LocalDate fromDate = LocalDate.of(3000, 1, 1);
        LocalDate toDate = LocalDate.of(2001, 1, 1);

        assertThrows(InvalidDataException.class, () -> userService.searchUsersByDateOfBirth(fromDate, toDate));
    }

    @Test
    public void testCreateUser_whenUserIsUnder18_shouldThrowException() {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setFirstName("First name");
        userCreateDTO.setLastName("Last name");
        userCreateDTO.setDateOfBirth(LocalDate.of(2009, 1, 1));//less than 18 years old
        userCreateDTO.setEmail("email@exmaple.com");

        when(userService.createUser(userCreateDTO)).thenThrow(InvalidDataException.class);

        assertThrows(InvalidDataException.class, () -> userService.createUser(userCreateDTO));
    }

    @Test
    public void testCreateUser_whenDateOfBirthIsInvalid_shouldThrowException() {
        UserCreateDTO userCreateDTO = new UserCreateDTO();
        userCreateDTO.setFirstName("First name");
        userCreateDTO.setLastName("Last name");
        userCreateDTO.setDateOfBirth(LocalDate.of(3000, 1, 1));
        userCreateDTO.setEmail("email@exmaple.com");


        assertThrows(InvalidDataException.class, () -> userService.createUser(userCreateDTO));
    }

    @Test
    public void testDeleteUser_whenUserDoesNotExist_shouldThrowException() {
        Long id = 124L;

        when(userRepository.existsById(id)).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> userService.deleteUser(id));
    }

    @Test
    public void testUpdateUser_whenUserDoesNotExist() {
        UserUpdateDTO userUpdateDTO1 = new UserUpdateDTO();
        userUpdateDTO1.setId(123L);
        userUpdateDTO1.setFirstName("First name 1");
        userUpdateDTO1.setLastName("Last name 1");
        userUpdateDTO1.setEmail("example1@email.com");
        userUpdateDTO1.setDateOfBirth(LocalDate.of(2000, 1, 1));
        when(userRepository.existsById(userUpdateDTO1.getId())).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> userService.updateUser(userUpdateDTO1));
    }

    @Test
    public void testUpdateUser_whenDateOfBirthIsInvalid_shouldThrowException() {
        UserUpdateDTO userUpdateDTO1 = new UserUpdateDTO();
        userUpdateDTO1.setId(123L);
        userUpdateDTO1.setFirstName("First name 1");
        userUpdateDTO1.setLastName("Last name 1");
        userUpdateDTO1.setEmail("example1@email.com");
        userUpdateDTO1.setDateOfBirth(LocalDate.of(2024, 1, 1));

        when(userRepository.existsById(userUpdateDTO1.getId())).thenReturn(true);

        assertThrows(InvalidDataException.class, () -> userService.updateUser(userUpdateDTO1));
    }

    @Test
    public void testUpdateUser_whenUserIsUnder18_shouldThrowException() {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setId(123L);
        userUpdateDTO.setFirstName("First name 1");
        userUpdateDTO.setLastName("Last name 1");
        userUpdateDTO.setEmail("example1@email.com");
        userUpdateDTO.setDateOfBirth(LocalDate.of(2009, 1, 1));// less than 18 years old

        when(userRepository.existsById(userUpdateDTO.getId())).thenReturn(true);
        when(userService.updateUser(userUpdateDTO)).thenThrow(InvalidDataException.class);

        assertThrows(InvalidDataException.class, () -> userService.updateUser(userUpdateDTO));
    }

    @Test
    public void testUpdateSeveralUsers_whenUserDoesNotExist_shouldThrowException() {
        List<UserUpdateDTO> userUpdateDTOS = new ArrayList<>();

        UserUpdateDTO userUpdateDTO1 = new UserUpdateDTO();
        userUpdateDTO1.setId(1L);
        userUpdateDTO1.setFirstName("First name 1");
        userUpdateDTO1.setLastName("Last name 1");
        userUpdateDTO1.setEmail("example1@email.com");
        userUpdateDTO1.setDateOfBirth(LocalDate.of(2000, 1, 1));

        UserUpdateDTO userUpdateDTO2 = new UserUpdateDTO();
        userUpdateDTO2.setId(1L);
        userUpdateDTO2.setFirstName("First name 2");
        userUpdateDTO2.setLastName("Last name 2");
        userUpdateDTO2.setEmail("exampl2e@email.com");
        userUpdateDTO2.setDateOfBirth(LocalDate.of(2001, 1, 1));

        userUpdateDTOS.add(userUpdateDTO1);
        userUpdateDTOS.add(userUpdateDTO2);


        when(userRepository.existsById(userUpdateDTO1.getId())).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> userService.updateSeveralUsers(userUpdateDTOS));
    }

    @Test
    public void testUpdateSeveralUsers_whenDateOfBirthIsInvalid_shouldThrowException() {
        List<UserUpdateDTO> userUpdateDTOS = new ArrayList<>();

        UserUpdateDTO userUpdateDTO1 = new UserUpdateDTO();
        userUpdateDTO1.setId(1L);
        userUpdateDTO1.setFirstName("First name 1");
        userUpdateDTO1.setLastName("Last name 1");
        userUpdateDTO1.setEmail("example1@email.com");
        userUpdateDTO1.setDateOfBirth(LocalDate.of(2000, 1, 1));

        UserUpdateDTO userUpdateDTO2 = new UserUpdateDTO();
        userUpdateDTO2.setId(1L);
        userUpdateDTO2.setFirstName("First name 2");
        userUpdateDTO2.setLastName("Last name 2");
        userUpdateDTO2.setEmail("exampl2e@email.com");
        userUpdateDTO2.setDateOfBirth(LocalDate.of(3000, 1, 1));

        userUpdateDTOS.add(userUpdateDTO1);
        userUpdateDTOS.add(userUpdateDTO2);

        when(userRepository.existsById(userUpdateDTO1.getId())).thenReturn(true);
        when(userRepository.existsById(userUpdateDTO2.getId())).thenReturn(true);

        assertThrows(InvalidDataException.class, () -> userService.updateSeveralUsers(userUpdateDTOS));
    }

    @Test
    public void testUpdateSeveralUsers_whenUserIsUnder18_shouldThrowException() {
        List<UserUpdateDTO> userUpdateDTOS = new ArrayList<>();

        UserUpdateDTO userUpdateDTO1 = new UserUpdateDTO();
        userUpdateDTO1.setId(1L);
        userUpdateDTO1.setFirstName("First name 1");
        userUpdateDTO1.setLastName("Last name 1");
        userUpdateDTO1.setEmail("example1@email.com");
        userUpdateDTO1.setDateOfBirth(LocalDate.of(2007, 1, 1));// less than 18 year old

        UserUpdateDTO userUpdateDTO2 = new UserUpdateDTO();
        userUpdateDTO2.setId(1L);
        userUpdateDTO2.setFirstName("First name 2");
        userUpdateDTO2.setLastName("Last name 2");
        userUpdateDTO2.setEmail("exampl2e@email.com");
        userUpdateDTO2.setDateOfBirth(LocalDate.of(2000, 1, 1));

        userUpdateDTOS.add(userUpdateDTO1);
        userUpdateDTOS.add(userUpdateDTO2);

        when(userRepository.existsById(userUpdateDTO1.getId())).thenReturn(true);
        when(userRepository.existsById(userUpdateDTO2.getId())).thenReturn(true);
        when(userService.updateSeveralUsers(userUpdateDTOS)).thenThrow(InvalidDataException.class);

        assertThrows(InvalidDataException.class, () -> userService.updateSeveralUsers(userUpdateDTOS));
    }

    private List<User> createUserList() {
        List<User> userList = new ArrayList<>();

        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("First name 1");
        user1.setLastName("Last name 2");
        user1.setDateOfBirth(LocalDate.of(2000, 1, 1));
        user1.setEmail("example1@email.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("First name 2");
        user2.setLastName("Last name 2");
        user2.setDateOfBirth(LocalDate.of(2001, 1, 1));
        user2.setEmail("example2@email.com");

        userList.add(user1);
        userList.add(user2);

        return userList;
    }

    private List<UserDTO> createUserDTOList() {
        List<UserDTO> userDTOList = new ArrayList<>();

        UserDTO userDTO1 = new UserDTO();
        userDTO1.setId(1L);
        userDTO1.setFirstName("First name 1");
        userDTO1.setLastName("Last name 1");
        userDTO1.setDateOfBirth(LocalDate.of(2000, 1, 1));
        userDTO1.setEmail("example1@email.com");

        UserDTO userDTO2 = new UserDTO();
        userDTO2.setId(2L);
        userDTO2.setFirstName("First name 2");
        userDTO2.setLastName("Last name 2");
        userDTO2.setDateOfBirth(LocalDate.of(2001, 1, 1));
        userDTO2.setEmail("example2@email.com");

        userDTOList.add(userDTO1);
        userDTOList.add(userDTO2);

        return userDTOList;
    }
}