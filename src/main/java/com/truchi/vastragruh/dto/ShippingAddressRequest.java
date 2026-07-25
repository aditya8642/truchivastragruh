package com.truchi.vastragruh.dto;

public record ShippingAddressRequest(String fullName,
                                     String phone,
                                     String line1,
                                     String line2,
                                     String city,
                                     String state,
                                     String pincode,
                                     String type) {
}
