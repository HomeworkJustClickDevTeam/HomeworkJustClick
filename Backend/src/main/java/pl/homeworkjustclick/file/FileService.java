package pl.homeworkjustclick.file;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.homeworkjustclick.assignment.AssignmentRepository;
import pl.homeworkjustclick.assignment.AssignmentService;
import pl.homeworkjustclick.infrastructure.exception.EntityNotFoundException;
import pl.homeworkjustclick.solution.SolutionRepository;
import pl.homeworkjustclick.solution.SolutionService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final AssignmentRepository assignmentRepository;
    private final SolutionRepository solutionRepository;
    private final AssignmentService assignmentService;
    private final SolutionService solutionService;

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
        if (assignmentRepository.findById(assignmentId).isPresent()) {
            file.setAssignment(assignmentService.findById(assignmentId));
            file.setSolution(null);
            var savedFile = fileRepository.save(file);
            return FileResponseDto.builder().id(savedFile.getId()).name(savedFile.getName()).format(savedFile.getFormat()).mongoId(savedFile.getMongoId()).build();
        } else {
            return null;
        }
    }

    @Transactional
    public FileResponseDto addWithSolution(File file, int solutionId) {
        if (solutionRepository.findById(solutionId).isPresent()) {
            file.setSolution(solutionService.findById(solutionId));
            file.setAssignment(null);
            var savedFile = fileRepository.save(file);
            return FileResponseDto.builder().id(savedFile.getId()).name(savedFile.getName()).format(savedFile.getFormat()).mongoId(savedFile.getMongoId()).build();
        } else {
            return null;
        }
    }

    @Transactional
    public boolean addListWithSolution(List<File> fileList, int solutionId) {
        if (solutionRepository.findById(solutionId).isPresent()) {
            fileList.forEach(file -> {
                file.setSolution(solutionService.findById(solutionId));
                file.setAssignment(null);
                fileRepository.save(file);
            });
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public boolean addListWithAssignment(List<File> fileList, int assignmentId) {
        if (assignmentRepository.findById(assignmentId).isPresent()) {
            fileList.forEach(file -> {
                file.setAssignment(assignmentService.findById(assignmentId));
                file.setSolution(null);
                fileRepository.save(file);
            });
            return true;
        } else {
            return false;
        }
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
