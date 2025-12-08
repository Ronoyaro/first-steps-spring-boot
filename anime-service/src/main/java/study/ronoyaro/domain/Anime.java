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
    private static List<Anime> animeList = new ArrayList<>();

    static {
        var dbz = new Anime(1L, "Dragon Ball");
        var yuyuHakusho = new Anime(2L, "Yuyu Hakusho");
        animeList.addAll(List.of(dbz, yuyuHakusho));
    }

    public static List<Anime> getAnimes() {
        return animeList;
    }

    @Override
    public String toString() {
        return "Anime{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
