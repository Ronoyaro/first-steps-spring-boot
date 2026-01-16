package study.ronoyaro.producer.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
public class ProducerGetResponse {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
}
