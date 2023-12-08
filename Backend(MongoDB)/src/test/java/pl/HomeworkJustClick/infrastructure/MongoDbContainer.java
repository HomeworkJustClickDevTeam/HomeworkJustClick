package pl.homeworkjustclick.infrastructure;

import jakarta.validation.constraints.NotNull;
import org.testcontainers.containers.GenericContainer;

public class MongoDbContainer extends GenericContainer<MongoDbContainer> {

    public static final int MONGODB_PORT = 27018;
    public static final String DEFAULT_IMAGE_AND_TAG = "mongo:4.0.10";

    public MongoDbContainer() {
        this(DEFAULT_IMAGE_AND_TAG);
    }

    public MongoDbContainer(@NotNull String image) {
        super(image);
        addExposedPort(MONGODB_PORT);
    }

    @NotNull
    public Integer getPort() {
        return getMappedPort(MONGODB_PORT);
    }
}
