package com.example.restaurant.controller.User;

import com.example.restaurant.dto.request.ReqRes;
import com.example.restaurant.dto.request.UpdateUserRequest;
import com.example.restaurant.entity.User;
import com.example.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/user/account")
public class UserAccountController {
    @Autowired
    private UserService userService;

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleConflict(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }

    @PutMapping
    public ResponseEntity<User> userUpdateUser(
                                           @RequestParam("name") String name,
                                           @RequestParam("phoneNumber") String phoneNumber,
                                           @RequestParam(value = "file", required = false) MultipartFile file){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email_ = authentication.getName();
        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
                .name(name)
                .phoneNumber(phoneNumber)
                .file(file)
                .build();

        User user = userService.userUpdateUser(updateUserRequest, email_);

        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/profile")
    public ResponseEntity<ReqRes> getMyProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ReqRes response = userService.getMyInfo(email);
        return  ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
