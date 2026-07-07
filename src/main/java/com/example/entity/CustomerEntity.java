package com.example.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.example.entity.enums.CustomerType;
import com.example.entity.enums.DocumentType;
import com.example.entity.enums.Status;

@Document (collection = "customers")
@Data
@NoArgsConstructor
public class CustomerEntity {

    @Id
    private String id;

    @Field(name = "customer_type")
    private CustomerType customerType;

    @Field(name = "document_type")
    private DocumentType documentType;

    @Field(name = "document_number")
    private String documentNumber;

    private String name;

    @Field(name = "company_name")
    private String companyName;

    private String email;

    private Status status;
}
