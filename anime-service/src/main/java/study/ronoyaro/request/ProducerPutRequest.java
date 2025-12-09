package study.ronoyaro.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProducerPutRequest {
    private Long id;
    private String name;
}
