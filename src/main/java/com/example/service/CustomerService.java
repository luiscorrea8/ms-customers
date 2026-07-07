package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

import com.example.entity.Customer;
import com.example.entity.enums.CustomerType;
import com.example.repository.CustomerRepository;

import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
	CustomerRepository customerRepository;

	public Single<Customer> createCustomer(Customer customer) {
		return Single.fromCallable(() -> customerRepository.save(customer))
				.subscribeOn(Schedulers.io());
	}

	public Single<Customer> getCustomerById(String id) {
		return Single.fromCallable(() -> customerRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Customer not found with id: " + id)))
				.subscribeOn(Schedulers.io());
	}

	public Observable<Customer> getAllCustomers() {
		return Observable.defer(() -> Observable.fromIterable(customerRepository.findAll()))
				.subscribeOn(Schedulers.io());
	}


	public Single<CustomerType> customerType(String id) {
		return Single.fromCallable(() -> {
			if (!customerRepository.existsById(id)) {
				throw new RuntimeException("Customer not found with id: " + id);
			}
			Optional<Customer> customer = customerRepository.findById(id);
			CustomerType customerType = customer.get().getCustomerType();
			return customerType;
		}).subscribeOn(Schedulers.io());
	}

	public Single<Customer> updateCustomer(Customer customer) {
		return Single.fromCallable(() -> {
			if (!customerRepository.existsById(customer.getId())) {
				throw new RuntimeException("Customer not found with id: " + customer.getId());
			}
			return customerRepository.save(customer);
		}).subscribeOn(Schedulers.io());
	}

	public Single<String> deleteCustomer(String id) {
		return Single.fromCallable(() -> {
			customerRepository.deleteById(id);
			return "Customer has been deleted.";
		}).subscribeOn(Schedulers.io());
	}
}
