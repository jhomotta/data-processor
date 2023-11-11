package  com.nttdata.dataprocessor.services.impl;

import com.nttdata.dataprocessor.domain.Singleton.CustomerSingleton;
import com.nttdata.dataprocessor.domain.models.entity.Customer;
import com.nttdata.dataprocessor.errors.Errors;
import com.nttdata.dataprocessor.exception.ApplicationException;
import com.nttdata.dataprocessor.services.CustomerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CustomerServiceImpl implements CustomerServices {

  private final CustomerSingleton customerSingleton;

  @Autowired
  public CustomerServiceImpl(CustomerSingleton customerSingleton) {
    this.customerSingleton = customerSingleton;
  }

  @Override
  public List<Customer> getAll() {
    return customerSingleton.getAllCustomers();
  }

  @Override
  public Customer findByDN(String documentNumber) {
    return customerSingleton.getCustomerByDocumentNumber(documentNumber).orElseThrow(
            () -> new ApplicationException(Errors.ITEM_NOT_FOUND, Map.of("info",   Customer.class.getName() + " with " + documentNumber + " not found")));
  }

  @Override
  public Customer findByDNAndDT(String documentNumber, String documentType) {
    return customerSingleton.getCustomerByDocumentNumberAndType(documentNumber, documentType).orElseThrow(
            () -> new ApplicationException(Errors.ITEM_NOT_FOUND, Map.of("info",   Customer.class.getName() + " with documentNumber: " + documentNumber + " and documentType: " + documentType + " not found")));
  }

  @Override
  public Customer save(Customer customer) {
    return customerSingleton.addCustomer(customer).orElseThrow(
            () -> new ApplicationException(Errors.ITEM_ALREADY_EXISTS, Map.of("info",   Customer.class.getName() + " with documentNumber: " + customer.getDocumentNumber() + " already exists")));
  }

  @Override
  public void delete(String documentNumber) {
    customerSingleton.deleteCustomer(documentNumber);
  }

  @Override
  public Customer update(String documentNumber, Customer updatedCustomer) {
    return customerSingleton.updateCustomer(documentNumber, updatedCustomer).orElseThrow(
            () -> new ApplicationException(Errors.ITEM_NOT_FOUND, Map.of("info",   Customer.class.getName() + " with " + documentNumber + " not found")));
  }

}
