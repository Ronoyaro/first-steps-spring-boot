package study.ronoyaro.anime.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class AnimeGetResponse {
    private Long id;
    private String name;
}

