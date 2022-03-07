package Model.stmt;


import Model.Exceptions.InterpreterException;
import Model.PrgState;
import Model.adt.IDict;
import Model.adt.IList;
import Model.exp.Exp;
import Model.types.IType;
import Model.value.IValue;

public class PrintStmt implements IStmt{

    Exp expression;

    public PrintStmt(Exp exp){
        this.expression = exp;
    }

    public PrgState execute(PrgState state) throws InterpreterException {
        IList<IValue> output = state.getOutput();
        output.add(expression.eval(state.getSymTable(), state.getHeap()));
        state.setOutput(output);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new PrintStmt(expression.deepCopy());
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws InterpreterException {
        expression.typeCheck(typeEnv);
        return typeEnv;
    }

    @Override
    public String toString() {
        return "Print(" + expression.toString() + ")";
    }
}
