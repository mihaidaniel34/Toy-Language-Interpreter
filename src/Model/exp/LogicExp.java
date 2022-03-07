package Model.exp;

import Model.Exceptions.InterpreterException;
import Model.Exceptions.InvalidOperatorException;
import Model.Exceptions.TypeException;
import Model.adt.IDict;
import Model.adt.IHeap;
import Model.types.BoolType;
import Model.types.IType;
import Model.types.IntType;
import Model.value.BoolValue;
import Model.value.IValue;

public class LogicExp extends Exp {
    Exp e1;
    Exp e2;
    String operator;
    public LogicExp(String operator, Exp e1, Exp e2){
        this.operator = operator;
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    public IValue eval(IDict<String, IValue> symTable, IHeap heap) throws InterpreterException {
        IValue v1, v2;
        v1 = e1.eval(symTable, heap);
        if(v1.getType().equals(new BoolType())){
            v2 = e2.eval(symTable,heap);
            if(v2.getType().equals(new BoolType())){
                BoolValue b1 = (BoolValue) v1;
                BoolValue b2 = (BoolValue) v2;
                if(operator.equals("and")){
                    return (new BoolValue(b1.getValue() && b2.getValue()));
                }
                else if(operator.equals("or"))
                    return (new BoolValue(b1.getValue() || b2.getValue()));
                else
                    throw new InvalidOperatorException("Invalid operator");
            }
            else throw new TypeException("second operand is not a boolean");
        }
        else throw new TypeException("first operand is not a boolean");
    }

    @Override
    public String toString() {
        return e1.toString() +  operator + e2.toString();
    }

    @Override
    public LogicExp deepCopy() {
        return new LogicExp(operator, e1.deepCopy(), e2.deepCopy());
    }

    @Override
    public IType typeCheck(IDict<String, IType> typeEnv) throws InterpreterException {
        IType type1, type2;
        type1 = e1.typeCheck(typeEnv);
        type2 = e2.typeCheck(typeEnv);
        if (type1.equals(new BoolType())) {
            if (type2.equals(new BoolType())) {
                return new BoolType();
            } else
                throw new TypeException("Second operand is not a boolean.");
        } else
            throw new TypeException("First operand is not a boolean.");

    }

}
