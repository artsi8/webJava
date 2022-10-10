package com.artsi.labs;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.*;


public class Start {

    public static void main(String[] args) throws InterruptedException {
        String dir = inputDir("enter the directory to process: ");
        String saveTo = inputDir("enter the directory for the result: ");

        //  C:\\webJavaTest
        //  C:\\webJavaResult

        ExecutorService pool = Executors.newCachedThreadPool();
        DirectoryProcess directoryProcess = new DirectoryProcess(new File(dir), saveTo, pool);
        pool.submit(directoryProcess);

        Thread.sleep(4000);
        pool.shutdown();
    }
    private static String inputDir(String message) {
        while (true) {
            System.out.println(message);
            Scanner sc = new Scanner(System.in);
            String dir = sc.next();
            File f = new File(dir);
            if (f.isDirectory() && f.exists()) {
                return dir;
            }
        }
    }
}





