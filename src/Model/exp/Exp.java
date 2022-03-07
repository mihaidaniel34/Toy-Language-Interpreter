package Model.exp;
import Model.Exceptions.InterpreterException;
import Model.adt.IDict;
import Model.adt.IHeap;
import Model.types.IType;
import Model.value.IValue;

public abstract class Exp {

    public abstract IValue eval(IDict<String, IValue> symTable, IHeap heap) throws InterpreterException;
    public abstract String toString();
    public abstract Exp deepCopy();
    public abstract IType typeCheck(IDict<String, IType> typeEnv) throws InterpreterException;
}
