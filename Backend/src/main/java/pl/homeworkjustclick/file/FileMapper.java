package pl.homeworkjustclick.file;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMapper {
    FileResponseDto map(File file);
}
