package com.nttdata.dataprocessor.domain.Singleton;

import com.nttdata.dataprocessor.domain.models.entity.Customer;
import com.nttdata.dataprocessor.errors.Errors;
import com.nttdata.dataprocessor.exception.ApplicationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Component
public class CustomerSingleton {

  private List<Customer> customerList = new ArrayList<>();
  private static CustomerSingleton instance;

  private CustomerSingleton() {

    Customer customer1 = new Customer();
    customer1.setDocumentNumber("23445322");
    customer1.setDocumentType("C");
    customer1.setFirstName("Juan");
    customer1.setMiddleName("Carlos");
    customer1.setLastName("Rodriguez");
    customer1.setSecondLastName("Gomez");
    customer1.setPhoneNumber("3001234567");
    customer1.setAddress("Calle 123");
    customer1.setCityOfResidence("Bogotá");
    customerList.add(customer1);

    Customer customer2 = new Customer();
    customer2.setDocumentNumber("23445323");
    customer2.setDocumentType("P");
    customer2.setFirstName("Maria");
    customer2.setMiddleName("Jose");
    customer2.setLastName("Lopez");
    customer2.setSecondLastName("Diaz");
    customer2.setPhoneNumber("3209876543");
    customer2.setAddress("Avenida 45");
    customer2.setCityOfResidence("Medellín");
    customerList.add(customer2);
  }

  public static CustomerSingleton getInstance() {
    if (instance == null) {
      instance = new CustomerSingleton();
    }
    return instance;
  }

  // CRUD operations

  public List<Customer> getAllCustomers() {
    return customerList;
  }

  public Optional<Customer> getCustomerByDocumentNumber(String documentNumber) {
    return customerList.stream()
            .filter(customer -> customer.getDocumentNumber().equals(documentNumber))
            .findFirst();
  }

  // Method to search by documentNumber and documentType
  public Optional<Customer> getCustomerByDocumentNumberAndType(String documentNumber, String documentType) {
    return customerList.stream()
            .filter(customer -> customer.getDocumentNumber().equals(documentNumber) && customer.getDocumentType().equals(documentType))
            .findFirst();
  }

  public Optional<Customer> addCustomer(Customer customer) {
    // Check if the documentNumber is numeric and doesn't exist already
    if (isNumeric(customer.getDocumentNumber()) && !customerList.stream().anyMatch(c -> c.getDocumentNumber().equals(customer.getDocumentNumber()))) {
      customerList.add(customer);
      return this.getCustomerByDocumentNumber(customer.getDocumentNumber());
    }
    throw new ApplicationException(Errors.ITEM_ALREADY_EXISTS, Map.of("info",   Customer.class.getName() + " with DocumentNumber: " + customer.getDocumentNumber() + " already exists"));
  }

  public Optional<Customer> updateCustomer(String documentNumber, Customer updatedCustomer) {
    Optional<Customer> existingCustomer = getCustomerByDocumentNumber(documentNumber);
    if (existingCustomer.isPresent()) {
      int index = customerList.indexOf(existingCustomer.get());
      customerList.set(index, updatedCustomer);
      return this.getCustomerByDocumentNumber(documentNumber);
    }
    throw  new ApplicationException(Errors.ITEM_NOT_FOUND, Map.of("info",   Customer.class.getName() + " with documentNumber: " + documentNumber + " not found"));
  }

  public void deleteCustomer(String documentNumber) {
    Optional<Customer> existingCustomer = getCustomerByDocumentNumber(documentNumber);
    if (existingCustomer.isPresent()) {
      customerList.removeIf(customer -> customer.getDocumentNumber().equals(documentNumber));
    } else {
      throw new ApplicationException(Errors.ITEM_NOT_FOUND, Map.of("info", Customer.class.getName() + " with documentNumber: " + documentNumber + " not found"));
    }
  }

  // Method to check if a string is numeric
  private boolean isNumeric(String str) {
    return str != null && str.matches("[0-9]+");
  }

}
