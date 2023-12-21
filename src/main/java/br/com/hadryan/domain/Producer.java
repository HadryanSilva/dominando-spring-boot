package br.com.hadryan.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class Producer {
	
	private Long id;
	private String name;
	private LocalDateTime createdAt;
	
}
