package com.truchi.vastragruh.service;

import com.truchi.vastragruh.entity.Address;
import com.truchi.vastragruh.entity.User;
import com.truchi.vastragruh.repository.AddressRepository;
import com.truchi.vastragruh.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;


@Service

public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressServiceImpl(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }
    @Override
    @Transactional
    public Address create(String userEmail, Address address) {
        User user = getUser(userEmail);
        List<Address> existing = addressRepository.findByUserId(user.getId());

        // First address a user ever adds is always their default, regardless
        // of what the request body says.
        if (existing.isEmpty()) {
            address.setIsDefault(true);
        } else if (Boolean.TRUE.equals(address.getIsDefault())) {
            unsetExistingDefault(existing);
        }

        address.setUser(user);

        return addressRepository.save(address);
    }

    @Override
    @Transactional
    public Address update(String userEmail, Long addressId, Address address) {
        User user = getUser(userEmail);
        Address existingAddress = getOwnedAddress(user.getId(), addressId);

        existingAddress.setFullName(address.getFullName());
        existingAddress.setPhone(address.getPhone());
        existingAddress.setLine1(address.getLine1());
        existingAddress.setLine2(address.getLine2());
        existingAddress.setCity(address.getCity());
        existingAddress.setState(address.getState());
        existingAddress.setPincode(address.getPincode());
        existingAddress.setType(address.getType());

        if (Boolean.TRUE.equals(address.getIsDefault()) && !Boolean.TRUE.equals(existingAddress.getIsDefault())) {
            unsetExistingDefault(addressRepository.findByUserId(user.getId()));
            existingAddress.setIsDefault(true);
        }

        return addressRepository.save(existingAddress);
    }

    @Override
    public void delete(String userEmail, Long addressId) {

    }

    @Override
    public Address getById(String userEmail, Long addressId) {
        return null;
    }

    @Override
    public List<Address> getAllForUser(String userEmail) {
        return addressRepository.findAll();
    }

    private void unsetExistingDefault(List<Address> addresses) {
        addresses.stream()
                .filter(a -> Boolean.TRUE.equals(a.getIsDefault()))
                .forEach(a -> {
                    a.setIsDefault(false);
                    addressRepository.save(a);
                });
    }
    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
    private Address getOwnedAddress(Long userId, Long addressId) {
        return addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));
    }
}
