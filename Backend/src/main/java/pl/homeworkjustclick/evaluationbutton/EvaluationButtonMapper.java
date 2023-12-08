package pl.homeworkjustclick.evaluationbutton;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EvaluationButtonMapper {
    EvaluationButton map(EvaluationButtonDto evaluationButtonDto);

    EvaluationButtonDto map(EvaluationButton evaluationButton);
}
