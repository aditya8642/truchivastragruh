package com.truchi.vastragruh.repository;

 import com.truchi.vastragruh.entity.Address;
 import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

 import java.util.List;
 import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {



    List<Address> findByUserId(Long userId);

    // Used to scope every read/update/delete to "this address AND this user" -
    // the core of preventing one user from touching another's address by id.
    Optional<Address> findByIdAndUserId(Long id, Long userId);
}
