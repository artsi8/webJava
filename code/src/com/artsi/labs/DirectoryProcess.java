package com.artsi.labs;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class DirectoryProcess implements Callable<Void> {
    private File dir;
    private String saveTo;
    private ExecutorService pool;
    public DirectoryProcess(File dir, String saveTo, ExecutorService pool) {
        this.dir = dir;
        this.saveTo = saveTo;
        this.pool = pool;

    }
    public Void call() {
        File[] files = dir.listFiles();
        assert files != null;
        for (File ff : files)

            if (ff.isDirectory()) {
                DirectoryProcess directoryProcess = new DirectoryProcess(ff, saveTo, pool);
                pool.submit(directoryProcess);

            } else if (ff.getName().toLowerCase().endsWith(".java")) {
                FileProcess removeComments = new FileProcess(ff, saveTo);
                pool.submit(removeComments);
            }
        return null;
    }
}
