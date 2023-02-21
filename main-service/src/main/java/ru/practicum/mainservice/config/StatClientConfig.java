package ru.practicum.mainservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.statclient.StatClient;

@Configuration
public class StatClientConfig {
    @Value("${stats-service.url}")
    private String url;

    @Bean
    StatClient statClient() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        return new StatClient(url, builder);
    }
}
