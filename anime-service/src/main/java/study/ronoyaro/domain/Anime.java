package study.ronoyaro.domain;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Anime {
    @EqualsAndHashCode.Include //Estou dizendo que o ID é o mesmo que tá sendo comparado
    private Long id;
    private String name;
    private LocalDateTime createdAt;
}
