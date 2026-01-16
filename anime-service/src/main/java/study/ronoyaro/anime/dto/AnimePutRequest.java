package study.ronoyaro.anime.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AnimePutRequest {
    private Long id;
    @NotBlank(message = "The field 'name' is required")
    private String name;
}
