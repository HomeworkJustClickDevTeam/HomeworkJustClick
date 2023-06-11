package pl.HomeworkJustClick.Backend.Services;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.Entities.File;
import pl.HomeworkJustClick.Backend.Repositories.AssignmentRepository;
import pl.HomeworkJustClick.Backend.Repositories.FileRepository;
import pl.HomeworkJustClick.Backend.Repositories.SolutionRepository;
import pl.HomeworkJustClick.Backend.Responses.FileResponse;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileServiceImplement implements FileService {

    private final FileRepository fileRepository;

    private final AssignmentRepository assignmentRepository;

    private final SolutionRepository solutionRepository;

    private final EntityManager entityManager;

    @Override
    public List<File> getAll() {
        return fileRepository.findAll();
    }

    @Override
    public Optional<File> getById(int id) {
        return fileRepository.findById(id);
    }

    @Override
    @Transactional
    public FileResponse addWithAssignment (File file, int assignment_id) {
        if(assignmentRepository.findById(assignment_id).isPresent()) {
            file.setAssignment(assignmentRepository.findById(assignment_id).get());
            file.setSolution(null);
            entityManager.persist(file);
            return FileResponse.builder().id(file.getId()).name(file.getName()).format(file.getFormat()).mongo_id(file.getMongo_id()).build();
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public FileResponse addWithSolution (File file, int solution_id) {
        if(solutionRepository.findById(solution_id).isPresent()) {
            file.setSolution(solutionRepository.findById(solution_id).get());
            file.setAssignment(null);
            entityManager.persist(file);
            return FileResponse.builder().id(file.getId()).name(file.getName()).format(file.getFormat()).mongo_id(file.getMongo_id()).build();
        } else {
            return null;
        }
    }

    @Override
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

    @Override
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

    @Override
    public Boolean delete(int id){
        if(fileRepository.existsById(id)) {
            fileRepository.deleteById(id);
            return true;
        } else  {
            return false;
        }
    }

    @Override
    public List<File> getFilesByAssignment(int id) {
        return fileRepository.getFilesByAssignmentId(id);
    }

    @Override
    public List<File> getFilesBySolution(int id) {
        return fileRepository.getFilesBySolutionId(id);
    }
}
