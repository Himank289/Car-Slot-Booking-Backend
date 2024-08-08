package vw.him.car.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vw.him.car.entity.AuthResponse;
import vw.him.car.entity.LoginRequest;
import vw.him.car.entity.User;
import vw.him.car.service.AuthService;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")

public class AuthController {

    @Autowired
    AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUser(@Valid @RequestBody User user)  {
        return authService.createUser(user);

    }
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(@Valid @RequestBody LoginRequest loginRequest)
    {
        return authService.login(loginRequest);
    }




}

