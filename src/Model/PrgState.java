package Model;

import Model.Exceptions.InterpreterException;
import Model.Exceptions.StackException;
import Model.adt.*;
import Model.stmt.IStmt;
import Model.value.IValue;
import Model.value.StringValue;

import java.io.BufferedReader;

public class PrgState {

    IStack<IStmt> exeStack;
    IDict<String, IValue> symTable;
    IList<IValue> out;
    IDict<StringValue, BufferedReader> fileTable;
    IHeap heap;
    int id;
    static int staticId = 0;

    IStmt originalProgram;

    public PrgState(IStack<IStmt> stk, IDict<String, IValue> symTable, IList<IValue> out, IDict<StringValue, BufferedReader> fileTable, IHeap heap, IStmt originalProgram) {
        this.exeStack = stk;
        this.symTable = symTable;
        this.out = out;
        this.fileTable = fileTable;
        this.originalProgram = originalProgram.deepCopy();
        this.heap = heap;
        stk.push(originalProgram);
        this.id = getNewId();
    }

    public IStack<IStmt> getStk() {
        return exeStack;
    }

    public IList<IValue> getOutput() {
        return out;
    }

    public IDict<String, IValue> getSymTable() {
        return this.symTable;
    }

    public boolean isNotCompleted() {
        return !(exeStack.isEmpty());
    }

    public int getId() {
        return id;
    }

    public IHeap getHeap() {
        return heap;
    }

    public void setOutput(IList<IValue> output) {
        this.out = output;
    }

    public IDict<StringValue, BufferedReader> getFileTable() {
        return fileTable;
    }

    public IStmt getOriginalProgram() {
        return originalProgram;
    }

    @Override
    public String toString() {
        return "Program " + id +
                "\nexeStack=" + exeStack +
                "\nsymTable=" + symTable +
                "\nout=" + out +
                "\nfileTable = " + fileTableToString() +
                "\nheap=" + heap + "\n";
    }

    private String fileTableToString() {
        StringBuilder fileTableString = new StringBuilder("Dict: {");
        for (StringValue key : fileTable.getKeys()) {
            fileTableString.append(key.getValue()).append(",");
        }
        if (fileTableString.lastIndexOf(",") != -1)
            fileTableString.deleteCharAt(fileTableString.lastIndexOf(","));
        fileTableString.append("}");
        return fileTableString.toString();
    }

    public PrgState oneStep() throws InterpreterException {
        if (exeStack.isEmpty()) throw new StackException("PrgState stack is empty.");
        IStmt crtStmt = exeStack.pop();
        return crtStmt.execute(this);
    }

    private static synchronized int getNewId() {
        ++staticId;
        return staticId;
    }
}