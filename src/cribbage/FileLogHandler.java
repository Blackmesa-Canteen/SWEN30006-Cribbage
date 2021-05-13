package cribbage;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * @program Cribbage
 * @description A pure Fabrication Class that take responsibility to handle file log tasks
 * @create 2021-05-11 18:58
 */
public class FileLogHandler {

    private final String FILE_NAME;
    private BufferedWriter out;
    boolean isFPClosed = true;

    public FileLogHandler(String fileName) {
        FILE_NAME = fileName;
    }

    public void initFP() {
        try {
            if(isFPClosed) {
                out = new BufferedWriter(new FileWriter(FILE_NAME));
                isFPClosed = false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeFP() {
        try {
            if(!isFPClosed) {
                out.close();
                isFPClosed = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeMessageToLog(String message) {
        try {
            out.write(message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeLineMessageToLog(String message) {
        try {
            out.write(message );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}