package com.example.restaurant.service;

import com.example.restaurant.dto.request.AddNewUserRequest;
import com.example.restaurant.dto.request.UpdateUserRequest;
import com.example.restaurant.entity.User;
import com.example.restaurant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoodService foodService;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User addUser(AddNewUserRequest request){
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this email already exists");
        }
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .phoneNumber(request.getPhoneNumber())
                .build();

        return userRepository.save(user);
    }

    public void deleteUser(String id){
        userRepository.deleteById(id);
    }

    public Page<User> listUser(int page, int size){
        return userRepository.findAll(PageRequest.of(page, size));
    }

    public User userDetail(String id){
        if (userRepository.findById(id).isEmpty()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User does not exists");
        }
        return userRepository.findById(id).get();
    }

    public User updateUser(UpdateUserRequest request, String id){
        try {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isEmpty()){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User does not exists");
            }

            User user = userOptional.get();

            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setType("Customer");

            if (request.getFile() != null && !request.getFile().isEmpty()) {
                String fileName = foodService.imageProcess(request.getFile());
                user.setImgURL("/uploads/" + fileName);
            }

            return userRepository.save(user);
        }catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating user: " + e.getMessage());
        }

    }
}
