package study.ronoyaro.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import study.ronoyaro.domain.Producer;
import study.ronoyaro.repository.ProducerHardCodedRepository;

import java.util.List;

public class ProducerService {
    private ProducerHardCodedRepository repository;

    public ProducerService() {
        this.repository = new ProducerHardCodedRepository();
    }

    public List<Producer> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByName(name);
    }

    public Producer findByIdOrThrowNotFound(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producer not found"));
    }

    public Producer save(Producer producer) {
        return repository.save(producer);
    }

    public void delete(Long id) {
        var producer = findByIdOrThrowNotFound(id);
        repository.delete(producer);
    }

    public void update(Producer producerToUpdate) {
        Producer producer = findByIdOrThrowNotFound(producerToUpdate.getId());
        producerToUpdate.setCreatedAt(producer.getCreatedAt());
        repository.update(producerToUpdate);
    }

}
