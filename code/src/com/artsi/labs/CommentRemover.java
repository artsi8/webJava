package com.artsi.labs;

import java.io.*;
import java.util.Arrays;


public class CommentRemover {

    private enum State{
        CODE,
        MAYBE_COMMENT_STARTS,
        ONE_LINE_COMMENT,
        MANY_LINE_COMMENT,
        MAYBE_COMMENT_ENDS,
    }

    public static void process(Reader source, Writer sink)
     throws IOException {

        //буфер фіксованого розміру для вичитування по частинам
        State state = State.CODE;
        int code_start = 0;
        while (true){
            char[] buf = new char[11];
            int n_read = source.read(buf);//скіль байт вичитано
            if (n_read == -1) {break;}//кінець потоку байтів(кінець файлу)
            //у write писати лише без коментарів(машина станів коментаря)
            for (int i = 0; i<n_read;i++) {
                char c = buf[i];
                switch (state) {
                    case CODE:
                        if (c == '/') {
                            state = State.MAYBE_COMMENT_STARTS;
                        }
                        break;
                    case MAYBE_COMMENT_STARTS:
                        if (c == '/') {
                            state = State.ONE_LINE_COMMENT;
                            if (i!=0)
                                sink.write(buf, code_start, i-1-code_start);
                        } else if (c == '*') {
                            state = State.MANY_LINE_COMMENT;
                            if (i!=0)
                                sink.write(buf, code_start, i-1-code_start);
                        } else {
                            if (i==0)
                                sink.write("/");
                            state = State.CODE;
                        }
                        break;
                    case ONE_LINE_COMMENT:
                        if (c == '\n') {
                            state = State.CODE;//
                            code_start = i;
                        } else {
                            state = State.ONE_LINE_COMMENT;
                        }
                        break;
                    case MANY_LINE_COMMENT:
                        if (c == '*') {
                            state = State.MAYBE_COMMENT_ENDS;
                        } else {
                            state = State.MANY_LINE_COMMENT;
                        }
                        break;
                    case MAYBE_COMMENT_ENDS:
                        if (c == '/') {
                            state = State.CODE;//
                            code_start = i+1;
                            sink.write(" ");
                        } else {
                            state = State.MANY_LINE_COMMENT;
                        }
                        break;
                        //code ->
                }
            }
            switch (state) {
                case CODE: {
                    int end = buf.length - code_start;
                    if (n_read<buf.length) {
                        end = n_read;
                    }
                    sink.write(buf,code_start,end);
                    code_start = 0;
                    break;
                }
                case MAYBE_COMMENT_STARTS: {
                    sink.write(buf,code_start,buf.length - code_start-1);
                    code_start = 0;
                    break;
                }
                case MAYBE_COMMENT_ENDS:
                case ONE_LINE_COMMENT:
                case MANY_LINE_COMMENT: {
                    code_start = 0;
                    break;
                }

            }
        }
        sink.flush();
        sink.close();
    }


}
