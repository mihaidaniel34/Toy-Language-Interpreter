package Model.exp;

import Model.Exceptions.DeclarationException;
import Model.Exceptions.InterpreterException;
import Model.Exceptions.TypeException;
import Model.adt.IDict;
import Model.adt.IHeap;
import Model.types.IType;
import Model.types.RefType;
import Model.value.IValue;
import Model.value.RefValue;


public class readHeap extends Exp {
    Exp expression;
    public readHeap(Exp expression){
        this.expression = expression;
    }


    @Override
    public IValue eval(IDict<String, IValue> symTable, IHeap heap) throws InterpreterException {
        IValue value = expression.eval(symTable, heap);
        if(!(value.getType() instanceof RefType))
            throw new TypeException("Expression must evaluate to a RefValue.");
        RefValue refValue = (RefValue) value;
        Integer address = refValue.getAddress();
        if(!heap.isDefined(address)){
            throw new DeclarationException("Address " +address + " is empty." );
        }
        return heap.lookup(address);
    }

    @Override
    public String toString() {
        return "ReadHeap{" + expression +"}";
    }

    @Override
    public Exp deepCopy() {
        return new readHeap(expression);
    }

    @Override
    public IType typeCheck(IDict<String, IType> typeEnv) throws InterpreterException {
        IType type = expression.typeCheck(typeEnv);
        if(type instanceof RefType refType){
            return refType.getInner();
        }
        else
            throw new TypeException("The readHeap argument is not a RefType");
    }

}
