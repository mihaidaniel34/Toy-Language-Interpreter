package Model.stmt;

import Model.Exceptions.InterpreterException;
import Model.PrgState;
import Model.adt.IDict;
import Model.exp.ValueExp;
import Model.types.IType;
import Model.value.IntValue;

public class WaitStmt implements IStmt {
    private Integer number;

    public WaitStmt(Integer number) {
        this.number = number;
    }


    @Override
    public PrgState execute(PrgState state) throws InterpreterException {
        if(number!=0)
            state.getStk().push(new CompStmt(new PrintStmt(new ValueExp(new IntValue(number))),
                    new WaitStmt(number-1)));
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new WaitStmt(number);
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws InterpreterException {
        return typeEnv;
    }

    @Override
    public String toString() {
        return "wait(" + number + ')';
    }
}
