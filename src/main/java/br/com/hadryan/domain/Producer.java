package br.com.hadryan.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class Producer {
	
	private Long id;
	private String name;
	private LocalDateTime createdAt;
	
	private static List<Producer> producers = new ArrayList<>();
	
	static {
		var toei = Producer.builder().id(ThreadLocalRandom.current().nextLong(100_000)).name("Toei Animation").createdAt(LocalDateTime.now()).build();
		var whatever = Producer.builder().id(ThreadLocalRandom.current().nextLong(100_000)).name("Whatever").createdAt(LocalDateTime.now()).build();
		producers.addAll(List.of(toei, whatever));
	}
	
	public static List<Producer> getProducers() {
		return producers;
	}
	
}
