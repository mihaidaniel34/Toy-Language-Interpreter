package Model.stmt;

import Model.Exceptions.InterpreterException;
import Model.Exceptions.TypeException;
import Model.PrgState;
import Model.adt.IDict;
import Model.exp.Exp;
import Model.types.IType;
import Model.types.IntType;
import Model.types.StringType;
import Model.value.IValue;
import Model.value.IntValue;
import Model.value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class readFile implements IStmt {

    Exp expression;
    String varName;

    public readFile(Exp expression, String varName) {
        this.expression = expression;
        this.varName = varName;
    }


    @Override
    public PrgState execute(PrgState state) throws InterpreterException {
        IDict<String, IValue> symTable = state.getSymTable();
        if (!symTable.isDefined(varName))
            throw new TypeException("Variable " + varName + " was not declared.");
        if (!symTable.lookup(varName).getType().equals(new IntType())) {
            throw new TypeException("Variable " + varName + " is not an integer type.");
        }
        IValue value = expression.eval(symTable, state.getHeap());
        if (!value.getType().equals(new StringType()))
            throw new TypeException("Expression " + expression + " does not evaluate to a string value.");
        StringValue fileName = (StringValue) value;
        IDict<StringValue, BufferedReader> fileTable = state.getFileTable();
        if (!fileTable.isDefined(fileName))
            throw new InterpreterException("File " + fileName + " is not open.");
        BufferedReader bufferedReader = fileTable.lookup(fileName);
        try {
            String buff = bufferedReader.readLine();
            if (buff == null)
                buff = "0";
            int number = Integer.parseInt(buff);
            symTable.update(varName, new IntValue(number));
        } catch (IOException ioException) {
            throw new InterpreterException("An error occurred while reading from the file.");
        } catch (NumberFormatException e) {
            throw new TypeException(" An error occurred while reading integer from file.");
        }

        return null;
    }

    @Override
    public String toString() {
        return "readFile{" +
                expression +
                "," + varName +
                '}';
    }

    @Override
    public IStmt deepCopy() {
        return new readFile(expression, varName);
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws InterpreterException {
        IType typeVar = typeEnv.lookup(varName);
        IType typeExp = expression.typeCheck(typeEnv);
        if(!typeExp.equals(new StringType()))
            throw new TypeException("readFile: Expression " + expression + " does not evaluate to a StringType" );
        if(!typeVar.equals(new IntType()))
            throw new TypeException("readFile: Variable " + varName + " is not an IntType");
        return typeEnv;
    }
}
