package br.com.hadryan.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnimePostResponse {
	
	private Long id;
	private String name;

}
