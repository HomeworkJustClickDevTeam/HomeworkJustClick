package pl.homeworkjustclick.evaluation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.homeworkjustclick.group.GroupMapper;
import pl.homeworkjustclick.solution.SolutionMapper;
import pl.homeworkjustclick.user.UserMapper;

import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
public class EvaluationMapper {
    private final UserMapper userMapper;
    private final SolutionMapper solutionMapper;
    private final GroupMapper groupMapper;

    public EvaluationResponseDto map(Evaluation evaluation) {
        return EvaluationResponseDto.builder()
                .id(evaluation.getId())
                .result(evaluation.getResult())
                .userId(evaluation.getUser().getId())
                .solutionId(evaluation.getSolution().getId())
                .groupId(evaluation.getGroup().getId())
                .creationDatetime(evaluation.getCreationDatetime())
                .lastModifiedDatetime(evaluation.getLastModifiedDatetime())
                .grade(evaluation.getGrade())
                .build();
    }

    EvaluationResponseExtendedDto mapExtended(Evaluation evaluation) {
        return EvaluationResponseExtendedDto.builder()
                .id(evaluation.getId())
                .result(evaluation.getResult())
                .user(userMapper.map(evaluation.getUser()))
                .solution(solutionMapper.map(evaluation.getSolution()))
                .group(groupMapper.map(evaluation.getGroup()))
                .creationDatetime(evaluation.getCreationDatetime())
                .lastModifiedDatetime(evaluation.getLastModifiedDatetime())
                .grade(evaluation.getGrade())
                .build();
    }

    Evaluation map(EvaluationDto evaluationDto) {
        return Evaluation.builder()
                .result(evaluationDto.getResult())
                .creationDatetime(OffsetDateTime.now())
                .lastModifiedDatetime(OffsetDateTime.now())
                .grade(evaluationDto.getGrade())
                .build();
    }

    void map(Evaluation evaluation, EvaluationDto evaluationDto) {
        evaluation.setResult(evaluationDto.getResult());
        evaluation.setLastModifiedDatetime(OffsetDateTime.now());
        evaluation.setGrade(evaluationDto.getGrade());
    }
}
