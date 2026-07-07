package com.example.controller;

import com.example.entity.Customer;
import com.example.entity.dto.ErrorResponse;
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

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/customers")
@Slf4j
public class CustomerController {

	@Autowired
	CustomerService customerService;

	@PostMapping("/create")
	@CircuitBreaker(name = "customerServiceCB", fallbackMethod = "fallbackCreateCustomer")
	@TimeLimiter(name = "customerServiceCB")
	public Single<Customer> createCustomer(@RequestBody Customer customer) {
		log.info("Creating customer: {}", customer);
		return customerService.createCustomer(customer);
	}

	@GetMapping("/getCustomerTypeById/{id}")
	@CircuitBreaker(name = "customerServiceCB", fallbackMethod = "fallbackGetCustomerType")
	@TimeLimiter(name = "customerServiceCB")
	public Single<CustomerType> getCustomerTypeById(@PathVariable String id) {
		log.info("Getting customerType by id: {}", id);
		return customerService.customerType(id);
	}

	@GetMapping("/getById/{id}")
	@CircuitBreaker(name = "customerServiceCB", fallbackMethod = "fallbackGetCustomer")
	@TimeLimiter(name = "customerServiceCB")
	public CompletableFuture<Customer> getCustomerById(@PathVariable String id) {
		log.info("Getting customer by id: {}", id);
		return CompletableFuture.supplyAsync(() -> customerService.getCustomerById(id).blockingGet());
	}

	@GetMapping("/all")
	@CircuitBreaker(name = "customerServiceCB", fallbackMethod = "fallbackGetAllCustomers")
	@TimeLimiter(name = "customerServiceCB")
	public CompletableFuture<List<Customer>> getAllCustomers() {
		log.info("Getting all customers");
		return CompletableFuture.supplyAsync(() -> customerService.getAllCustomers().toList().blockingGet());
	}

	@PutMapping("/update")
	@CircuitBreaker(name = "customerServiceCB", fallbackMethod = "fallbackUpdateCustomer")
	@TimeLimiter(name = "customerServiceCB")
	public Single<Customer> updateCustomer(@RequestBody Customer customer) {
		log.info("Updating customer: {}", customer);
		return customerService.updateCustomer(customer);
	}

	@DeleteMapping("/delete/{id}")
	@CircuitBreaker(name = "customerServiceCB", fallbackMethod = "fallbackDeleteCustomer")
	@TimeLimiter(name = "customerServiceCB")
	public Single<String> deleteCustomer(@PathVariable String id) {
		log.info("Deleting customer with id: {}", id);
		return customerService.deleteCustomer(id);
	}

	// --- Fallback Methods ---

	private CompletableFuture<Customer> fallbackGetCustomer(String id, Throwable t) {
		log.warn("Fallback for getCustomerById: {}. Error: {}", id, t.getMessage());
		// Lanza una excepción específica si el error es un Timeout para que el cliente reciba un error 503
		// o un error más informativo.
		throw new RuntimeException("Service is currently unavailable. Please try again later.");
	}

	private CompletableFuture<List<Customer>> fallbackGetAllCustomers(Throwable t) {
		log.warn("Fallback for getAllCustomers. Error: {}", t.getMessage());
		return CompletableFuture.completedFuture(Collections.emptyList());
	}

	// Implementa otros métodos fallback según sea necesario para create, update, delete y getCustomerTypeById
	// Ejemplo para delete:
	private Single<String> fallbackDeleteCustomer(String id, Throwable t) {
		log.warn("Fallback for deleteCustomer: {}. Error: {}", id, t.getMessage());
		return Single.just("Fallback: Could not delete customer " + id);
	}

	private Single<Customer> fallbackCreateCustomer(Customer customer, Throwable t) {
		log.warn("Fallback for createCustomer. Error: {}", t.getMessage());
		return Single.error(new RuntimeException("Could not create customer at this time."));
	}

	private Single<Customer> fallbackUpdateCustomer(Customer customer, Throwable t) {
		log.warn("Fallback for updateCustomer. Error: {}", t.getMessage());
		return Single.error(new RuntimeException("Could not update customer at this time."));
	}

	private Single<CustomerType> fallbackGetCustomerType(String id, Throwable t) {
		log.warn("Fallback for getCustomerTypeById: {}. Error: {}", id, t.getMessage());
		return Single.error(new RuntimeException("Could not get customer type at this time."));
	}
}
