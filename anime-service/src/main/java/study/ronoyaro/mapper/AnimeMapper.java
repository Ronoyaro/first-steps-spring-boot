package study.ronoyaro.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import study.ronoyaro.domain.Anime;
import study.ronoyaro.request.AnimePostRequest;
import study.ronoyaro.request.AnimePutRequest;
import study.ronoyaro.response.AnimeGetResponse;
import study.ronoyaro.response.AnimePostResponse;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimeMapper {

    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(1,200))")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Anime toAnime(AnimePostRequest animePostRequest);

    Anime toAnime(AnimePutRequest animePutRequest);

    AnimeGetResponse toAnimeResponse(Anime anime);

    AnimePostResponse toAnimePostResponse(Anime anime);

    List<AnimeGetResponse> toAnimeListResponses(List<Anime> animes);

}
