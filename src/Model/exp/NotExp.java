package Model.exp;

import Model.Exceptions.InterpreterException;
import Model.adt.IDict;
import Model.adt.IHeap;
import Model.types.IType;
import Model.value.BoolValue;
import Model.value.IValue;

public class NotExp extends Exp{
    Exp expression;

    public NotExp(Exp expression){
        this.expression = expression;
    }
    @Override
    public IValue eval(IDict<String, IValue> symTable, IHeap heap) throws InterpreterException {
        BoolValue value = (BoolValue) expression.eval(symTable, heap);
        return new BoolValue(!value.getValue());
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public Exp deepCopy() {
        return null;
    }

    @Override
    public IType typeCheck(IDict<String, IType> typeEnv) throws InterpreterException {
        return null;
    }
}
