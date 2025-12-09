package study.ronoyaro.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import study.ronoyaro.domain.Producer;
import study.ronoyaro.mapper.ProducerMapper;
import study.ronoyaro.request.ProducerPostRequest;
import study.ronoyaro.request.ProducerPutRequest;
import study.ronoyaro.response.ProducerPostResponse;

import java.util.List;

@RestController
@RequestMapping("v1/producers")
@Slf4j
public class ProducerController {
    public static final ProducerMapper MAPPER = ProducerMapper.INSTANCE;

    @GetMapping
    public ResponseEntity<List<Producer>> list(@RequestParam(required = false) String name) {
        if (name == null) return ResponseEntity.ok(Producer.getProducers());
        var producers = Producer.getProducers()
                .stream()
                .filter(p -> p.getName().equals(name))
                .toList();
        return ResponseEntity.ok(producers);
    }

    @GetMapping("{id}")
    public ResponseEntity<Producer> filterById(@PathVariable Long id) {

        var producer = Producer.getProducers().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producer not found"));

        return ResponseEntity.ok(producer);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "x-api-key=1234")
    public ResponseEntity<ProducerPostResponse> save(@RequestBody ProducerPostRequest producerPostRequest) {

        log.debug("Request do save Producer '{}'", producerPostRequest.getName());

        var producer = MAPPER.toProducer(producerPostRequest);

        var response = MAPPER.toProducerPostResponse(producer);


        Producer.getProducers().add(producer);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {

        log.debug("Request Producer to delete Producer by id '{}'", id);

        var producerToDelete = Producer.getProducers().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Producer.getProducers().remove(producerToDelete);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody ProducerPutRequest producerPutRequest) {

        log.debug("Request to update Producer '{}'", producerPutRequest.getName());

        var producerToDelete = Producer.getProducers()
                .stream()
                .filter(p -> p.getId().equals(producerPutRequest.getId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producer not found"));

        var producerToUpdate = MAPPER.toProducer(producerPutRequest, producerToDelete.getCreatedAt());

        Producer.getProducers().remove(producerToDelete);

        Producer.getProducers().add(producerToUpdate);

        return ResponseEntity.noContent().build();
    }

}
