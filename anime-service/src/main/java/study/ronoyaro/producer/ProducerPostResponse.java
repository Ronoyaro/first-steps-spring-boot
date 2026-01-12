package study.ronoyaro.producer;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
public class ProducerPostResponse {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
}
