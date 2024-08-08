package vw.him.car.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vw.him.car.entity.User;
import vw.him.car.exception.UserAlreadyExists;
import vw.him.car.exception.UserNotFoundException;
import vw.him.car.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class UserServiceTest {

    @Mock
  UserRepository userRepository;

    @InjectMocks
    UserService userService;


    @Test
    void testGetAllUsers(){
        List<User> lst=new ArrayList<>();
        lst.add(new User());
        lst.add(new User());

        when(userRepository.findAll()).thenReturn(lst);

        List<User>userList=userService.getAllUsers();
        assertNotNull(userList);
        assertEquals(lst.size(),userList.size());
        assertEquals(lst,userList);
    }

    @Test
    void testGetUserById(){
        Long id=1L;
        User u=new User();
        when(userRepository.findById(id)).thenReturn(Optional.of(u));

        Optional<User> foundedUser=userService.getUserById(id);
        assertNotNull(foundedUser);
        verify(userRepository,times(2)).findById(id);

    }


    @Test
    void testGetUserById_UserNotFound() {
        long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        Throwable exception = assertThrows(UserNotFoundException.class, () -> userService.getUserById(id));
        assertNotNull(exception);
        assertEquals("User Not Found", exception.getMessage());
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void testSaveUser_Success() {

        User newUser=new User();
        newUser.setEmail("Luv@gmail.com");
        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(null);
        when(userRepository.save(newUser)).thenReturn(newUser);


        User savedUser = userService.saveUser(newUser);

        assertNotNull(savedUser);
        assertEquals(newUser.getEmail(), savedUser.getEmail());

        verify(userRepository, times(1)).findByEmail(newUser.getEmail());
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    void testSaveUser_UserAlreadyExists() {
        User existingUser = new User();
        existingUser.setEmail("nirmal@gmail.com");

        User newUser = new User();
        newUser.setEmail("nirmal@gmail.com");

        when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(existingUser);

        Throwable exception = assertThrows(UserAlreadyExists.class, () -> userService.saveUser(newUser));

        assertNotNull(exception);
        assertEquals("User is present please login", exception.getMessage());


        verify(userRepository, times(1)).findByEmail(newUser.getEmail());
        verify(userRepository, never()).save(newUser);
    }
}



