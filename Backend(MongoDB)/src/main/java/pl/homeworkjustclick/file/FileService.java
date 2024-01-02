package pl.homeworkjustclick.file;

import lombok.RequiredArgsConstructor;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.homeworkjustclick.infrastructure.exception.FileNotFoundException;
import pl.homeworkjustclick.infrastructure.rest.PostgresClientService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final PostgresClientService postgresClientService;

    public FileResponseDto addFile(String title, String format, MultipartFile file, String jwtToken) throws IOException {
//        checkToken(jwtToken);
        File _file = new File(title, format);
        _file.setFile(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
        _file = fileRepository.insert(_file);
        return FileResponseDto.builder().id(_file.getId()).name(_file.getName()).format(_file.getFormat()).build();
    }

    public List<FileResponseDto> addFileList(List<MultipartFile> fileList, String jwtToken) throws IOException {
//        checkToken(jwtToken);
        List<FileResponseDto> responseList = new ArrayList<>();
        fileList.forEach(file -> {
            String title = file.getOriginalFilename();
            String format = title.split("\\.")[1];
            File _file = new File(title, format);
            try {
                _file.setFile(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            _file = fileRepository.insert(_file);
            responseList.add(FileResponseDto.builder().id(_file.getId()).name(_file.getName()).format(_file.getFormat()).build());
        });
        return responseList;
    }

    public Optional<File> getFile(String id, String token) {
//        checkToken(token);
        return fileRepository.findById(id);
    }

    public void deleteFile(String id, String token) {
//        checkToken(token);
        var file = fileRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException("File not found"));
        fileRepository.delete(file);
    }

//    private void checkToken(String token) {
//        var valid = postgresClientService.checkToken(token);
//        if (!valid) {
//            throw new JwtNotValidException("Jwt token not valid!");
//        }
//    }
}
