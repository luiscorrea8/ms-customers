package com.example.controller;

import com.example.entity.Customer;
import com.example.entity.enums.CustomerType;
import com.example.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/customers")
@Slf4j
public class CustomerController {

	@Autowired
	CustomerService customerService;

	@PostMapping("/create")
	public Single<Customer> createCustomer(@RequestBody Customer customer) {
		log.info("Creating customer: {}", customer);
		return customerService.createCustomer(customer);
	}

	@GetMapping("/getCustomerTypeById/{id}")
	public Single<CustomerType> getCustomerTypeById(@PathVariable String id) {
		log.info("Getting customerType by id: {}", id);
		return customerService.customerType(id);
	}

	@GetMapping("/getById/{id}")
	public Single<Customer> getCustomerById(@PathVariable String id) {
		log.info("Getting customer by id: {}", id);
		return customerService.getCustomerById(id);
	}

	@GetMapping("/all")
	public Observable<Customer> getAllCustomers() {
		log.info("Getting all customers");
		return customerService.getAllCustomers();
	}

	@PutMapping("/update")
	public Single<Customer> updateCustomer(@RequestBody Customer customer) {
		log.info("Updating customer: {}", customer);
		return customerService.updateCustomer(customer);
	}

	@DeleteMapping("/delete/{id}")
	public Single<String> deleteCustomer(@PathVariable String id) {
		log.info("Deleting customer with id: {}", id);
		return customerService.deleteCustomer(id);
	}
}
