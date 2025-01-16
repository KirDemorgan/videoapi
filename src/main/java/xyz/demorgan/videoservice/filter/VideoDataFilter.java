package xyz.demorgan.videoservice.filter;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import xyz.demorgan.videoservice.entity.VideoData;

import java.time.LocalDateTime;

public record VideoDataFilter(String nameStarts, LocalDateTime createdAtAfter) {
    public Specification<VideoData> toSpecification() {
        return Specification.where(nameStartsSpec())
                .and(createdAtAfterSpec());
    }

    private Specification<VideoData> nameStartsSpec() {
        return ((root, query, cb) -> StringUtils.hasText(nameStarts)
                ? cb.like(cb.lower(root.get("name")), nameStarts.toLowerCase() + "%")
                : null);
    }

    private Specification<VideoData> createdAtAfterSpec() {
        return ((root, query, cb) -> createdAtAfter != null
                ? cb.greaterThan(root.get("createdAt"), createdAtAfter)
                : null);
    }
}