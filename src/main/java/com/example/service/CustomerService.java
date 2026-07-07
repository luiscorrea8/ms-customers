package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.entity.CustomerEntity;
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

	public Mono<CustomerEntity> createCustomer(CustomerEntity customer) {
		return Mono.fromCallable(() -> customerRepository.save(customer))
				.subscribeOn(Schedulers.boundedElastic());
	}

	public Mono<CustomerEntity> getCustomerById(String id) {
		return Mono.fromCallable(() -> customerRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Customer not found with id: " + id)))
				.subscribeOn(Schedulers.boundedElastic());
	}

	public Flux<CustomerEntity> getAllCustomers() {
		return Flux.defer(() -> Flux.fromIterable(customerRepository.findAll()))
				.subscribeOn(Schedulers.boundedElastic());
	}


	public Mono<CustomerType> customerType(String id) {
		return Mono.fromCallable(() -> customerRepository.findById(id)
						.map(CustomerEntity::getCustomerType)
						.orElseThrow(() -> new RuntimeException("Customer not found with id: " + id)))
				.subscribeOn(Schedulers.boundedElastic());
	}

	public Mono<CustomerEntity> updateCustomer(CustomerEntity customer) {
		return Mono.fromCallable(() -> customerRepository.findById(customer.getId())
						.map(existingCustomer -> customerRepository.save(customer))
						.orElseThrow(() -> new RuntimeException("Customer not found with id: " + customer.getId())))
				.subscribeOn(Schedulers.boundedElastic());
	}

	public Mono<Boolean> deleteCustomer(String id) {
		return Mono.fromCallable(() -> {
			Optional<CustomerEntity> customerOpt = customerRepository.findById(id);
			if (customerOpt.isPresent()) {
				customerRepository.deleteById(id);
				return true;
			}
			return false;
		}).subscribeOn(Schedulers.boundedElastic());
	}
}
