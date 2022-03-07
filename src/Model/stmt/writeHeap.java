package Model.stmt;

import Model.Exceptions.DeclarationException;
import Model.Exceptions.InterpreterException;
import Model.Exceptions.TypeException;
import Model.PrgState;
import Model.adt.IDict;
import Model.adt.IHeap;
import Model.exp.Exp;
import Model.types.IType;
import Model.types.RefType;
import Model.value.IValue;
import Model.value.RefValue;

public class writeHeap implements IStmt {
    String varName;
    Exp expression;

    public writeHeap(String varName, Exp expression) {
        this.varName = varName;
        this.expression = expression;
    }


    @Override
    public PrgState execute(PrgState state) throws InterpreterException {
        IHeap heap = state.getHeap();
        IDict<String, IValue> symTable = state.getSymTable();
        if (!symTable.isDefined(varName))
            throw new DeclarationException("Variable " + varName + " is not declared.");
        IValue value = symTable.lookup(varName);
        if (!(value.getType() instanceof RefType))
            throw new TypeException("Variable " + varName + " is not of RefType.");
        RefValue refValue = (RefValue) value;
        Integer address = refValue.getAddress();
        if (!(heap.isDefined(address)))
            throw new DeclarationException("Address " + address + " is empty.");
        IValue value1 = expression.eval(symTable, heap);
        if (!value1.getType().equals(refValue.getLocationType()))
            throw new TypeException("Type mismatch: " + value1.getType() + " and " + refValue.getType());
        heap.update(address, value1);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new writeHeap(varName, expression);
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws InterpreterException {
        IType typeVar = typeEnv.lookup(varName);
        IType typeExp = expression.typeCheck(typeEnv);
        if (typeVar.equals(new RefType(typeExp)))
            return typeEnv;
        else
            throw new TypeException("writeHeap: Variable " + varName + " and expression " + expression + " are of different types.");

    }

    @Override
    public String toString() {
        return "WriteHeap{" + varName + ", " + expression + "}";
    }
}
