package vw.him.car.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vw.him.car.entity.User;
import vw.him.car.exception.NotAuthorizedException;
import vw.him.car.exception.UserNotFoundException;
import vw.him.car.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        try{
            return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
        }
        catch (UserNotFoundException ex){
            throw new UserNotFoundException("Users not found");
        }

    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        try{
            return new ResponseEntity<>(userService.getUserById(id).get(), HttpStatus.OK);
        }
        catch (UserNotFoundException ex) {
            throw new UserNotFoundException("User not found");
        }

    }

    @DeleteMapping("user/{id}")
    public ResponseEntity<String>  deleteUser(@RequestHeader("Authorization") String jwt, @PathVariable long id) {
        try{
            String message= userService.deleteUser(jwt, id);
            return ResponseEntity.ok(message);
        }
        catch (UserNotFoundException ex) {
            throw new UserNotFoundException("User not found");
        } catch (NotAuthorizedException ex) {
            throw new NotAuthorizedException("Unauthorized operation");
        }

    }


    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) {
        User user = this.userService.getUserProfile(jwt);

        String message = "User profile retrieved successfully";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Custom-Message", message);

        return ResponseEntity.ok().headers(headers).body(user);
    }

}

