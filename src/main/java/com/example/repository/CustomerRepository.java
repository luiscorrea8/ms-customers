package com.example.repository;

import com.example.entity.CustomerEntity;
import com.example.entity.enums.CustomerType;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends MongoRepository<CustomerEntity, String> {

    CustomerEntity findByEmail(String email);

    CustomerEntity findByDocumentNumber(String documentNumber);

    List<CustomerEntity> findByCustomerType(CustomerType customerType);

}
