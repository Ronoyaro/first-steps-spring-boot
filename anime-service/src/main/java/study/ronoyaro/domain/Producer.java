package study.ronoyaro.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Producer {
    @EqualsAndHashCode.Include
    private Long id;
    @JsonProperty("name") //Serve para serialização do Jackson quando a requisição pode vir com outro nome no atributo
    private String name;
    private LocalDateTime createdAt;
}
