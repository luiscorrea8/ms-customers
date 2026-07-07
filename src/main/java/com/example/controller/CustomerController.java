package com.example.controller;

import com.example.api.ApiApi;
import com.example.entity.CustomerEntity;
import com.example.model.Customer;
import com.example.model.Customer.DocumentTypeEnum;
import com.example.model.Customer.StatusEnum;
import com.example.model.CustomerType;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import com.example.service.CustomerService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CustomerController implements ApiApi {

	@Autowired
	CustomerService customerService;

	@Override
	@CircuitBreaker(name = "CircuitBreakerApi", fallbackMethod = "fallbackCreateCustomer")
	@TimeLimiter(name = "CircuitBreakerApi")
	public Mono<ResponseEntity<Customer>> createCustomer(@Valid @RequestBody Mono<Customer> customerMono, final ServerWebExchange exchange) {
		return customerMono.flatMap(customer -> {
			log.info("Creating customer: {}", customer);
			// Mapeo del modelo (API) a la entidad (BD)
			CustomerEntity customerEntity = new CustomerEntity();
			customerEntity.setCustomerType(com.example.entity.enums.CustomerType.valueOf(customer.getCustomerType().getValue()));
			customerEntity.setDocumentType(com.example.entity.enums.DocumentType.valueOf(customer.getDocumentType().getValue()));
			customerEntity.setDocumentNumber(customer.getDocumentNumber());
			customerEntity.setName(customer.getName());
			customerEntity.setCompanyName(customer.getCompanyName());
			customerEntity.setEmail(customer.getEmail());
			customerEntity.setStatus(com.example.entity.enums.Status.valueOf(customer.getStatus().getValue()));
			
			return customerService.createCustomer(customerEntity)
				.map(createdEntity -> ResponseEntity.ok(customer)); 
		});
	}

	public Mono<ResponseEntity<Customer>> fallbackCreateCustomer(Mono<Customer> customerMono, Throwable t) {
		log.error("Fallback for createCustomer: {}", t.getMessage());
		return Mono.just(ResponseEntity.status(503).build());
	}

	@CircuitBreaker(name = "CircuitBreakerApi", fallbackMethod = "fallbackGetCustomerTypeById")
	@TimeLimiter(name = "CircuitBreakerApi")
	@Override
	public Mono<ResponseEntity<CustomerType>> getCustomerTypeById(@PathVariable("id") String id, final ServerWebExchange exchange) {
		log.info("Getting customerType by id: {}", id);
		return customerService.customerType(id)
				.map(entityCustomerType -> CustomerType.fromValue(entityCustomerType.name()))
				.map(ResponseEntity::ok);
	}

	
	@CircuitBreaker(name = "CircuitBreakerApi", fallbackMethod = "fallbackGetCustomerById")
	@TimeLimiter(name = "CircuitBreakerApi")
	@Override
	public Mono<ResponseEntity<Customer>> getCustomerById(@PathVariable("id") String id, final ServerWebExchange exchange) {
		log.info("Getting customer by id: {}", id);
		return customerService.getCustomerById(id)
				.map(entity -> {
					// Mapeo de la entidad (BD) al modelo (API)
					Customer model = new Customer();
					model.setId(entity.getId());
					model.setCustomerType(CustomerType.fromValue(entity.getCustomerType().name()));
					model.setDocumentType(DocumentTypeEnum.valueOf(entity.getDocumentType().name()));
					model.setDocumentNumber(entity.getDocumentNumber());
					model.setName(entity.getName());
					model.setCompanyName(entity.getCompanyName());
					model.setEmail(entity.getEmail());
					model.setStatus(StatusEnum.valueOf(entity.getStatus().name()));
					return ResponseEntity.ok(model);
				})
				.switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
	}

	public Mono<ResponseEntity<CustomerType>> fallbackGetCustomerTypeById(String id, Throwable t) {
		log.error("Fallback for getCustomerTypeById with id {}: {}", id, t.getMessage());
		return Mono.just(ResponseEntity.status(503).build());
	}

	public Mono<ResponseEntity<Customer>> fallbackGetCustomerById(String id, Throwable t) {
		log.error("Fallback for getCustomerById with id {}: {}", id, t.getMessage());
		return Mono.just(ResponseEntity.status(503).build());
	}
	
	@CircuitBreaker(name = "CircuitBreakerApi", fallbackMethod = "fallbackGetAllCustomers")
	@TimeLimiter(name = "CircuitBreakerApi")
	@Override
	public Mono<ResponseEntity<Flux<Customer>>> getAllCustomers(final ServerWebExchange exchange) {
		log.info("Getting all customers");
		Flux<Customer> customerFlux = customerService.getAllCustomers()
			.map(entity -> {
				// Mapeo de la entidad (BD) al modelo (API)
				Customer model = new Customer();
				model.setId(entity.getId());
				model.setCustomerType(CustomerType.fromValue(entity.getCustomerType().name()));
				model.setDocumentType(DocumentTypeEnum.valueOf(entity.getDocumentType().name()));
				model.setDocumentNumber(entity.getDocumentNumber());
				model.setName(entity.getName());
				model.setCompanyName(entity.getCompanyName());
				model.setEmail(entity.getEmail());
				model.setStatus(StatusEnum.valueOf(entity.getStatus().name()));
				return model;
			});
		return Mono.just(ResponseEntity.ok(customerFlux));
	}

	public Mono<ResponseEntity<Flux<Customer>>> fallbackGetAllCustomers(ServerWebExchange exchange, Throwable t) {
		log.error("Fallback for getAllCustomers: {}", t.getMessage());
		return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build());
	}

	
	@CircuitBreaker(name = "CircuitBreakerApi", fallbackMethod = "fallbackUpdateCustomer")
	@TimeLimiter(name = "CircuitBreakerApi")
	@Override
	public Mono<ResponseEntity<Customer>> updateCustomer(@Valid Mono<Customer> customerMono, final ServerWebExchange exchange) {
		return customerMono.flatMap(customer -> {
			log.info("Updating customer: {}", customer);
			// Mapeo del modelo (API) a la entidad (BD)
			CustomerEntity customerEntity = new CustomerEntity();
			customerEntity.setId(customer.getId());
			customerEntity.setCustomerType(com.example.entity.enums.CustomerType.valueOf(customer.getCustomerType().getValue()));
			customerEntity.setDocumentType(com.example.entity.enums.DocumentType.valueOf(customer.getDocumentType().getValue()));
			customerEntity.setDocumentNumber(customer.getDocumentNumber());
			customerEntity.setName(customer.getName());
			customerEntity.setCompanyName(customer.getCompanyName());
			customerEntity.setEmail(customer.getEmail());
			customerEntity.setStatus(com.example.entity.enums.Status.valueOf(customer.getStatus().getValue()));

			return customerService.updateCustomer(customerEntity)
				.map(updatedEntity -> {
					// Mapeo de la entidad (BD) al modelo (API) para la respuesta
					customer.setId(updatedEntity.getId()); // Asegurarse de que el ID esté en la respuesta
					return ResponseEntity.ok(customer);
				})
				.switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
		});
	}

	public Mono<ResponseEntity<Customer>> fallbackUpdateCustomer(Mono<Customer> customerMono, Throwable t, final ServerWebExchange exchange) {
		log.error("Fallback for updateCustomer: {}", t.getMessage());
		return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build());
	}


	@CircuitBreaker(name = "CircuitBreakerApi", fallbackMethod = "fallbackDeleteCustomer")
	@TimeLimiter(name = "CircuitBreakerApi")
	@Override
	public Mono<ResponseEntity<Void>> deleteCustomer(@PathVariable String id, final ServerWebExchange exchange) {
		log.info("Deleting customer with id: {}", id);
		return customerService.deleteCustomer(id)
				.flatMap(deleted -> {
					if (deleted) {
						return Mono.just(ResponseEntity.noContent().<Void>build());
					} else {
						return Mono.just(ResponseEntity.notFound().<Void>build());
					}
				});
	}

	public Mono<ResponseEntity<Void>> fallbackDeleteCustomer(String id, Throwable t, final ServerWebExchange exchange) {
		log.error("Fallback for deleteCustomer with id {}: {}", id, t.getMessage());
		return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build());
	}
}
