package start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Properties;


@ComponentScan(basePackages = {"repository","services"})
@SpringBootApplication
public class StartRestServices {

    @Primary
    @Bean(name="props")
    public static Properties createProps() {
        Properties props = new Properties();
        try {
            InputStream file = new ClassPathResource("server.properties").getInputStream();
            props.load(file);
            props.list(System.out);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(StartRestServices.class);
        app.setDefaultProperties(Collections.singletonMap("server.port","8080"));
        app.run(args);
    }
}

