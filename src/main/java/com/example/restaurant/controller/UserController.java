package com.example.restaurant.controller;

import com.example.restaurant.dto.request.AddNewUserRequest;
import com.example.restaurant.dto.request.ReqRes;
import com.example.restaurant.dto.request.UpdateUserRequest;
import com.example.restaurant.entity.User;
import com.example.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/admin/user")
public class UserController {
    @Autowired
    private UserService userService;

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleConflict(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<User> addUser(@RequestParam("name") String name,
                                        @RequestParam("email") String email,
                                        @RequestParam("phoneNumber") String phoneNumber,
                                        @RequestParam("password") String password){
        AddNewUserRequest request = AddNewUserRequest.builder()
                .name(name)
                .email(email)
                .phoneNumber(phoneNumber)
                .password(password)
                .build();
        return ResponseEntity.ok().body(userService.addUser(request));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<User>> listUser(@RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "size", defaultValue = "10") int size){
        return ResponseEntity.ok().body(userService.listUser(page, size));
    }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<User> userDetail(@PathVariable String userId){
        return ResponseEntity.ok().body(userService.userDetail(userId));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable String userId,
                                           @RequestParam("name") String name,
                                           @RequestParam("email") String email,
                                           @RequestParam("phoneNumber") String phoneNumber,
                                           @RequestParam(value = "file", required = false) MultipartFile file){
        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
                .name(name)
                .email(email)
                .phoneNumber(phoneNumber)
                .file(file)
                .build();

        User user = userService.updateUser(updateUserRequest, userId);

        return ResponseEntity.ok().body(user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<ReqRes> getMyProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ReqRes response = userService.getMyInfo(email);
        return  ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
