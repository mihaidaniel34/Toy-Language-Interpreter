package Repo;
import Model.Exceptions.InterpreterException;
import Model.Exceptions.ListException;
import Model.PrgState;
import Model.adt.IList;

import java.io.IOException;
import java.util.List;

public interface IRepo {
    void addPrg(PrgState newPrg);
    void setCrtPrg(PrgState prgState);
    PrgState getPrg(int index);
    int size();
    List<PrgState> getStates();
    void logPrgStateExec(PrgState prgState) throws InterpreterException;
    void setLogFilePath(String logFilePath);
    void emptyLogFile() throws InterpreterException;
    public void setPrgList(List<PrgState> prgList);
    }
