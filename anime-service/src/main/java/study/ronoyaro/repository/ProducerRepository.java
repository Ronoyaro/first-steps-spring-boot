package study.ronoyaro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.ronoyaro.domain.Producer;

import java.util.List;

public interface ProducerRepository extends JpaRepository<Producer, Long> {
    List<Producer> findByNameIgnoreCase(String name);
}
