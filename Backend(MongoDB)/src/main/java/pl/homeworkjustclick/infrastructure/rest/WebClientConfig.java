package pl.homeworkjustclick.infrastructure.rest;

import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
import org.apache.hc.core5.http.URIScheme;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.nio.ssl.BasicClientTlsStrategy;
import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.HttpComponentsClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {
    private final PostgresConfigProps postgresConfigProps;

    @Value("${server.ssl.key-store}")
    private Resource keystore;

    @Value("${server.ssl.key-store-password}")
    private String password;

    @Bean
    public WebClient postgresWebClient() throws IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        var sslContext = new SSLContextBuilder()
                .loadKeyMaterial(keystore.getURL(), password.toCharArray(), password.toCharArray())
                .build();

        var registry = RegistryBuilder.<TlsStrategy>create()
                .register(URIScheme.HTTPS.getId(), new BasicClientTlsStrategy(sslContext))
                .build();

        var httpClient = HttpAsyncClients.custom()
                .setConnectionManager(new PoolingAsyncClientConnectionManager(registry))
                .build();

        return WebClient.builder()
                .baseUrl(postgresConfigProps.getUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new HttpComponentsClientHttpConnector(httpClient))
                .build();
    }
}
