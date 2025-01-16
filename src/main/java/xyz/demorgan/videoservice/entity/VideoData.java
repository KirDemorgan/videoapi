package xyz.demorgan.videoservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "video_data")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VideoData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    String name;

    @Column(name = "created_at")
    LocalDateTime createdAt;
}
