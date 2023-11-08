package pl.HomeworkJustClick.Backend.evaluationpanel;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.HomeworkJustClick.Backend.evaluationbutton.EvaluationButton;
import pl.HomeworkJustClick.Backend.evaluationbutton.EvaluationButtonDto;
import pl.HomeworkJustClick.Backend.evaluationbutton.EvaluationButtonMapper;
import pl.HomeworkJustClick.Backend.evaluationbutton.EvaluationButtonService;
import pl.HomeworkJustClick.Backend.evaluationpanelbutton.EvaluationPanelButton;
import pl.HomeworkJustClick.Backend.evaluationpanelbutton.EvaluationPanelButtonService;
import pl.HomeworkJustClick.Backend.infrastructure.exception.EntityNotFoundException;
import pl.HomeworkJustClick.Backend.infrastructure.exception.InvalidArgumentException;
import pl.HomeworkJustClick.Backend.user.UserService;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationPanelService {
    private final EvaluationPanelRepository repository;
    private final EvaluationPanelMapper mapper;
    private final EvaluationButtonService evaluationButtonService;
    private final EvaluationButtonMapper evaluationButtonMapper;
    private final EvaluationPanelButtonService evaluationPanelButtonService;
    private final UserService userService;

    public EvaluationPanel findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("EvaluationPanel with id = " + id + " not found"));
    }

    public EvaluationPanelResponseDto getEvaluationPanelResponseDtoById(Integer id) {
        var evaluationPanel = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("EvaluationPanel with id = " + id + " not found"));
        return createEvaluationPanelResponseDto(evaluationPanel);
    }

    @Transactional
    public List<EvaluationPanelResponseDto> getEvaluationPanelsListByUserId(Integer userId) {
        var evaluationPanels = repository.findAllByUserId(userId);
        var evaluationPanelResponseDtoList = new ArrayList<EvaluationPanelResponseDto>();
        evaluationPanels.forEach(evaluationPanel ->
                evaluationPanelResponseDtoList.add(createEvaluationPanelResponseDto(evaluationPanel)));
        return evaluationPanelResponseDtoList;
    }

    public Slice<EvaluationPanelResponseDto> getEvaluationPanelsByUserId(Integer userId, Pageable pageable) {
        var evaluationPanels = repository.findAllByUserId(userId, pageable);
        var evaluationPanelResponseDtoList = new ArrayList<EvaluationPanelResponseDto>();
        evaluationPanels.forEach(evaluationPanel ->
                evaluationPanelResponseDtoList.add(createEvaluationPanelResponseDto(evaluationPanel)));
        return new PageImpl<>(evaluationPanelResponseDtoList, pageable, evaluationPanelResponseDtoList.size());
    }

    public EvaluationPanelResponseDto createEvaluationPanel(EvaluationPanelDto evaluationPanelDto) {
        validateEvaluationPanelDto(evaluationPanelDto);
        var evaluationPanel = mapper.map(evaluationPanelDto);
        var savedButtonsList = createButtonsList(evaluationPanelDto);
        evaluationPanel.setUser(userService.findById(evaluationPanelDto.getUserId()));
        var savedEvaluationPanel = repository.save(evaluationPanel);
        var evaluationPanelButtons = evaluationPanelButtonService.save(savedEvaluationPanel, savedButtonsList);
        savedEvaluationPanel.setEvaluationPanelButtons(evaluationPanelButtons);
        return createEvaluationPanelResponseDto(savedEvaluationPanel);
    }

    public void deleteEvaluationPanel(Integer id) {
        var evaluationPanel = findById(id);
        evaluationPanelButtonService.deleteAllByEvaluationPanelId(evaluationPanel.getId());
        repository.delete(evaluationPanel);
    }

    public EvaluationPanelResponseDto updateEvaluationPanel(Integer id, EvaluationPanelDto evaluationPanelDto) {
        validateEvaluationPanelDto(evaluationPanelDto);
        var evaluationPanel = findById(id);
        mapper.map(evaluationPanelDto, evaluationPanel);
        var savedButtonsList = (createButtonsList(evaluationPanelDto));
        evaluationPanel.setUser(userService.findById(evaluationPanelDto.getUserId()));
        var savedEvaluationPanel = repository.save(evaluationPanel);
        evaluationPanelButtonService.deleteAllByEvaluationPanelId(id);
        var evaluationPanelButtons = evaluationPanelButtonService.save(savedEvaluationPanel, savedButtonsList);
        savedEvaluationPanel.setEvaluationPanelButtons(evaluationPanelButtons);
        return createEvaluationPanelResponseDto(savedEvaluationPanel);
    }

    public EvaluationPanelResponseDto updateEvaluationPanelCounterAndLastUsedDate(Integer id) {
        var evaluationPanel = findById(id);
        evaluationPanel.setCounter(evaluationPanel.getCounter() + 1);
        evaluationPanel.setLastUsedDate(OffsetDateTime.now());
        return mapper.map(repository.save(evaluationPanel));
    }

    private EvaluationPanelResponseDto createEvaluationPanelResponseDto(EvaluationPanel evaluationPanel) {
        var buttonIds = evaluationPanel.getEvaluationPanelButtons()
                .stream()
                .map(EvaluationPanelButton::getEvaluationButton)
                .map(EvaluationButton::getId)
                .toList();
        var buttons = evaluationButtonService.findById(buttonIds)
                .stream()
                .map(evaluationButtonMapper::map)
                .sorted(Comparator.comparing(EvaluationButtonDto::getPoints))
                .toList();
        var evaluationPanelResponseDto = mapper.map(evaluationPanel);
        evaluationPanelResponseDto.setButtons(buttons);
        return evaluationPanelResponseDto;
    }

    private List<EvaluationButton> createButtonsList(EvaluationPanelDto evaluationPanelDto) {
        var buttons = evaluationPanelDto.getButtons();
        validateButtonsList(buttons);
        var savedButtonsList = new ArrayList<EvaluationButton>();
        buttons.forEach(button -> {
            var savedButton = evaluationButtonService.createOrGetEvaluationButton(button);
            savedButtonsList.add(savedButton);
        });
        return savedButtonsList;
    }

    private void validateEvaluationPanelDto(EvaluationPanelDto evaluationPanelDto) {
        if (evaluationPanelDto.getWidth() > 5 || evaluationPanelDto.getWidth() < 1) {
            throw new InvalidArgumentException("Width must be greater or equal 1 and lower or equal 5");
        }
    }

    private void validateButtonsList(List<EvaluationButtonDto> buttons) {
        if (buttons.isEmpty()) {
            throw new InvalidArgumentException("Button list must not be empty");
        }
    }
}
