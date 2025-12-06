package study.ronoyaro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

@SpringBootApplication
@ComponentScan(basePackages = {"outside.controller", "study.ronoyaro"})
public class AnimeServiceApplication {

	public static void main(String[] args) {
        var run = SpringApplication.run(AnimeServiceApplication.class, args);
        Arrays.stream( run.getBeanDefinitionNames())
                .forEach(System.out::println);
    }

}
