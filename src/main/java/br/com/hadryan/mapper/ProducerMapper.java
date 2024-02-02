package br.com.hadryan.mapper;

import br.com.hadryan.domain.Producer;
import br.com.hadryan.request.ProducerPostRequest;
import br.com.hadryan.request.ProducerPutRequest;
import br.com.hadryan.response.ProducerGetResponse;
import br.com.hadryan.response.ProducerPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProducerMapper {
	Producer toProducer(ProducerPostRequest request);
	Producer toProducer(ProducerPutRequest request);
	
	ProducerGetResponse toGetResponse(Producer producer);
	
	ProducerPostResponse toPostResponse(Producer producer);
	
	List<ProducerGetResponse> toProducersGetResponse(List<Producer> producers);
	
}
