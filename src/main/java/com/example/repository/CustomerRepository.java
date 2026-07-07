package com.example.repository;

import com.example.entity.Customer;
import com.example.entity.enums.CustomerType;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {

    Customer findByEmail(String email);

    Customer findByDocumentNumber(String documentNumber);

    List<Customer> findByCustomerType(CustomerType customerType);

}
