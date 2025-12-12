package study.ronoyaro.config;

import external.dependecy.Connection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration //indico que ele é um componente a ser escaneado
public class ConnectionConfiguration {

    @Bean //para retornar o objeto e ser enxegado como Bean
    public Connection getConnectionMySQL() {
        return new Connection("localhost", "Ronoyaro", "9999");
    }

    @Bean(name = "connectionMongoDB") //para retornar o objeto e ser enxegado como Bean
    public Connection getConnectionMongoDB() {
        return new Connection("localhost", "Ronoyaro", "9999");
    }
}
