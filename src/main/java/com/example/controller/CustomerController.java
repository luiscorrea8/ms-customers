package com.example.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
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
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/customers")
@Slf4j
public class CustomerController {

	@Autowired
	CustomerService customerService;

	@PostMapping("/create")
	@CircuitBreaker(name = "CircuitBreakerApi", fallbackMethod = "fallbackCreateCustomer")
	@TimeLimiter(name = "CircuitBreakerApi")
	public Mono<Customer> createCustomer(@RequestBody Customer customer) {
		log.info("Creating customer: {}", customer);
		return customerService.createCustomer(customer);
	}

	public Mono<Customer> fallbackCreateCustomer(Customer customer, Throwable t) {
		log.error("Fallback for createCustomer with body {}: {}", customer, t.getMessage());
		return Mono.error(new RuntimeException("Service is currently unavailable. Please try again later."));
	}

	@GetMapping("/getCustomerTypeById/{id}")
	@CircuitBreaker(name = "CircuitBreakerApi", fallbackMethod = "fallbackGetCustomerTypeById")
	@TimeLimiter(name = "CircuitBreakerApi")
	public Mono<CustomerType> getCustomerTypeById(@PathVariable String id) {
		log.info("Getting customerType by id: {}", id);
		return customerService.customerType(id);
	}
	
	@GetMapping("/getById/{id}")
	@CircuitBreaker(name = "CircuitBreakerApi", fallbackMethod = "fallbackGetCustomerById")
	@TimeLimiter(name = "CircuitBreakerApi")
	public Mono<Customer> getCustomerById(@PathVariable String id) {
		log.info("Getting customer by id: {}", id);
		return customerService.getCustomerById(id);
	}

	public Mono<CustomerType> fallbackGetCustomerTypeById(String id, Throwable t) {
		log.error("Fallback for getCustomerTypeById with id {}: {}", id, t.getMessage());
		return Mono.error(new RuntimeException("Service is currently unavailable. Please try again later."));
	}

	public Mono<Customer> fallbackGetCustomerById(String id, Throwable t) {
		log.error("Fallback for getCustomerById with id {}: {}", id, t.getMessage());
		// Aquí puedes devolver un cliente por defecto o simplemente propagar el error.
		return Mono.error(new RuntimeException("Service is currently unavailable. Please try again later."));
	}
	
	@GetMapping("/all")
	@CircuitBreaker(name = "CircuitBreakerApi", fallbackMethod = "fallbackGetAllCustomers")
	@TimeLimiter(name = "CircuitBreakerApi")
	public Flux<Customer> getAllCustomers() {
		log.info("Getting all customers");
		return customerService.getAllCustomers();
	}

	public Flux<Customer> fallbackGetAllCustomers(Throwable t) {
		log.error("Fallback for getAllCustomers: {}", t.getMessage());
		return Flux.error(new RuntimeException("Service is currently unavailable. Please try again later."));
	}

	@PutMapping("/update")
	@CircuitBreaker(name = "CircuitBreakerApi", fallbackMethod = "fallbackUpdateCustomer")
	@TimeLimiter(name = "CircuitBreakerApi")
	public Mono<Customer> updateCustomer(@RequestBody Customer customer) {
		log.info("Updating customer: {}", customer);
		return customerService.updateCustomer(customer);
	}

	public Mono<Customer> fallbackUpdateCustomer(Customer customer, Throwable t) {
		log.error("Fallback for updateCustomer with body {}: {}", customer, t.getMessage());
		return Mono.error(new RuntimeException("Service is currently unavailable. Please try again later."));
	}

	@DeleteMapping("/delete/{id}")
	@CircuitBreaker(name = "CircuitBreakerApi", fallbackMethod = "fallbackDeleteCustomer")
	@TimeLimiter(name = "CircuitBreakerApi")
	public Mono<Void> deleteCustomer(@PathVariable String id) {
		log.info("Deleting customer with id: {}", id);
		return customerService.deleteCustomer(id);
	}

	public Mono<Void> fallbackDeleteCustomer(String id, Throwable t) {
		log.error("Fallback for deleteCustomer with id {}: {}", id, t.getMessage());
		return Mono.error(new RuntimeException("Service is currently unavailable. Please try again later."));
	}
}
