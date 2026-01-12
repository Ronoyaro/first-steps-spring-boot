package study.ronoyaro.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.ronoyaro.config.Connection;

@RestController
@RequestMapping("v1/connection")
@AllArgsConstructor
public class ConnectionController {

    private final Connection connection;

    @GetMapping
    public ResponseEntity<Connection> getConnection() {
        return ResponseEntity.ok(connection);
    }

}
