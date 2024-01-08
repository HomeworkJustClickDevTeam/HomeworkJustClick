package pl.homeworkjustclick;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.homeworkjustclick.infrastructure.rest.MongoClientService;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseTestEntity {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected MongoClientService mongoClientService;

    public HttpHeaders createHttpHeaders() {
        var headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkYWR3YUB3ZGEuY29tIiwiaWF0IjoxNzA0NzI4Mjc1LCJleHAiOjI2NTA4MDgyNzV9.1jpkUaR90d8tS7xqFys8wS8MI-iF2Caz5chhrirJg5wrGYYwscRusTwVGWa_O_vRpQkZdC1HwHaKk8k82T2w0g");
        return headers;
    }
}
