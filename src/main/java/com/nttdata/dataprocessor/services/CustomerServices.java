package  com.nttdata.dataprocessor.services;

import com.nttdata.dataprocessor.domain.models.entity.Customer;

import java.util.List;

public interface CustomerServices {
  List<Customer> getAll();
  Customer findByDN(String documentNumber);
  Customer findByDNAndDT(String documentNumber, String documentType);
  Customer save(Customer Item);
  void delete(String documentNumber);
  Customer update(String documentNumber, Customer updatedCustomer);
}
