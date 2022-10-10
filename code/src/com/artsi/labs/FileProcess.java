package com.artsi.labs;

import java.io.*;

public class FileProcess implements Runnable {
    File file;
    String saveTo;

    public FileProcess(File file, String saveTo) {
        this.file = file;
        this.saveTo = saveTo;

    }
    @Override
    public void run() {
            StringBuffer newFileName;
            newFileName = getNewName(new StringBuffer(file.getPath()));
            File resultFile = new File(saveTo.concat("\\"+newFileName));
            System.out.println(newFileName);
            if (resultFile.exists()) {
                resultFile.delete();
            }
            try {
                FileReader source = new FileReader(file);
                resultFile.createNewFile();
                FileWriter sink =  new FileWriter(resultFile);
                CommentRemover.process(source,sink);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private StringBuffer getNewName(StringBuffer path) {
        char[] extraСhars = new char[]{'\\','/',':'};
        for(char c : extraСhars) {
            while (true) {
                int ind = path.indexOf(String.valueOf(c));
                if (ind == -1) {break;}
                path.setCharAt(ind,' ');
            }
        }
        return path;
    }

}
