package pl.homeworkjustclick.file;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.homeworkjustclick.assignment.AssignmentRepository;
import pl.homeworkjustclick.assignment.AssignmentService;
import pl.homeworkjustclick.infrastructure.exception.EntityNotFoundException;
import pl.homeworkjustclick.infrastructure.exception.InvalidArgumentException;
import pl.homeworkjustclick.solution.SolutionService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final AssignmentRepository assignmentRepository;
    private final AssignmentService assignmentService;
    private final SolutionService solutionService;

    private final List<String> formats = List.of("txt", "json", "xml", "jpg", "png");

    public List<File> getAll() {
        return fileRepository.findAll();
    }

    public Optional<File> getById(int id) {
        return fileRepository.findById(id);
    }

    public File findById(Integer id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("File with id = " + id + " not found"));
    }

    @Transactional
    public FileResponseDto addWithAssignment(File file, int assignmentId) {
        var assignment = assignmentService.findById(assignmentId);
        file.setAssignment(assignment);
        file.setSolution(null);
        var savedFile = fileRepository.save(file);
        return FileResponseDto.builder().id(savedFile.getId()).name(savedFile.getName()).format(savedFile.getFormat()).mongoId(savedFile.getMongoId()).build();
    }

    @Transactional
    public FileResponseDto addWithSolution(File file, int solutionId) {
        var solution = solutionService.findById(solutionId);
        if (Boolean.TRUE.equals(solution.getAssignment().getAdvancedEvaluation()) && !formats.contains(file.getFormat())) {
            throw new InvalidArgumentException("Invalid format for extended evaluation");
        }
        file.setSolution(solution);
        file.setAssignment(null);
        var savedFile = fileRepository.save(file);
        return FileResponseDto.builder().id(savedFile.getId()).name(savedFile.getName()).format(savedFile.getFormat()).mongoId(savedFile.getMongoId()).build();
    }

    @Transactional
    public Boolean delete(int id){
        if(fileRepository.existsById(id)) {
            fileRepository.deleteById(id);
            return true;
        } else  {
            return false;
        }
    }

    public List<File> getFilesByAssignment(int id) {
        return fileRepository.getFilesByAssignmentId(id);
    }

    public List<File> getFilesBySolution(int id) {
        return fileRepository.getFilesBySolutionId(id);
    }
}
