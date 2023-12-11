package br.com.hadryan.request;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProducerPutRequest {
	
	private Long id;
	private String name;
	private LocalDateTime createdAt;
	
}
