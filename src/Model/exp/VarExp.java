package Model.exp;
import Model.Exceptions.InterpreterException;
import Model.adt.IDict;
import Model.adt.IHeap;
import Model.types.IType;
import Model.value.IValue;

public class VarExp extends Exp{
    String id;

    public VarExp(String id){
        this.id = id;
    }

    public IValue eval(IDict<String,IValue> symTable, IHeap heap) throws InterpreterException {
        return symTable.lookup(id);
    }

    public String toString() {return id;}

    @Override
    public Exp deepCopy() {
        return new VarExp(id);
    }

    @Override
    public IType typeCheck(IDict<String, IType> typeEnv) throws InterpreterException {
        return typeEnv.lookup(id);
    }

}
