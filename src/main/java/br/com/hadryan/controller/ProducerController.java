package br.com.hadryan.controller;

import br.com.hadryan.ProducerMapper;
import br.com.hadryan.domain.Producer;
import br.com.hadryan.repository.ProducerHardCodedRepository;
import br.com.hadryan.request.ProducerPostRequest;
import br.com.hadryan.request.ProducerPutRequest;
import br.com.hadryan.response.ProducerGetResponse;
import br.com.hadryan.response.ProducerPostResponse;
import br.com.hadryan.service.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@Log4j2
@RequestMapping(path = {"v1/producers", "v1/producers/"})
@RequiredArgsConstructor
public class ProducerController {
	
	private static final ProducerMapper MAPPER = ProducerMapper.INSTANCE;

	private final ProducerService service;
	
	@GetMapping
	public ResponseEntity<List<ProducerGetResponse>> list(@RequestParam(required = false) String name) {
		log.info("Request recevied to list all producers, params '{}'", name);
		var producers = MAPPER.toProducersGetResponse(service.findAll(name));
		if (name == null) return ResponseEntity.ok(producers);
		var producerFound = producers
				.stream()
				.filter(producer -> producer.getName().equalsIgnoreCase(name))
				.toList();
		
		return ResponseEntity.ok(producerFound);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<ProducerGetResponse> findById(@PathVariable Long id) {
		log.info("Request recevied to find animes by id '{}'", id);
		var producer = service.findById(id).stream()
				.filter(n -> n.getId().equals(id))
				.findFirst()
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		var response = MAPPER.toGetResponse(producer);
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProducerPostResponse> save(@RequestBody ProducerPostRequest request) {
		var producer = MAPPER.toProducer(request);
		service.save(producer);
		var response = MAPPER.toPostResponse(producer);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		log.info("Request recevied to delete producer with id '{}'", id);
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping
	public ResponseEntity<Void> update(@RequestBody ProducerPutRequest request) {
		log.info("Request recevied to update producer '{}'", request);
		var producerToUpdate = MAPPER.toProducer(request);
		service.update(producerToUpdate);
		return ResponseEntity.noContent().build();
	}

}
