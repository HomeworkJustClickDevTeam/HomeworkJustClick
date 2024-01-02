package pl.homeworkjustclick.infrastructure.rest;

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
        var valid = postgresWebClient.post()
                .uri(uri)
                .headers(httpHeaders -> httpHeaders.addAll(createHttpHeaders(token)))
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorComplete()
                .block();
        return !Boolean.FALSE.equals(valid);
    }

    private HttpHeaders createHttpHeaders(String jwtToken) {
        var headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(jwtToken);
        return headers;
    }
}
