package xyz.demorgan.videoservice.controller;

import io.minio.MinioClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.demorgan.videoservice.entity.VideoData;
import xyz.demorgan.videoservice.filter.VideoDataFilter;
import xyz.demorgan.videoservice.service.VideoDataService;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/videos")
@Tag(name = "Video Controller", description = "API для работы с S3 MiniO для загрузки видео.")
@Slf4j
public class VideoController {

    private final VideoDataService videoDataService;

    @GetMapping
    @Operation(summary = "Получить все видео", description = "Получить все видео с возможностью фильтрации и пагинации.")
    public PagedModel<VideoData> getAll(@ParameterObject @ModelAttribute VideoDataFilter filter, @ParameterObject Pageable pageable) {
        log.info("Getting all videos");
        Page<VideoData> videoData = videoDataService.getAll(filter, pageable);
        return new PagedModel<>(videoData);
    }

    @PostMapping("/upload")
    public VideoData uploadVideo(@RequestBody MultipartFile file) {
        log.info("Uploading video");

        return null;
    }
}
