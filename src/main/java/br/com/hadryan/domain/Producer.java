package br.com.hadryan.domain;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Producer {
	
	private Long id;
	private String name;
	private LocalDateTime createdAt;
	
	private static List<Producer> producers = new ArrayList<>();
	
	static {
		var toei = Producer.builder().id(1L).name("Toei Animation").createdAt(LocalDateTime.now()).build();
		var whatever = Producer.builder().id(2L).name("Whatever").createdAt(LocalDateTime.now()).build();
		producers.addAll(List.of(toei, whatever));
	}
	
	public static List<Producer> getProducers() {
		return producers;
	}
	
}
