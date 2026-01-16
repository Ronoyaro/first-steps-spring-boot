package study.ronoyaro.anime.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import study.ronoyaro.anime.domain.Anime;
import study.ronoyaro.anime.dto.AnimeGetResponse;
import study.ronoyaro.anime.dto.AnimePostRequest;
import study.ronoyaro.anime.dto.AnimePostResponse;
import study.ronoyaro.anime.dto.AnimePutRequest;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimeMapper {

    Anime toAnime(AnimePostRequest animePostRequest);

    Anime toAnime(AnimePutRequest animePutRequest);

    AnimeGetResponse toAnimeResponse(Anime anime);

    AnimePostResponse toAnimePostResponse(Anime anime);

    List<AnimeGetResponse> toAnimeListResponses(List<Anime> animes);

}
