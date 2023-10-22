package pl.HomeworkJustClick.HomeworkJustClick.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.Network;
import pl.HomeworkJustClick.HomeworkJustClick.infrastructure.rest.PostgresClientService;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseTestEntity {
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected PostgresClientService postgresClientService;

    private static final MongoDBContainer mongoDBContainer;
    private static final Network network;

    static {
        network = Network.newNetwork();
        mongoDBContainer = new MongoDBContainer("mongo:4.2")
                .withNetwork(network);

        mongoDBContainer.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }
}
