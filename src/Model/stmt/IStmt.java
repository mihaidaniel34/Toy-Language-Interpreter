package Model.stmt;

import Model.Exceptions.InterpreterException;
import Model.PrgState;
import Model.adt.IDict;
import Model.types.IType;

public interface IStmt {
    PrgState execute(PrgState state) throws InterpreterException;
    IStmt deepCopy();
    IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws InterpreterException;
}
