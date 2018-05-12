package io.github.zunpiau.utils;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class FileWatcher {

    private final FileListener listener;
    private final WatchService watchService;
    private final Map<WatchKey, Path> keys;
    private final List<Path> watchList;

    public FileWatcher(FileListener listener) throws IOException {
        this.listener = listener;
        this.watchService = FileSystems.getDefault().newWatchService();
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                doWatch();
            } catch (IOException | InterruptedException e) {
                this.listener.onError(e);
            }
        });
        keys = new HashMap<>(2);
        watchList = new ArrayList<>(4);
    }

    public void destroy() throws IOException {
        watchService.close();
    }

    public void register(Path path) throws IOException {
        if (Files.isDirectory(path))
            throw new IllegalArgumentException("require a file path");
        Path parent = path.getParent();
        if (!keys.containsValue(parent)) {
            WatchKey key = parent.register(watchService,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE);
            keys.put(key, parent);
        }
        watchList.add(path);
    }

    @SuppressWarnings("unchecked")
    private void doWatch() throws IOException, InterruptedException {
        while (true) {
            WatchKey key = watchService.take();
            Path parent = keys.get(key);
            for (WatchEvent<?> watchEvent : key.pollEvents()) {
                Path modifyFile = parent.resolve(((WatchEvent<Path>) watchEvent).context());
                for (Path path : watchList) {
                    if (Files.exists(modifyFile) && Files.isSameFile(path, modifyFile)) {
                        if (watchEvent.kind() == StandardWatchEventKinds.ENTRY_MODIFY)
                            listener.onModify(path);
                        else if (watchEvent.kind() == StandardWatchEventKinds.ENTRY_DELETE)
                            listener.onDelete(path);
                        break;
                    }
                }
            }
            if (!key.reset()) {
                keys.remove(key);
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }

    public interface FileListener {

        void onDelete(Path path);

        void onModify(Path path);

        void onError(Exception e);
    }
}
