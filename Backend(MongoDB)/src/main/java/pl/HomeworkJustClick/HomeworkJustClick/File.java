package pl.HomeworkJustClick.HomeworkJustClick;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "files")
@Getter
@Setter
public class File {
    @Id
    private String id;

    private String name;

    private String format;

    private Binary file;

    public File(String name, String format) {
        this.name = name;
        this.format = format;
    }
}
