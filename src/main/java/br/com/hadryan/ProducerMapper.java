package br.com.hadryan;

import br.com.hadryan.domain.Producer;
import br.com.hadryan.request.ProducerPostRequest;
import br.com.hadryan.response.ProducerPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProducerMapper {
	
	ProducerMapper INSTANCE = Mappers.getMapper(ProducerMapper.class);

	@Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
	@Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(100_000))")
	Producer toProducer(ProducerPostRequest request);
	
	ProducerPostResponse toResponse(Producer producer);
	
}
