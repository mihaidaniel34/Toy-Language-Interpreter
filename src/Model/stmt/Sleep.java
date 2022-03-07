package Model.stmt;

import Model.Exceptions.InterpreterException;
import Model.PrgState;
import Model.adt.IDict;
import Model.types.IType;

public class Sleep implements IStmt {

    private Integer number;

    public Sleep(Integer number) {
        this.number = number;
    }

    @Override
    public PrgState execute(PrgState state) throws InterpreterException {
        if (number != 0)
            state.getStk().push(new Sleep(number - 1));
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new Sleep(number);
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws InterpreterException {
        return typeEnv;
    }

    @Override
    public String toString() {
        return "Sleep(" +
                number + ")";
    }
}
