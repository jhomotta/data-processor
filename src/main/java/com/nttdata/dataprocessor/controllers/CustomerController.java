package com.nttdata.dataprocessor.controllers;

import com.nttdata.dataprocessor.controllers.api.dto.request.CustomerRequest;
import com.nttdata.dataprocessor.controllers.api.dto.response.CustomerResponse;
import com.nttdata.dataprocessor.domain.models.entity.Customer;
import com.nttdata.dataprocessor.services.CustomerServices;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping(value = "/customer")
@Slf4j
public class CustomerController {
  @Autowired
  private  CustomerServices service;


  @GetMapping("/all")
  public ResponseEntity<List<CustomerResponse>> getAll() throws Exception {
    final HttpHeaders headers = new HttpHeaders();

    List<Customer> customersDB = service.getAll();
    List<CustomerResponse> customerResponses = this.convertToDtoList(customersDB);

    return new ResponseEntity<>(customerResponses, headers, HttpStatus.OK);
  }


  @GetMapping()
  public ResponseEntity<CustomerResponse> get(@RequestParam("documentType") String documentType,
                                              @Pattern(regexp = "^[0-9]+$", message = "documentNumber must contain only numbers")
                                              @RequestParam("documentNumber") String documentNumber) throws Exception {

    if (!isValidDocumentType(documentType)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    if (!isValidDocumentNumber(documentNumber)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    final HttpHeaders headers = new HttpHeaders();
    Customer customerDB;
    //if(documentNumber.equals("23445322")) {
      //customerDB = service.findByDN(documentNumber);
    //} else {
    customerDB = service.findByDNAndDT(documentNumber, documentType);
    //}

    CustomerResponse customerResponse = this.convertToDto(customerDB);
    return new ResponseEntity<>(customerResponse, headers, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<CustomerResponse> creat(@RequestBody final CustomerRequest customerRequest) {
    final HttpHeaders headers = new HttpHeaders();

    if (!isValidDocumentType(customerRequest.getDocumentType())) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    if (!isValidDocumentNumber(customerRequest.getDocumentNumber())) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    Customer customerDB = this.convertToModel(customerRequest);
    service.save(customerDB);
    CustomerResponse customerResponse = this.convertToDto(customerDB);
    return new ResponseEntity<>(customerResponse, headers, HttpStatus.CREATED);
  }

  @PutMapping("/{documentNumber}")
  public ResponseEntity<CustomerResponse> edit(@PathVariable
                                                 @Pattern(regexp = "^[0-9]+$", message = "documentNumber must contain only numbers")
                                                 String documentNumber,
                                                 @RequestBody final CustomerRequest customerRequest) {

    if (!isValidDocumentType(customerRequest.getDocumentType())) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    if (!isValidDocumentNumber(documentNumber)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    final HttpHeaders headers = new HttpHeaders();
    customerRequest.setDocumentNumber(documentNumber);
    Customer customerDB = this.convertToModel(customerRequest);
    service.update(documentNumber, customerDB);
    CustomerResponse customerResponse = this.convertToDto(customerDB);
    return new ResponseEntity<>(customerResponse, headers, HttpStatus.OK);
  }

  @DeleteMapping("/{documentNumber}")
  public ResponseEntity<?> delete(@PathVariable
                                    @Pattern(regexp = "^[0-9]+$", message = "documentNumber must contain only numbers")
                                    String documentNumber) {

    if (!isValidDocumentNumber(documentNumber)) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    final HttpHeaders headers = new HttpHeaders();
    service.delete(documentNumber);
    return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
  }

  private List<CustomerResponse> convertToDtoList(List<Customer> customerList) {
    ModelMapper modelMapper = new ModelMapper();
    return customerList.stream()
            .map(customer -> modelMapper.map(customer, CustomerResponse.class))
            .collect(Collectors.toList());
  }

  private CustomerResponse convertToDto(Customer customer) {
    ModelMapper modelMapper = new ModelMapper();
    return modelMapper.map(customer, CustomerResponse.class);
  }

  private Customer convertToModel(CustomerRequest customerDto) {
    ModelMapper modelMapper = new ModelMapper();
    return modelMapper.map(customerDto, Customer.class);
  }

  private boolean isValidDocumentNumber(String str) {
    return (str != null && str.matches("[0-9]+"));
  }

  private boolean isValidDocumentType(String documentType) {
    return "C".equals(documentType) || "P".equals(documentType);
  }

}
