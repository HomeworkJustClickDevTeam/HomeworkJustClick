package pl.HomeworkJustClick.HomeworkJustClick.infrastructure.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostgresClientService {
    private final WebClient postgresWebClient;
    private final PostgresConfigProps postgresConfigProps;

    public Boolean checkToken(String token) {
        String uri = postgresConfigProps.getUrl() + "/auth/checkToken";
        uri += "?token=" + token;
        return postgresWebClient.post()
                .uri(uri)
                .headers(httpHeaders -> httpHeaders.addAll(createHttpHeaders()))
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorComplete()
                .block();
    }

    private HttpHeaders createHttpHeaders() {
        var headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}
