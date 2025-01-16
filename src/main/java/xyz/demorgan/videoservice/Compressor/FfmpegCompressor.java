package xyz.demorgan.videoservice.Compressor;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FfmpegCompressor {
    FFprobe ffprobe;
    FFmpeg ffmpeg;

    public File compress(MultipartFile file) throws IOException {

        File inputFile = new File(System.getProperty("java.io.tmpdir"), file.getOriginalFilename());
        file.transferTo(inputFile);

        FFmpegProbeResult probeResult = ffprobe.probe(inputFile.getAbsolutePath());

        File outputFile = new File(System.getProperty("java.io.tmpdir"), "c_" + file.getOriginalFilename());

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(probeResult)
                .addOutput(outputFile.getAbsolutePath())
                .setVideoResolution(1920, 1080)
                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();

        return outputFile;
    }
}
