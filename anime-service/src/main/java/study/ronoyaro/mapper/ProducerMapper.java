package study.ronoyaro.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import study.ronoyaro.domain.Producer;
import study.ronoyaro.request.ProducerPostRequest;
import study.ronoyaro.response.ProducerGetResponse;

@Mapper
public interface ProducerMapper {
    ProducerMapper INSTANCE = Mappers.getMapper(ProducerMapper.class);

    /*Serve para eu pegar um source para um target*/
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(1, 200))")
    Producer toProducer(ProducerPostRequest postRequest);

    /*Transformando o Producer em ProducerResponse*/
    ProducerGetResponse toProducerResponse(Producer producer);

}
