package br.com.hadryan;

import br.com.hadryan.domain.Producer;
import br.com.hadryan.request.ProducerPostRequest;
import br.com.hadryan.request.ProducerPutRequest;
import br.com.hadryan.response.ProducerGetResponse;
import br.com.hadryan.response.ProducerPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProducerMapper {
	
	ProducerMapper INSTANCE = Mappers.getMapper(ProducerMapper.class);

	@Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
	@Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(100_000))")
	Producer toProducer(ProducerPostRequest request);
	Producer toProducer(ProducerPutRequest request);
	
	ProducerGetResponse toGetResponse(Producer producer);
	
	ProducerPostResponse toPostResponse(Producer producer);
	
	List<ProducerGetResponse> toProducersGetResponse(List<Producer> producers);
	
}
