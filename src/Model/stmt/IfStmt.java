package Model.stmt;

import Model.Exceptions.InterpreterException;
import Model.Exceptions.TypeException;
import Model.PrgState;
import Model.adt.IDict;
import Model.adt.IHeap;
import Model.adt.IStack;
import Model.exp.Exp;
import Model.types.BoolType;
import Model.types.IType;
import Model.value.BoolValue;
import Model.value.IValue;

public class IfStmt implements IStmt{
    Exp exp;
    IStmt thenS;
    IStmt elseS;

    public IfStmt(Exp exp, IStmt thenS, IStmt elseS){
        this.exp = exp;
        this.thenS = thenS;
        this.elseS = elseS;
    }
    @Override
    public PrgState execute(PrgState state) throws InterpreterException {
        IDict<String, IValue> symTable = state.getSymTable();
        IStack<IStmt> stk = state.getStk();
        IHeap heap = state.getHeap();
        IValue value = exp.eval(symTable, heap);
        if (value.getType().equals(new BoolType())){
            BoolValue cond = (BoolValue) value;
            if(cond.getValue()){
                stk.push(thenS);
            }
                else
                    stk.push(elseS);
        }
        else
            throw new TypeException(value + " is not a boolean");
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new IfStmt(exp.deepCopy(), thenS.deepCopy(), elseS.deepCopy());
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws InterpreterException {
        IType typeExp = exp.typeCheck(typeEnv);
        if(typeExp.equals(new BoolType())){
            thenS.typeCheck(typeEnv.deepCopy());
            elseS.typeCheck(typeEnv.deepCopy());
            return typeEnv;
        }
        else
            throw new TypeException("The if condition is not of type bool.");

    }

    @Override
    public String toString() {
        return "(IF(" + exp.toString() + ")THEN(" + thenS.toString() + ")ELSE(" + elseS.toString()+"))";
    }

}
