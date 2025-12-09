package study.ronoyaro.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
public class ConsoleGetResponse {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
}
