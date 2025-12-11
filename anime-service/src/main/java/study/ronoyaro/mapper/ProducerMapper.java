package study.ronoyaro.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import study.ronoyaro.domain.Producer;
import study.ronoyaro.request.ProducerPostRequest;
import study.ronoyaro.request.ProducerPutRequest;
import study.ronoyaro.response.ProducerGetResponse;
import study.ronoyaro.response.ProducerPostResponse;

import java.util.List;

@Mapper
public interface ProducerMapper {
    ProducerMapper INSTANCE = Mappers.getMapper(ProducerMapper.class);

    /*Serve para eu pegar um source para um target*/
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(1, 200))")
    Producer toProducer(ProducerPostRequest postRequest);

    Producer toProducer(ProducerPutRequest putRequest);

    /*Transformando o Producer em ProducerResponse*/
    ProducerGetResponse toProducerGetResponse(Producer producer);

    ProducerPostResponse toProducerPostResponse(Producer producer);

    /*Transforma uma lista de Producers para uma lista de ProducersGetResponse*/
    List<ProducerGetResponse> toProducerListGetResponse(List<Producer> producers);

}
