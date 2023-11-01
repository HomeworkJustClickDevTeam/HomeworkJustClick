package pl.HomeworkJustClick.Backend.evaluation;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EvaluationMapper {
    EvaluationResponseDto map(Evaluation evaluation);
}
