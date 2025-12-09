package study.ronoyaro.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.ronoyaro.domain.ConsoleGamer;
import study.ronoyaro.mapper.ConsoleMapper;
import study.ronoyaro.request.ConsolePostRequest;
import study.ronoyaro.response.ConsoleGetResponse;
import study.ronoyaro.response.ConsolePostResponse;

import java.util.List;

@RestController
@RequestMapping("v1/consoles")
public class ConsoleController {
    public static final ConsoleMapper MAPPER = ConsoleMapper.INSTANCE;

    @GetMapping
    public ResponseEntity<List<ConsoleGetResponse>> list(@RequestParam(required = false) String name) {
        if (name == null) {
            var consoleResponse = ConsoleGamer.getConsoles()
                    .stream().map(MAPPER::toConsoleGetResponse)
                    .toList();
            return ResponseEntity.ok(consoleResponse);
        }
        var consolesResponse = ConsoleGamer.getConsoles().stream()
                .filter(c -> c.getName().equals(name))
                .map(MAPPER::toConsoleGetResponse)
                .toList();
        return ResponseEntity.ok(consolesResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<ConsoleGetResponse> findById(@PathVariable Long id) {
        var consoleGetResponse = ConsoleGamer.getConsoles().stream()
                .filter(c -> c.getId().equals(id))
                .map(MAPPER::toConsoleGetResponse)
                .findFirst()
                .orElse(null);
        return ResponseEntity.ok(consoleGetResponse);
    }

    @PostMapping
    public ResponseEntity<ConsolePostResponse> save(@RequestBody ConsolePostRequest consolePostRequest) {
        var console = MAPPER.toConsole(consolePostRequest);
        ConsolePostResponse consolePostResponse = MAPPER.toConsolePostResponse(console);

        ConsoleGamer.getConsoles()
                .add(console);

        return ResponseEntity.status(HttpStatus.CREATED).body(consolePostResponse);
    }
}
