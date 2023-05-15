package pl.HomeworkJustClick.Backend.Services;

import pl.HomeworkJustClick.Backend.Entities.File;
import pl.HomeworkJustClick.Backend.Responses.FileResponse;

import java.util.List;
import java.util.Optional;

public interface FileService {

    public List<File> getAll();

    public Optional<File> getById(int id);

    public FileResponse addWithAssignment (File file, int assignment_id);

    public FileResponse addWithSolution (File file, int solution_id);

    public Boolean delete(int id);

    public List<File> getFilesByAssignment(int id);

    public List<File> getFilesBySolution(int id);
}
