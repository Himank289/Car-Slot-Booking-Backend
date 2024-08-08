package vw.him.car.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import vw.him.car.configuration.JwtProvider;
import vw.him.car.entity.User;
import vw.him.car.exception.NotAuthorizedException;
import vw.him.car.exception.UserAlreadyExists;
import vw.him.car.exception.UserNotFoundException;
import vw.him.car.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(long id) {
        Optional< User>  user = userRepository.findById(id);
        if(user.isPresent()) {
            return userRepository.findById(id);
        }
        throw new UserNotFoundException("User Not Found");

    }

    public User saveUser(User user) {
        User fetchedUser = userRepository.findByEmail(user.getEmail());
        if(fetchedUser != null) {
            throw new UserAlreadyExists("User is present please login");
        }
        return userRepository.save(user);
    }

    public String deleteUser(String jwt,long id) {
        if (isAdmin(jwt)) {
            Optional<User> user = userRepository.findById(id);{
                if (user.isPresent()) {
                    userRepository.deleteById(id);
                    return "User Deleted";
                }}
            throw new UserNotFoundException("User Not Found");
        }

        throw new NotAuthorizedException("Not authorized");
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByEmail(username);
        System.out.println(user);
        if (user== null) {
            throw new UserNotFoundException("User not found.");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    public User getUserProfile(String jwt)
    {
        String email= JwtProvider.getEmailFromJwtToken(jwt);
        User fetchedUser=userRepository.findByEmail(email);
        if(fetchedUser!=null) {
            return userRepository.findByEmail(email);}
        throw new UserNotFoundException("User not found");

    }

    public boolean isAdmin(String jwt){
        User jwtUser=getUserProfile(jwt);
        String role = jwtUser.getRole();
        return role != null && role.equals("admin");
    }
}
