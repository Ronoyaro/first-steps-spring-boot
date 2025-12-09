package study.ronoyaro.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
@Builder
@ToString
public class ConsoleGamer {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    @Getter
    private static List<ConsoleGamer> consoles = new ArrayList<>();

    static {
        var ps5 = ConsoleGamer.builder()
                .id(1L)
                .name("Playstation 5")
                .createdAt(LocalDateTime.now())
                .build();
        var xbox = ConsoleGamer.builder()
                .id(2L)
                .name("Xbox Series X")
                .createdAt(LocalDateTime.now())
                .build();
        var nintendoSwitch = ConsoleGamer.builder()
                .id(ThreadLocalRandom.current().nextLong(1, 150))
                .name("Nintendo Switch")
                .createdAt(LocalDateTime.now())
                .build();
        consoles.addAll(List.of(ps5, xbox, nintendoSwitch));
    }
}
