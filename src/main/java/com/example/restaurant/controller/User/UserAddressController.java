package com.example.restaurant.controller.User;

import com.example.restaurant.dto.request.UpdateAddressUserDTO;
import com.example.restaurant.entity.AddressUser;
import com.example.restaurant.service.AddressUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/user/address")
public class UserAddressController {
    @Autowired
    private AddressUserService addressUserService;

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleConflict(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }

    @GetMapping
    public ResponseEntity<AddressUser> getAddressDetail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return ResponseEntity.ok().body(addressUserService.getDetails(email));
    }

    @PutMapping
    public ResponseEntity<AddressUser> updateAddressDetail(@ModelAttribute UpdateAddressUserDTO request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return ResponseEntity.ok().body(addressUserService.updateDetails(email, request));
    }
}
