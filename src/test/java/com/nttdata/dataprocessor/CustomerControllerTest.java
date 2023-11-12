package com.nttdata.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.dataprocessor.controllers.CustomerController;
import com.nttdata.dataprocessor.controllers.api.dto.request.CustomerRequest;
import com.nttdata.dataprocessor.controllers.api.dto.response.CustomerResponse;
import com.nttdata.dataprocessor.domain.models.entity.Customer;
import com.nttdata.dataprocessor.services.CustomerServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@SpringJUnitWebConfig
@AutoConfigureMockMvc
public class CustomerControllerTest {

  @InjectMocks
  private CustomerController customerController;

  @Mock
  private CustomerServices customerService;

  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
  }

  @Test
  public void testGetAllCustomers() throws Exception {
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/customer/all")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

    String responseBody = result.getResponse().getContentAsString();
  }


  @Test
  public void testGetValidInput() throws Exception {
    String validDocumentType = "passport";
    String validDocumentNumber = "123456789";

    Customer validCustomer = new Customer(
            validDocumentNumber,
            validDocumentType,
            "John",
            "Doe",
            "Smith",
            "Johnson",
            "123-456-7890",
            "123 Main St",
            "City"
    );

    when(customerService.findByDNAndDT(validDocumentNumber, validDocumentType))
            .thenReturn(validCustomer);

    ResponseEntity<CustomerResponse> responseEntity = customerController.get(validDocumentType, validDocumentNumber);

  }

  @Test
  public void testGetCustomerByDocumentWithInvalidParameters() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/customer")
                    .param("documentType", "X")
                    .param("documentNumber", "abc")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
  }




  @Test
  public void testCreateCustomer() throws Exception {
    CustomerRequest customerRequest = new CustomerRequest();
    customerRequest.setDocumentNumber("23445310");
    customerRequest.setDocumentType("C");
    customerRequest.setFirstName("John");
    customerRequest.setMiddleName("Robert");
    customerRequest.setLastName("Doe");
    customerRequest.setSecondLastName("Smith");
    customerRequest.setPhoneNumber("123-456-7890");
    customerRequest.setAddress("123 Main St");
    customerRequest.setCityOfResidence("New York");

    Customer createdCustomer = new Customer("23445310", "C", "John", "Robert", "Doe", "Smith", "123-456-7890", "123 Main St", "New York");

    when(customerService.save(createdCustomer)).thenReturn(createdCustomer);

    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/customer")
                    .content(asJsonString(customerRequest))
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andReturn();

    String responseBody = result.getResponse().getContentAsString();
  }

  @Test
  public void testCreateCustomerWithInvalidParameters() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/customer")
                    .content("{}")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
  }

  private static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }



  @Test
  public void testEditCustomer() throws Exception {
    CustomerRequest customerRequest = new CustomerRequest();
    customerRequest.setDocumentType("C");
    customerRequest.setFirstName("John");

    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/customer/123456")
                    .content(asJsonString(customerRequest))
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

    String responseBody = result.getResponse().getContentAsString();
  }

  @Test
  public void testEditCustomerWithInvalidParameters() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.put("/customer/abc")
                    .content("{}")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
  }

  @Test
  public void testDeleteCustomer() throws Exception {
    String documentNumber = "12345678";

    doNothing().when(customerService).delete(documentNumber);

    mockMvc.perform(MockMvcRequestBuilders.delete("/customer/" + documentNumber)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
  }

  @Test
  public void testDeleteCustomerWithInvalidParameters() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/customer/abc")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
  }


}