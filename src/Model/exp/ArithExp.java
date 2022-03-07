package Model.exp;

import Model.Exceptions.DivisionByZeroException;
import Model.Exceptions.InterpreterException;
import Model.Exceptions.InvalidOperatorException;
import Model.Exceptions.TypeException;
import Model.adt.IDict;
import Model.adt.IHeap;
import Model.types.IType;
import Model.types.IntType;
import Model.value.IValue;
import Model.value.IntValue;

public class ArithExp extends Exp {
    char op;
    Exp e1, e2;

    //constructor
    public ArithExp(char op, Exp e1, Exp e2) {
        this.op = op;
        this.e1 = e1;
        this.e2 = e2;
    }

    public IValue eval(IDict<String, IValue> symTable, IHeap heap) throws InterpreterException {
        IValue v1, v2;
        v1 = e1.eval(symTable, heap);
        if (v1.getType().equals(new IntType())) {
            v2 = e2.eval(symTable, heap);
            if (v2.getType().equals(new IntType())) {
                IntValue i1 = (IntValue) v1;
                IntValue i2 = (IntValue) v2;
                switch (op) {
                    case '+' -> {
                        return (new IntValue(i1.getValue() + i2.getValue()));
                    }
                    case '-' -> {
                        return (new IntValue(i1.getValue() - i2.getValue()));
                    }
                    case '*' -> {
                        return (new IntValue(i1.getValue() * i2.getValue()));
                    }
                    case '/' -> {
                        if (i2.getValue() == 0) {
                            throw new DivisionByZeroException("Division by zero.");
                        }
                        return (new IntValue(i1.getValue() / i2.getValue()));
                    }
                    default -> throw new InvalidOperatorException("Operator " + op + "is invalid.");
                }

            } else
                throw new TypeException("Second operand is not an integer.");
        } else
            throw new TypeException("First operand is not an integer.");
    }

    public char getOp() {
        return this.op;
    }

    public Exp getFirst() {
        return this.e1;
    }

    public Exp getSecond() {
        return this.e2;
    }

    public String toString() {
        return e1.toString() + op + e2.toString();
    }

    @Override
    public Exp deepCopy() {
        return new ArithExp(op, e1.deepCopy(), e2.deepCopy());
    }

    @Override
    public IType typeCheck(IDict<String, IType> typeEnv) throws InterpreterException {
        IType type1, type2;
        type1 = e1.typeCheck(typeEnv);
        type2 = e2.typeCheck(typeEnv);
        if (type1.equals(new IntType())) {
            if (type2.equals(new IntType())) {
                return new IntType();
            } else
                throw new TypeException("Second operand is not an integer.");
        } else
            throw new TypeException("First operand is not an integer.");

    }
}
