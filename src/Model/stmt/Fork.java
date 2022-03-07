package Model.stmt;

import Model.Exceptions.InterpreterException;
import Model.PrgState;
import Model.adt.IDict;
import Model.adt.MyStack;
import Model.types.IType;

public class Fork implements IStmt{
    IStmt stmt;

    public Fork(IStmt stmt){
        this.stmt = stmt;
    }

    @Override
    public PrgState execute(PrgState state) throws InterpreterException {
        return new PrgState(new MyStack<>(), state.getSymTable().deepCopy(), state.getOutput(), state.getFileTable(), state.getHeap(), stmt);
    }

    @Override
    public IStmt deepCopy() {
        return new Fork(stmt);
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws InterpreterException {
        stmt.typeCheck(typeEnv.deepCopy());
        return typeEnv;
    }

    @Override
    public String toString() {
        return "Fork{" + stmt +
                '}';
    }
}
