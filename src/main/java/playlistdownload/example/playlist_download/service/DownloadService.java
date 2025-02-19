package playlistdownload.example.playlist_download.service;

import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class DownloadService {
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private final ConcurrentHashMap<String, Integer> progressMap = new ConcurrentHashMap<>();

    public String downloadVideo(String videoUrl, String downloadId) {
        progressMap.put(downloadId, 0);
        String fileName = "video_" + downloadId + ".mp4";
        String filePath = "downloads/" + fileName;

        executorService.submit(() -> {
            try {
                // ytdlp ya youtube-dl ka use karna padega
                ProcessBuilder pb = new ProcessBuilder(
                        "yt-dlp", "-f", "best", "-o", filePath, videoUrl
                );
                pb.inheritIO();
                Process process = pb.start();
                process.waitFor();
                
                progressMap.put(downloadId, 100);
            } catch (IOException | InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        return fileName;
    }

    public int getProgress(String downloadId) {
        return progressMap.getOrDefault(downloadId, 0);
    }

    public void cancelDownload(String downloadId) {
        progressMap.remove(downloadId);
    }
}
