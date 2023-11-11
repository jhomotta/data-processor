package  com.nttdata.dataprocessor.controllers.api.dto.response;

import com.nttdata.dataprocessor.controllers.api.dto.BaseClassDtos;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {

  @Schema(description = "Document number is required", example = "23445322" ,nullable = false )
  private String documentNumber;

  @Schema(description = "Document type must be 'C' or 'P'", example = "C" ,nullable = false )
  private String documentType;

  @Schema(description = "First name of the person", example = "John", nullable = false)
  private String firstName;

  @Schema(description = "Middle name of the person", example = "Robert", nullable = true)
  private String middleName;

  @Schema(description = "Last name of the person", example = "Doe", nullable = false)
  private String lastName;

  @Schema(description = "Second last name of the person", example = "Smith", nullable = true)
  private String secondLastName;

  @Schema(description = "Phone number of the person", example = "123-456-7890", nullable = true)
  private String phoneNumber;

  @Schema(description = "Address of the person", example = "123 Main St, City", nullable = true)
  private String address;

  @Schema(description = "City of residence of the person", example = "New York", nullable = true)
  private String cityOfResidence;
}
