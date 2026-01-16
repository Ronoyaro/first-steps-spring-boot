package study.ronoyaro.anime.domain;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
public class Anime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}


