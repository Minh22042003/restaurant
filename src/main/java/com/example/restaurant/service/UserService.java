package com.example.restaurant.service;

import com.example.restaurant.dto.request.AddNewUserRequest;
import com.example.restaurant.dto.request.ReqRes;
import com.example.restaurant.dto.request.UpdateUserRequest;
import com.example.restaurant.entity.User;
import com.example.restaurant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
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

    public ReqRes register(ReqRes request){
        ReqRes reqRes = new ReqRes();

        try {
            if (userRepository.findByEmail(request.getEmail()).isPresent()){
                reqRes.setMessage("User existed");
                reqRes.setStatusCode(500);
                return reqRes;
            }
            User user = User.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .phoneNumber(request.getPhoneNumber())
                    .role("USER")
                    .build();

            User result = userRepository.save(user);
            if (!result.getId().isEmpty() && !result.getId().isBlank()){
                reqRes.setUser(result);
                reqRes.setMessage("Success");
                reqRes.setStatusCode(200);
            }
        } catch (Exception e) {
            reqRes.setMessage(e.getMessage());
            reqRes.setStatusCode(500);
        }
        return reqRes;
    }

    public ReqRes login(ReqRes request){
        ReqRes reqRes = new ReqRes();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),
                    request.getPassword()));
            var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            reqRes.setStatusCode(200);
            reqRes.setToken(jwt);
            reqRes.setRole(user.getRole());
            reqRes.setRefreshToken(refreshToken);
            reqRes.setExpirationTime("24Hrs");
            reqRes.setMessage("Successfully Logged In");
        } catch (Exception e) {
            reqRes.setMessage(e.getMessage());
            reqRes.setStatusCode(500);
        }
        return reqRes;
    }

    public ReqRes refreshToken(ReqRes refreshTokenRequest){
        ReqRes response = new ReqRes();
        try{
            String email = jwtUtils.exactUserName(refreshTokenRequest.getToken());
            User users = userRepository.findByEmail(email).orElseThrow();
            if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), users)) {
                var jwt = jwtUtils.generateToken(users);
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenRequest.getToken());
                response.setExpirationTime("24Hr");
                response.setMessage("Successfully Refreshed Token");
            }
            response.setStatusCode(200);
            return response;

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return response;
        }
    }

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

    public ReqRes getMyInfo(String email){
        ReqRes reqRes = new ReqRes();
        try {
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                reqRes.setUser(userOptional.get());
                reqRes.setStatusCode(200);
                reqRes.setMessage("successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }

        }catch (Exception e){
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while getting user info: " + e.getMessage());
        }
        return reqRes;
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
