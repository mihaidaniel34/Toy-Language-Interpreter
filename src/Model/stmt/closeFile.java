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
import java.io.IOException;

public class closeFile implements IStmt {

    Exp expression;

    public closeFile(Exp expression) {
        this.expression = expression;
    }


    @Override
    public PrgState execute(PrgState state) throws InterpreterException {
        IDict<StringValue, BufferedReader> fileTable = state.getFileTable();
        IValue value = expression.eval(state.getSymTable(), state.getHeap());
        if (!value.getType().equals(new StringType())) {
            throw new TypeException("Expression " + expression + " does not evaluate to a string value.");
        }
        StringValue fileName = (StringValue) value;
        if (!fileTable.isDefined(fileName))
            throw new InterpreterException("Cannot close file " + fileName + " because it is not open.");
        BufferedReader bufferedReader = fileTable.lookup(fileName);
        try {
            bufferedReader.close();
            fileTable.delete(fileName);
        } catch (IOException ioException) {
            throw new InterpreterException("Error while closing file " + fileName + ".");
        }

        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new closeFile(this.expression);
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws InterpreterException {
        if(expression.typeCheck(typeEnv).equals(new StringType()))
            return typeEnv;
        else
            throw new TypeException("Expression " + expression + " does not evaluate to a string value");
    }

    @Override
    public String toString() {
        return "closeFile{" +
                expression +
                '}';
    }
}
