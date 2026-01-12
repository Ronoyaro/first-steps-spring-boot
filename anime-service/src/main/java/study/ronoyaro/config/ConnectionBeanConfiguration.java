package study.ronoyaro.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration //indico que ele é um component a ser escaneado
@RequiredArgsConstructor
@EnableConfigurationProperties(ConnectionConfigurationProperties.class)
public class ConnectionBeanConfiguration {
    private final ConnectionConfigurationProperties connectionProperties;

    //para retornar o objeto e ser enxegado como Bean
    @Bean
//    @Profile("mysql")
    public Connection connectionMySQL() {
        return new Connection(connectionProperties.url(), connectionProperties.username(), connectionProperties.password());
    }

    //para retornar o objeto e ser enxegado como Bean
    @Bean(name = "connectionMongoDB")
    @Profile("mongo")
    @Primary
    public Connection connectionMongoDB() {
        return new Connection(connectionProperties.url(), connectionProperties.username(), connectionProperties.password());
    }
}
