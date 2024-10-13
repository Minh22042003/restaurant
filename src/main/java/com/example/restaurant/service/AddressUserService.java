package com.example.restaurant.service;

import com.example.restaurant.dto.request.AddNewFoodCategoryDTO;
import com.example.restaurant.dto.request.UpdateAddressUserDTO;
import com.example.restaurant.dto.request.UpdateFoodCategoryDTO;
import com.example.restaurant.entity.AddressUser;
import com.example.restaurant.entity.FoodCategory;
import com.example.restaurant.entity.User;
import com.example.restaurant.repository.AddressUserRepository;
import com.example.restaurant.repository.FoodCategoryRepository;
import com.example.restaurant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressUserService {
    @Autowired
    private AddressUserRepository addressUserRepository;
    @Autowired
    private UserRepository userRepository;


    public AddressUser getDetails(String email) {
        try {
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isEmpty()){
                throw new RuntimeException("Email doesn't exit");
            }
            AddressUser addressUser = user.get().getAddressUser();

            if (addressUser != null){
                Optional<AddressUser> optionalAddressUser = addressUserRepository.findById(addressUser.getId());
                if (optionalAddressUser.isPresent()){
                    return optionalAddressUser.get();
                }else {
                    throw new RuntimeException("không tìm thấy AddressUser trong repository (mặc dù User có reference đến nó)");
                }
            }else {
                AddressUser newAddressUser = AddressUser.builder()
                        .street("")
                        .city("")
                        .district("")
                        .description("")
                        .build();
                user.get().setAddressUser(newAddressUser);
                userRepository.save(user.get());
                return newAddressUser;
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AddressUser updateDetails(String email, UpdateAddressUserDTO request) {
        try {
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isEmpty()){
                throw new RuntimeException("Email doesn't exit");
            }
            AddressUser addressUser = user.get().getAddressUser();

            addressUser.setCity(request.getCity());
            addressUser.setDistrict(request.getDistrict());
            addressUser.setDescription(request.getDescription());
            addressUser.setStreet(request.getStreet());

            return addressUserRepository.save(addressUser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
