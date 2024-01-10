package pl.homeworkjustclick.file;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileFindService {
    private final FileRepository repository;

    public Optional<File> findFileBySolutionId(Integer solutionId) {
        return repository.findBySolutionId(solutionId);
    }

    public Optional<File> findFileByAssignmentId(Integer assignmentId) {
        return repository.findByAssignmentId(assignmentId);
    }
}
