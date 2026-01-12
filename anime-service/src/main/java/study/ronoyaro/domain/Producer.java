package study.ronoyaro.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(of = "id")
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Producer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //    @JsonProperty("name") //Serve para serialização do Jackson quando a requisição pode vir com outro nome no atributo
    @Column(nullable = false)
    private String name;
    private LocalDateTime createdAt;
}
