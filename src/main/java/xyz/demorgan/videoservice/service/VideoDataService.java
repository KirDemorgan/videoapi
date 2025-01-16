package xyz.demorgan.videoservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import xyz.demorgan.videoservice.entity.VideoData;
import xyz.demorgan.videoservice.filter.VideoDataFilter;
import xyz.demorgan.videoservice.repos.VideoDataRepository;

@RequiredArgsConstructor
@Service
public class VideoDataService {

    private final VideoDataRepository videoDataRepository;

    public Page<VideoData> getAll(VideoDataFilter filter, Pageable pageable) {
        Specification<VideoData> spec = filter.toSpecification();
        return videoDataRepository.findAll(spec, pageable);
    }
}
