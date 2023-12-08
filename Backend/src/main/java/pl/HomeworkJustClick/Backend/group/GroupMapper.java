package pl.HomeworkJustClick.Backend.group;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    GroupResponseDto map(Group group);
}
