package com.truchi.vastragruh.controller;

import com.truchi.vastragruh.entity.Address;
import com.truchi.vastragruh.entity.Category;
import com.truchi.vastragruh.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<Address> create(Authentication authentication,
                                          @RequestBody Address address) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(addressService.create(authentication.getName(), address));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Address> update(Authentication authentication,
                                          @PathVariable Long id,
                                          @RequestBody Address address) {
        return ResponseEntity.ok(addressService.update(authentication.getName(), id, address));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> getById(Authentication authentication,
                                           @PathVariable Long id) {
        return ResponseEntity.ok(addressService.getById(authentication.getName(), id));
    }

    @GetMapping
    public ResponseEntity<List<Address>> getAll(Authentication authentication) {
        return ResponseEntity.ok(addressService.getAllForUser(authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Authentication authentication,
                                       @PathVariable Long id) {
        addressService.delete(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
