package br.com.hadryan;

import br.com.hadryan.domain.Anime;
import br.com.hadryan.request.AnimePostRequest;
import br.com.hadryan.response.AnimeGetResponse;
import br.com.hadryan.response.AnimePostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper
public interface AnimeMapper {
	
	AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);
	
	@Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(100_000))")
	Anime toAnime(AnimePostRequest request);
	AnimePostResponse toPostResponse(Anime anime);
	AnimeGetResponse toGetResponse(Anime anime);
	
	List<AnimeGetResponse> toAnimesGetResponse(List<Anime> animes);
	
}
