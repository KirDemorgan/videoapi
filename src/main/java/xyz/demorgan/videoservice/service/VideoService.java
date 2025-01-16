package xyz.demorgan.videoservice.service;

import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.demorgan.videoservice.Compressor.FfmpegCompressor;
import xyz.demorgan.videoservice.entity.VideoData;
import xyz.demorgan.videoservice.repos.VideoDataRepository;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class VideoService {

    MinioClient minioClient;
    VideoDataRepository videoDataRepository;
    FfmpegCompressor ffmpegCompressor;

    @Transactional
    public void uploadVideo(MultipartFile file) {
        log.info("Uploading video{}", file.getOriginalFilename());

        // validate file
        validateFile(file);

        // compress video and upload to minio
        try {
            File compressedFile = ffmpegCompressor.compress(file);

            minioClient.putObject(PutObjectArgs
                    .builder()
                    .bucket("videos")
                    .object(compressedFile.getName())
                    .stream(Files.newInputStream(Paths.get(compressedFile.getAbsolutePath())), compressedFile.length(), -1)
                    .contentType(file.getContentType())
                    .build());

            VideoData videoData = new VideoData();
            VideoData
                    .builder()
                    .name(file.getOriginalFilename())
                    .createdAt(LocalDateTime.now())
                    .build();

            // save video data to database
            videoDataRepository.save(videoData);

        } catch (Exception e) {
            log.error("Error while uploading video", e);
            throw new IllegalArgumentException("Error uploading video");
        }
    }

    public InputStream getVideo(String fileName) {
        try {
            GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                    .bucket("videos")
                    .object(fileName)
                    .build();

            GetObjectResponse response = minioClient.getObject(getObjectArgs);
            return response;
        } catch (Exception e) {
            log.error("Error while getting video", e);
            throw new IllegalArgumentException("Error getting video");
        }
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
