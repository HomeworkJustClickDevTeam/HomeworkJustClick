package pl.HomeworkJustClick.Backend.file;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMapper {
    FileResponseDto map(File file);
}
