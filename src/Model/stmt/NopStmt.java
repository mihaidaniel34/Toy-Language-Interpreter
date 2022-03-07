package Model.stmt;

import Model.Exceptions.InterpreterException;
import Model.PrgState;
import Model.adt.IDict;
import Model.types.IType;

public class NopStmt implements IStmt {

    public PrgState execute(PrgState state){
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new NopStmt();
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws InterpreterException {
        return typeEnv;
    }

    @Override
    public String toString(){
        return "no operation";
    }
}
