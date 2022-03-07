package Repo;

import Model.Exceptions.InterpreterException;
import Model.Exceptions.ListException;
import Model.PrgState;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Repo implements IRepo {

    List<PrgState> myPrgStates;
    PrgState crtPrg;
    String logFilePath;

    public Repo(String logFilePath) {
        this.logFilePath = logFilePath;
        myPrgStates = new ArrayList<>();
        crtPrg = null;
        emptyLogFile();
    }

    public void setLogFilePath(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    @Override
    public void emptyLogFile() {
        try {
            new FileWriter(logFilePath, false).close();
        } catch (IOException ignored) {

        }
    }

    @Override
    public void setCrtPrg(PrgState prgState) {
        crtPrg = prgState;
    }

    @Override
    public PrgState getPrg(int index) {
        return myPrgStates.get(index);
    }

    @Override
    public int size() {
        return myPrgStates.size();
    }

    @Override
    public List<PrgState> getStates() {
        return myPrgStates;
    }

    @Override
    public void logPrgStateExec(PrgState prgState) throws InterpreterException {
        try {
            PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)));
            printWriter.write(prgState.toString() + "\n\n");
            printWriter.close();
        } catch (IOException ioException) {
            throw new InterpreterException("Error occurred while writing to file " + logFilePath + ".");
        }

    }

    public void setPrgList(List<PrgState> prgList){
        this.myPrgStates = prgList;
    }

    @Override
    public void addPrg(PrgState newPrg) {
        myPrgStates.add(newPrg);
        crtPrg = newPrg;
    }

}
