package ru.practicum.statclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.statdto.RequestHitDto;

import java.util.Map;

@Service
public class StatClient extends BaseClient {
    private static final String HIT_API_PREFIX = "/hit";
    private static final String STAT_API_PREFIX = "/stats";

    @Autowired
    public StatClient(@Value("${stat-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> saveHit(RequestHitDto requestHitDto) {
        return post(HIT_API_PREFIX, requestHitDto);
    }

    public ResponseEntity<Object> getStat(String start,
                                          String end,
                                          String[] uris,
                                          Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique);
        return post(STAT_API_PREFIX, parameters);
    }

}
