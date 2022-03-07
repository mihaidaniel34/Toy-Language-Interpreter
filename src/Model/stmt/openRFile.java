package Model.stmt;

import Model.Exceptions.InterpreterException;
import Model.Exceptions.TypeException;
import Model.PrgState;
import Model.adt.IDict;
import Model.exp.Exp;
import Model.types.IType;
import Model.types.StringType;
import Model.value.IValue;
import Model.value.StringValue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;

public class openRFile implements IStmt {
    private Exp expression;

    public openRFile(Exp expression) {
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws InterpreterException {
        IValue expValue = this.expression.eval(state.getSymTable(), state.getHeap());
        if (!expValue.getType().equals(new StringType())) {
            throw new TypeException("Expression " + expression + " does not evaluate to a StringValue.");
        }
        StringValue fileName = (StringValue) expValue;
        IDict<StringValue, BufferedReader> fileTable = state.getFileTable();
        if (fileTable.isDefined(fileName))
            throw new InterpreterException("File " + fileName + " is already open!");
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader(fileName.getValue()));
        } catch (IOException ioException) {
            throw new InterpreterException("File " + fileName + " could not be opened");
        }
        fileTable.add(fileName, bufferedReader);
        return null;
    }

    @Override
    public String toString() {
        return "openRFile{" +
                expression +
                '}';
    }

    @Override
    public IStmt deepCopy() {
        return new openRFile(this.expression);
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws InterpreterException {
        if (expression.typeCheck(typeEnv).equals(new StringType()))
            return typeEnv;
        else
            throw new TypeException("openRFile requires a string type.");
    }
}
