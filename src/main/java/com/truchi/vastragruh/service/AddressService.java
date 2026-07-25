package com.truchi.vastragruh.service;

import com.truchi.vastragruh.entity.Address;

import java.util.List;

public interface AddressService {

    public Address create(String userEmail, Address address);
    public Address update(String userEmail, Long addressId, Address address);
    public void delete(String userEmail, Long addressId);
    public Address getById(String userEmail, Long addressId);
    public List<Address> getAllForUser(String userEmail);


}
