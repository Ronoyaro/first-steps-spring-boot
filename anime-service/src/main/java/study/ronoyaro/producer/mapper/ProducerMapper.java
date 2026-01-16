package study.ronoyaro.producer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import study.ronoyaro.producer.domain.Producer;
import study.ronoyaro.producer.dto.ProducerGetResponse;
import study.ronoyaro.producer.dto.ProducerPostRequest;
import study.ronoyaro.producer.dto.ProducerPostResponse;
import study.ronoyaro.producer.dto.ProducerPutRequest;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProducerMapper {

    /*Serve para eu pegar um source para um target*/
    Producer toProducer(ProducerPostRequest postRequest);

    Producer toProducer(ProducerPutRequest putRequest);

    /*Transformando o Producer em ProducerResponse*/
    ProducerGetResponse toProducerGetResponse(Producer producer);

    ProducerPostResponse toProducerPostResponse(Producer producer);

    /*Transforma uma lista de Producers para uma lista de ProducersGetResponse*/
    List<ProducerGetResponse> toProducerListGetResponse(List<Producer> producers);

}
