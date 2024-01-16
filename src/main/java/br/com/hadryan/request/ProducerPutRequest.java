package br.com.hadryan.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ProducerPutRequest {
	
	private Long id;

	@NotBlank(message = "the field 'name' is required")
	private String name;
	private LocalDateTime createdAt;
	
}
