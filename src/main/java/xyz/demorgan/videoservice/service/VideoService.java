package xyz.demorgan.videoservice.service;

import io.minio.MinioClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.demorgan.videoservice.repos.VideoDataRepository;

@Service
@Slf4j
@AllArgsConstructor
public class VideoService {

    MinioClient minioClient;
    VideoDataRepository videoDataRepository;

    public void uploadVideo(MultipartFile file) {
    log.info("Uploading video");

    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        if (!file.getContentType().toLowerCase().startsWith("video/")) {
            throw new IllegalArgumentException("File is not a video");
        }
        String fileName = file.getOriginalFilename();
        if (fileName != null && !fileName.toLowerCase().matches(".*\\.(mp4|avi|mkv|mov)$")) {
            throw new IllegalArgumentException("Недопустимое расширение файла");
        }
    }
}
