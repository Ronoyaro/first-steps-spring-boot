package study.ronoyaro.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import study.ronoyaro.domain.Producer;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("v1/producers")
@Slf4j
public class ProducerController {

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
    public Producer save(@RequestBody Producer producer, @RequestHeader HttpHeaders headers) {
        log.info("Producer '{}' saved", producer.getName());
        log.info("All headers '{}' ", headers);
        producer.setId(ThreadLocalRandom.current().nextLong(1, 50));
        Producer.getProducers()
                .add(producer);
        return producer;
    }

}
