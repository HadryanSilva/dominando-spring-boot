package br.com.hadryan.controller;

import br.com.hadryan.exception.NotFoundException;
import br.com.hadryan.mapper.ProducerMapper;
import br.com.hadryan.request.ProducerPostRequest;
import br.com.hadryan.request.ProducerPutRequest;
import br.com.hadryan.response.ProducerGetResponse;
import br.com.hadryan.response.ProducerPostResponse;
import br.com.hadryan.service.ProducerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequestMapping(path = {"v1/producers", "v1/producers/"})
@RequiredArgsConstructor
public class ProducerController {

	private final ProducerService service;
	private final ProducerMapper mapper;
	
	@GetMapping
	public ResponseEntity<List<ProducerGetResponse>> findAll(@RequestParam(required = false) String name) {
		log.info("Request recevied to list all producers, params '{}'", name);
		var producers = mapper.toProducersGetResponse(service.findAll(name));
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
				.orElseThrow(() -> new NotFoundException("Producer not found"));
		var response = mapper.toGetResponse(producer);
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProducerPostResponse> save(@RequestBody @Valid ProducerPostRequest request) {
		var producer = mapper.toProducer(request);
		service.save(producer);
		var response = mapper.toPostResponse(producer);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		log.info("Request recevied to delete producer with id '{}'", id);
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping
	public ResponseEntity<Void> update(@RequestBody @Valid ProducerPutRequest request) {
		log.info("Request recevied to update producer '{}'", request);
		var producerToUpdate = mapper.toProducer(request);
		service.update(producerToUpdate);
		return ResponseEntity.noContent().build();
	}

}
