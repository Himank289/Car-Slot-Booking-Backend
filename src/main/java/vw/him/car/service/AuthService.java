package vw.him.car.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vw.him.car.configuration.JwtProvider;
import vw.him.car.entity.AuthResponse;
import vw.him.car.entity.LoginRequest;
import vw.him.car.entity.User;
import vw.him.car.exception.UserAlreadyExists;
import vw.him.car.exception.UserNotFoundException;
import vw.him.car.exception.WrongCredentials;
import vw.him.car.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
     UserRepository userRepository;
    @Autowired
     PasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;

    public AuthService() {
    }

    public ResponseEntity<AuthResponse> createUser(User user) {


        String email = user.getEmail();
        String password = user.getPassword();
        String role = user.getRole();
        String name = user.getName();


        User user1 = userRepository.findByEmail(email);
        if(user1 != null){
            throw new UserAlreadyExists("user already exist");
        }
        User userCreate = new User();

        userCreate.setEmail(email);
        userCreate.setPassword(passwordEncoder.encode(password));
        userCreate.setRole(role);
        userCreate.setName(name);

        User savedUser=userRepository.save(userCreate);

        userCreate.setId(savedUser.getId());


        Authentication authentication=new UsernamePasswordAuthenticationToken(email,password);
        String token= JwtProvider.generateToken(authentication);

        AuthResponse authResponse=new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Register successfully");
        authResponse.setStatus(true);
        authResponse.setUser(savedUser);

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
    }

    public ResponseEntity<AuthResponse> login(LoginRequest loginRequest) {
        String username=loginRequest.getEmail();
        String password=loginRequest.getPassword();

        Authentication authentication=authenticate(username, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token=JwtProvider.generateToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User existinguser = userRepository.findByEmail(username);
        User user = new User();
        user.setId(existinguser.getId());
        user.setEmail(userDetails.getUsername());
        user.setName(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String userRole = roles.isEmpty() ? "" : roles.get(0);

        user.setRole(userRole);

        AuthResponse authResponse=new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Login sucess");
        authResponse.setStatus(true);
        authResponse.setUser(user);

//       return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.OK);
        return ResponseEntity.ok(authResponse);
    }

    private Authentication authenticate(String username,String password)
    {

        UserDetails userDetails=this.userService.loadUserByUsername(username);
        System.out.println("user details are" +userDetails);
        if(userDetails==null)
        {
            throw new UserNotFoundException("User not Found");
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword()))
        {

            throw new WrongCredentials("Invalid username or password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }







}
