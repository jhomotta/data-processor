package  com.nttdata.dataprocessor.controllers.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public abstract class BaseClassDtos<T extends RepresentationModel<? extends T>> extends RepresentationModel<T> {

  private Long id;

}
