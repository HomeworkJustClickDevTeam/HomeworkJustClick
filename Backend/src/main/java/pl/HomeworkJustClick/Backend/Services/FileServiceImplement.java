package pl.HomeworkJustClick.Backend.Services;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.Entities.File;
import pl.HomeworkJustClick.Backend.Repositories.AssignmentRepository;
import pl.HomeworkJustClick.Backend.Repositories.FileRepository;
import pl.HomeworkJustClick.Backend.Repositories.SolutionRepository;
import pl.HomeworkJustClick.Backend.Responses.FileResponse;

import java.util.List;

@Service
public class FileServiceImplement implements FileService {

    @Autowired
    FileRepository fileRepository;

    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    SolutionRepository solutionRepository;

    EntityManager entityManager;

    @Override
    public List<File> getAll() {
        return fileRepository.findAll();
    }

    @Override
    public File getById(int id) {
        if (fileRepository.findById(id).isPresent()) {
            return fileRepository.findById(id).get();
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public FileResponse addWithAssignment (File file, int assignment_id) {
        if(assignmentRepository.findById(assignment_id).isPresent()) {
            file.setAssignment(assignmentRepository.findById(assignment_id).get());
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
            entityManager.persist(file);
            return FileResponse.builder().id(file.getId()).name(file.getName()).format(file.getFormat()).mongo_id(file.getMongo_id()).build();
        } else {
            return null;
        }
    }

    @Override
    public Boolean delete(int id){
        try {
            fileRepository.deleteById(id);
            return true;
        } catch (IllegalArgumentException e) {
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
