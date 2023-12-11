package pl.homeworkjustclick.file;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.homeworkjustclick.assignment.AssignmentRepository;
import pl.homeworkjustclick.infrastructure.exception.EntityNotFoundException;
import pl.homeworkjustclick.solution.SolutionRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    private final AssignmentRepository assignmentRepository;

    private final SolutionRepository solutionRepository;

    private final EntityManager entityManager;

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
    public FileResponseDto addWithAssignment(File file, int assignment_id) {
        if(assignmentRepository.findById(assignment_id).isPresent()) {
            file.setAssignment(assignmentRepository.findById(assignment_id).get());
            file.setSolution(null);
            entityManager.persist(file);
            return FileResponseDto.builder().id(file.getId()).name(file.getName()).format(file.getFormat()).mongo_id(file.getMongo_id()).build();
        } else {
            return null;
        }
    }

    @Transactional
    public FileResponseDto addWithSolution(File file, int solution_id) {
        if(solutionRepository.findById(solution_id).isPresent()) {
            file.setSolution(solutionRepository.findById(solution_id).get());
            file.setAssignment(null);
            entityManager.persist(file);
            return FileResponseDto.builder().id(file.getId()).name(file.getName()).format(file.getFormat()).mongo_id(file.getMongo_id()).build();
        } else {
            return null;
        }
    }

    @Transactional
    public boolean addListWithSolution (List<File> fileList, int solution_id) {
        if(solutionRepository.findById(solution_id).isPresent()) {
            fileList.forEach(file -> {
                file.setSolution(solutionRepository.findById(solution_id).get());
                file.setAssignment(null);
                entityManager.persist(file);
            });
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public boolean addListWithAssignment (List<File> fileList, int assignment_id) {
        if(assignmentRepository.findById(assignment_id).isPresent()) {
            fileList.forEach(file -> {
                file.setAssignment(assignmentRepository.findById(assignment_id).get());
                file.setSolution(null);
                entityManager.persist(file);
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