package xyz.demorgan.videoservice.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import xyz.demorgan.videoservice.entity.VideoData;

import java.util.UUID;

public interface VideoDataRepository extends JpaRepository<VideoData, UUID>, JpaSpecificationExecutor<VideoData> {
}