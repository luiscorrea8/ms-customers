package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.entity.Customer;
import com.example.entity.enums.CustomerType;
import com.example.repository.CustomerRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
	CustomerRepository customerRepository;

	public Mono<Customer> createCustomer(Customer customer) {
		return Mono.fromCallable(() -> customerRepository.save(customer))
				.subscribeOn(Schedulers.boundedElastic());
	}

	public Mono<Customer> getCustomerById(String id) {
		return Mono.fromCallable(() -> customerRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Customer not found with id: " + id)))
				.subscribeOn(Schedulers.boundedElastic());
	}

	public Flux<Customer> getAllCustomers() {
		return Flux.defer(() -> Flux.fromIterable(customerRepository.findAll()))
				.subscribeOn(Schedulers.boundedElastic());
	}


	public Mono<CustomerType> customerType(String id) {
		return Mono.fromCallable(() -> customerRepository.findById(id)
						.map(Customer::getCustomerType)
						.orElseThrow(() -> new RuntimeException("Customer not found with id: " + id)))
				.subscribeOn(Schedulers.boundedElastic());
	}

	public Mono<Customer> updateCustomer(Customer customer) {
		return Mono.fromCallable(() -> customerRepository.findById(customer.getId())
						.map(existingCustomer -> customerRepository.save(customer))
						.orElseThrow(() -> new RuntimeException("Customer not found with id: " + customer.getId())))
				.subscribeOn(Schedulers.boundedElastic());
	}

	public Mono<Void> deleteCustomer(String id) {		
		return Mono.fromRunnable(() -> {			
			customerRepository.findById(id).ifPresentOrElse(
				customer -> customerRepository.deleteById(id),
				() -> { throw new RuntimeException("Customer not found with id: " + id); }
			);
		})
		.subscribeOn(Schedulers.boundedElastic())
		.then();
	}
}
