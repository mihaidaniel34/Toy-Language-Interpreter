package Model.exp;

import Model.Exceptions.InterpreterException;
import Model.adt.IDict;
import Model.adt.IHeap;
import Model.types.IType;
import Model.value.IValue;

public class ValueExp extends Exp {
    IValue value;
   public ValueExp(IValue value){
       this.value = value;
   }

    @Override
    public IValue eval(IDict<String, IValue> symTable, IHeap heap) throws InterpreterException {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public Exp deepCopy() {
        return new ValueExp(value.deepCopy());
    }

    @Override
    public IType typeCheck(IDict<String, IType> typeEnv) throws InterpreterException {
        return value.getType();
    }
}
