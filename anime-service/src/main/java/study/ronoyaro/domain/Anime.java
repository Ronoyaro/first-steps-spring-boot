package study.ronoyaro.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Anime {
    private Long id;
    private String name;

    public static List<Anime> getAnimes() {
        var dbz = new Anime(1L, "Dragon Ball");
        var yuyuHakusho = new Anime(2L, "Yuyu Hakusho");
        return List.of(dbz, yuyuHakusho);
    }
}
