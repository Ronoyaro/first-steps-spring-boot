package study.ronoyaro.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class Anime {
    private Long id;
    private String name;
    @Getter
    private static List<Anime> animes = new ArrayList<>();

    static {
        var dbz = new Anime(1L, "Dragon Ball");
        var yuyuHakusho = new Anime(2L, "Yuyu Hakusho");
        animes.addAll(List.of(dbz, yuyuHakusho));
    }

    @Override
    public String toString() {
        return "Anime{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
