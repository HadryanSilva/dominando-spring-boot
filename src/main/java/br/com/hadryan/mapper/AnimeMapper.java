package br.com.hadryan.mapper;

import br.com.hadryan.domain.Anime;
import br.com.hadryan.request.AnimePostRequest;
import br.com.hadryan.request.AnimePutRequest;
import br.com.hadryan.response.AnimeGetResponse;
import br.com.hadryan.response.AnimePostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimeMapper {
	Anime toAnime(AnimePostRequest request);
	Anime toAnime(AnimePutRequest request);
	AnimePostResponse toPostResponse(Anime anime);
	AnimeGetResponse toGetResponse(Anime anime);
	List<AnimeGetResponse> toGetResponses(List<Anime> animes);
	
}
