@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    public ResumeResponse uploadResume(MultipartFile file) {

        Resume resume = new Resume();

        try {
            resume.setFileName(file.getOriginalFilename());
            resume.setFileType(file.getContentType());
            resume.setData(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload resume", e);
        }

        Resume savedResume = resumeRepository.save(resume);

        return mapToResponse(savedResume);
    }
    public Resume getResumeById(Long id) {

        return resumeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Resume not found"));
    }

    public void deleteResume(Long id) {

        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Resume not found"));

        resumeRepository.delete(resume);
    }
}