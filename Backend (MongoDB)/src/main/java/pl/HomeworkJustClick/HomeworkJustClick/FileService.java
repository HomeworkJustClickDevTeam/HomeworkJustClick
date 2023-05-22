package pl.HomeworkJustClick.HomeworkJustClick;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    FileRepository fileRepository;

    public FileResponse addFile(String title, String format, MultipartFile file) throws IOException {
        File _file = new File(title, format);
        _file.setFile(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
        _file = fileRepository.insert(_file);
        return FileResponse.builder().id(_file.getId()).name(_file.getName()).format(_file.getFormat()).build();
    }

    public Optional<File> getFile(String id) {
        return fileRepository.findById(id);
    }

    public Boolean deleteFile(String id) {
        if(fileRepository.existsById(id)) {
            fileRepository.deleteById(id);
            return true;
        } else  {
            return false;
        }
    }

}
