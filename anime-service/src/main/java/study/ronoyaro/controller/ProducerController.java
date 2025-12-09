package study.ronoyaro.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.ronoyaro.domain.Producer;
import study.ronoyaro.mapper.ProducerMapper;
import study.ronoyaro.request.ProducerPostRequest;
import study.ronoyaro.response.ProducerGetResponse;

import java.util.List;

@RestController
@RequestMapping("v1/producers")
@Slf4j
public class ProducerController {
    public static final ProducerMapper MAPPER = ProducerMapper.INSTANCE;

    @GetMapping
    public List<Producer> list(@RequestParam(required = false) String name) {
        if (name == null) return Producer.getProducers();
        return Producer.getProducers()
                .stream()
                .filter(p -> p.getName().equals(name))
                .toList();
    }

    @GetMapping("{id}")
    public Producer filterById(@PathVariable Long id) {
        return Producer.getProducers().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "x-api-key=1234")
    public ResponseEntity<ProducerGetResponse> save(@RequestBody ProducerPostRequest producerPostRequest) {

        var producer = MAPPER.toProducer(producerPostRequest);
        var response = MAPPER.toProducerResponse(producer);

        Producer.getProducers().add(producer);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

}
