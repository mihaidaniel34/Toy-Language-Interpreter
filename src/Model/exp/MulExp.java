package Model.exp;

import Model.Exceptions.InterpreterException;
import Model.Exceptions.TypeException;
import Model.adt.IDict;
import Model.adt.IHeap;
import Model.types.IType;
import Model.types.IntType;
import Model.value.IValue;

public class MulExp extends Exp{
    private Exp exp1, exp2;

    public MulExp(Exp exp1, Exp exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
    }


    @Override
    public IValue eval(IDict<String, IValue> symTable, IHeap heap) throws InterpreterException {
        return new ArithExp('-', new ArithExp('*', exp1,exp2), new ArithExp('+', exp1,exp2)).eval(symTable,heap);
    }

    @Override
    public String toString() {
        return "MUL("+exp1+","+exp2+")";
    }

    @Override
    public Exp deepCopy() {
        return new MulExp(exp1,exp2);
    }

    @Override
    public IType typeCheck(IDict<String, IType> typeEnv) throws InterpreterException {
        if(exp1.typeCheck(typeEnv).equals(new IntType()) && exp2.typeCheck(typeEnv).equals(new IntType()))
            return new IntType();
        throw new TypeException("(mul) the expressions do not evaluate to int types.");
    }
}
