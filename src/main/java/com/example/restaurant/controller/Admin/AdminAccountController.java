package com.example.restaurant.controller.Admin;

import com.example.restaurant.dto.request.AddNewUserRequest;
import com.example.restaurant.dto.request.UpdateUserRequest;
import com.example.restaurant.entity.User;
import com.example.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/admin/account")
public class AdminAccountController {
    @Autowired
    private UserService userService;

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleConflict(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }

    @PostMapping
    public ResponseEntity<User> addUser(@ModelAttribute AddNewUserRequest request){
        return ResponseEntity.ok().body(userService.addUser(request));
    }

    @GetMapping
    public ResponseEntity<Page<User>> listUser(@RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "size", defaultValue = "10") int size){
        return ResponseEntity.ok().body(userService.listUser(page, size));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> userDetail(@PathVariable String userId){
        return ResponseEntity.ok().body(userService.userDetail(userId));
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> adminUpdateUser(@PathVariable String userId,
                                           @ModelAttribute UpdateUserRequest request){

        User user = userService.adminUpdateUser(request, userId);

        return ResponseEntity.ok().body(user);
    }
}
