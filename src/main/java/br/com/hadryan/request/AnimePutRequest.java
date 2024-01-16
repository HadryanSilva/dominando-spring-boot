package br.com.hadryan.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimePutRequest {
	
	private Long id;
	@NotBlank(message = "the field 'name' is required")
	private String name;
	
}
