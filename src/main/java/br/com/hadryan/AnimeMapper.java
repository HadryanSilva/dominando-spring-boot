package br.com.hadryan;

import br.com.hadryan.domain.Anime;
import br.com.hadryan.request.AnimePostRequest;
import br.com.hadryan.request.AnimePutRequest;
import br.com.hadryan.response.AnimeGetResponse;
import br.com.hadryan.response.AnimePostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimeMapper {
	@Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(100_000))")
	Anime toAnime(AnimePostRequest request);
	Anime toAnime(AnimePutRequest request);
	AnimePostResponse toPostResponse(Anime anime);
	AnimeGetResponse toGetResponse(Anime anime);
	
	List<AnimeGetResponse> toAnimesGetResponse(List<Anime> animes);
	
}
