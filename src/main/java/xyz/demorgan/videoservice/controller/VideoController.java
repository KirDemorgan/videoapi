package xyz.demorgan.videoservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.demorgan.videoservice.entity.VideoData;
import xyz.demorgan.videoservice.filter.VideoDataFilter;
import xyz.demorgan.videoservice.repos.VideoDataRepository;
import xyz.demorgan.videoservice.service.VideoDataService;
import xyz.demorgan.videoservice.service.VideoService;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/videos")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Tag(name = "Video Controller", description = "API для работы с S3 MiniO для загрузки видео.")
@Slf4j
public class VideoController {

    VideoDataService videoDataService;
    VideoService videoService;
    VideoDataRepository videoDataRepository;

    @GetMapping
    @Operation(summary = "Получить все видео", description = "Получить все видео с возможностью фильтрации и пагинации.")
    public PagedModel<VideoData> getAll(@ParameterObject @ModelAttribute VideoDataFilter filter, @ParameterObject Pageable pageable) {
        log.info("Getting all videos");
        Page<VideoData> videoData = videoDataService.getAll(filter, pageable);
        return new PagedModel<>(videoData);
    }

    @GetMapping("/download/{fileName}")
    @Operation(summary = "Скачать видео", description = "Скачать видео по имени файла.")
    public ResponseEntity<InputStreamResource> downloadVideo(@PathVariable String fileName) {
        log.info("Downloading video {}", fileName);

        InputStream videoStream = videoService.getVideo(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                .body(new InputStreamResource(videoStream));
    }

    @PostMapping("/upload")
    @Operation(summary = "Загрузить видео", description = "Загрузить видео на сервер.")
    public ResponseEntity<VideoData> uploadVideo(@RequestBody MultipartFile file) {
        log.info("Uploading video{}", file.getOriginalFilename());

        VideoData videoData =  videoService.uploadVideo(file);

        return ResponseEntity.ok().body(videoData);
    }
}
