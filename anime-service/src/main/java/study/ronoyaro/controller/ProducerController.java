package study.ronoyaro.controller;

import external.dependecy.Connection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.ronoyaro.domain.Producer;
import study.ronoyaro.mapper.ProducerMapper;
import study.ronoyaro.request.ProducerPostRequest;
import study.ronoyaro.request.ProducerPutRequest;
import study.ronoyaro.response.ProducerGetResponse;
import study.ronoyaro.response.ProducerPostResponse;
import study.ronoyaro.service.ProducerService;

import java.util.List;

@RestController
@RequestMapping("v1/producers")
@Slf4j
@RequiredArgsConstructor
public class ProducerController {
    private final ProducerMapper mapper;
    private final ProducerService service;
    @Qualifier(value = "connectionMongoDB")
    private final Connection connection;

    @GetMapping
    public ResponseEntity<List<ProducerGetResponse>> listAll(@RequestParam(required = false) String name) {
        log.debug("Request received to list all producers, param name '{}'", name);

        var producers = service.findAll(name);

        var producerListGetResponse = mapper.toProducerListGetResponse(producers);

        return ResponseEntity.ok(producerListGetResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProducerGetResponse> filterById(@PathVariable Long id) {
        log.debug("Request to find producer by id: '{}'", id);
        log.debug("'{}'", connection); //injetado e buscando

        var producer = service.findByIdOrThrowNotFound(id);

        var producerGetResponse = mapper.toProducerGetResponse(producer);

        return ResponseEntity.ok(producerGetResponse);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "x-api-key=1234")
    public ResponseEntity<ProducerPostResponse> save(@RequestBody ProducerPostRequest producerPostRequest) {

        log.debug("Request do save Producer '{}'", producerPostRequest.getName());

        var producer = mapper.toProducer(producerPostRequest);

        Producer producerSaved = service.save(producer);

        var response = mapper.toProducerPostResponse(producerSaved);


        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {

        log.debug("Request Producer to delete Producer by id '{}'", id);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody ProducerPutRequest producerPutRequest) {

        log.debug("Request to update Producer '{}'", producerPutRequest.getName());

        var producerToUpdate = mapper.toProducer(producerPutRequest);

        service.update(producerToUpdate);

        return ResponseEntity.noContent().build();
    }

}
