package pl.homeworkjustclick.infrastructure.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MongoClientService {
    private final WebClient postgresWebClient;
    private final MongoConfigProps mongoConfigProps;

    public Boolean deleteFile(String token, String fileId) {
        String uri = mongoConfigProps.getUrl() + "/file/" + fileId;
        var valid = postgresWebClient.delete()
                .uri(uri)
                .headers(httpHeaders -> httpHeaders.addAll(createHttpHeaders(token)))
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorComplete()
                .block();
        return Boolean.TRUE.equals(valid);
    }

    private HttpHeaders createHttpHeaders(String jwtToken) {
        var headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(jwtToken);
        return headers;
    }
}
