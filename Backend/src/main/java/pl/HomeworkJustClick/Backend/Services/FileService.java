package pl.HomeworkJustClick.Backend.Services;

import pl.HomeworkJustClick.Backend.Entities.File;
import pl.HomeworkJustClick.Backend.Responses.FileResponse;

import java.util.List;

public interface FileService {

    public List<File> getAll();

    public File getById(int id);

    public FileResponse addWithAssignment (File file, int assignment_id);

    public FileResponse addWithSolution (File file, int solution_id);

    public Boolean delete(int id);
}
