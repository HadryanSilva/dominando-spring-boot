package br.com.hadryan.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ProducerPutRequest {
	
	private Long id;
	private String name;
	private LocalDateTime createdAt;
	
}
