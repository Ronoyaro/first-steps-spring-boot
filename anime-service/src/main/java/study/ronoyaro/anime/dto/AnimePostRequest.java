package study.ronoyaro.anime.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimePostRequest {
    @NotBlank(message = "The field 'name' is required")
    private String name;
}
