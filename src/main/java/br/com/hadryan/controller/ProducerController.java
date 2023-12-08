package br.com.hadryan.controller;

import br.com.hadryan.ProducerMapper;
import br.com.hadryan.domain.Producer;
import br.com.hadryan.request.ProducerPostRequest;
import br.com.hadryan.response.ProducerPostResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = {"v1/producers", "v1/producers/"})
public class ProducerController {
	
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProducerPostResponse> save(@RequestBody ProducerPostRequest request) {
		var mapper = ProducerMapper.INSTANCE;
		var producer = mapper.toProducer(request);
		var response = mapper.toResponse(producer);
		Producer.getProducers().add(producer);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
