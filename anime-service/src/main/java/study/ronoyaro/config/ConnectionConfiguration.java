package study.ronoyaro.config;

import external.dependecy.Connection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration //indico que ele é um componente a ser escaneado
public class ConnectionConfiguration {
    @Value("${database.url}")
    private String url;
    @Value("${database.username}")
    private String username;
    @Value("${database.password}")
    private String password;

    //para retornar o objeto e ser enxegado como Bean
    @Bean
    @Profile("mysql")
    public Connection connectionMySQL() {
        return new Connection(url, username, password);
    }

    //para retornar o objeto e ser enxegado como Bean
    @Bean(name = "connectionMongoDB")
    @Profile("mongo")
    public Connection connectionMongoDB() {
        return new Connection(url, username, password);
    }
}
