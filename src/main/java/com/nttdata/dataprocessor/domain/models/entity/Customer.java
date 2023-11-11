package com.nttdata.dataprocessor.domain.models.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Customer {
  private String documentNumber;
  private String documentType;
  private String firstName;
  private String middleName;
  private String lastName;
  private String secondLastName;
  private String phoneNumber;
  private String address;
  private String cityOfResidence;

}
