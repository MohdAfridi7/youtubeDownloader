package playlistdownload.example.playlist_download.controller;

import playlistdownload.example.playlist_download.service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = {"https://effulgent-bubblegum-23ea6b.netlify.app/", "*"})
@RestController
@RequestMapping("/api")
public class DownloadController {

    @Autowired
    private DownloadService downloadService;

    @PostMapping("/download")
    public ResponseEntity<?> startDownload(@RequestBody Map<String, String> request) {
        String videoUrl = request.get("url");
        if (videoUrl == null || videoUrl.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid URL");
        }

        String downloadId = UUID.randomUUID().toString();
        String filePath = downloadService.downloadVideo(videoUrl, downloadId);

        return ResponseEntity.ok(Map.of("message", "Download started", "downloadId", downloadId, "filePath", filePath));
    }

    @GetMapping("/progress/{downloadId}")
    public ResponseEntity<?> getProgress(@PathVariable String downloadId) {
        int progress = downloadService.getProgress(downloadId);
        return ResponseEntity.ok(Map.of("progress", progress));
    }

    @PostMapping("/cancel/{downloadId}")
    public ResponseEntity<?> cancelDownload(@PathVariable String downloadId) {
        downloadService.cancelDownload(downloadId);
        return ResponseEntity.ok(Map.of("message", "Download cancelled"));
    }

    @GetMapping("/download-file/{fileName}")
    public ResponseEntity<FileSystemResource> getFile(@PathVariable String fileName) {
        File file = new File("downloads/" + fileName);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(new FileSystemResource(file));
    }
}
