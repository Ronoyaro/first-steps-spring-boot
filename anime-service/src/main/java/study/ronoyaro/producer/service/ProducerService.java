package study.ronoyaro.producer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.ronoyaro.exception.NotFoundException;
import study.ronoyaro.producer.domain.Producer;
import study.ronoyaro.producer.repository.ProducerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProducerService {
    private final ProducerRepository repository;

    public List<Producer> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByNameIgnoreCase(name);
    }

    public Producer findByIdOrThrowNotFound(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producer not found"));
    }

    public Producer save(Producer producer) {
        return repository.save(producer);
    }

    public void delete(Long id) {
        var producer = findByIdOrThrowNotFound(id);
        repository.delete(producer);
    }

    public void update(Producer producerToUpdate) {
        assertProducerExists(producerToUpdate.getId());
        repository.save(producerToUpdate);
    }

    public void assertProducerExists(Long id) {
        findByIdOrThrowNotFound(id);
    }

}
