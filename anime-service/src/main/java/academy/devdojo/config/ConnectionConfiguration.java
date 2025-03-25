package academy.devdojo.config;

import external.dependency.Connection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ConnectionConfiguration {

    @Bean
    public Connection connection() {
        return new Connection("localhost", "goku", "ninja");
    }

    @Bean(name = "connectionMySql")
    public Connection connectionMySql() {
        return new Connection("localhost", "gokuMySql", "ninja");
    }

    @Bean
    @Primary
    public Connection connectionMongo() {
        return new Connection("localhost", "gokuMongo", "ninja");
    }

}
