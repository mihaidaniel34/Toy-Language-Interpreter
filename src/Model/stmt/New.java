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


public class New implements IStmt {

    String varName;
    Exp expression;

    public New(String varName, Exp expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws InterpreterException {
        IDict<String, IValue> symTable = state.getSymTable();
        IHeap heap = state.getHeap();
        if (!symTable.isDefined(varName))
            throw new DeclarationException("Variable " + varName + " not declared.");
        IValue t1 = symTable.lookup(varName);
        if (!(t1.getType() instanceof RefType))
            throw new TypeException("Variable should be a of type Ref");
        IValue value = expression.eval(symTable, heap);
        RefValue refValue = (RefValue) t1;
        if (!refValue.getLocationType().equals(value.getType()))
            throw new TypeException("Type mismatch occurred during heap allocation.");
        Integer position = state.getHeap().add(value);
        symTable.update(varName, new RefValue(position, refValue.getLocationType()));
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new New(varName, expression);
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws InterpreterException {
        IType typeVar = typeEnv.lookup(varName);
        IType typeExp = expression.typeCheck(typeEnv);
        if(typeVar.equals(new RefType(typeExp)))
            return typeEnv;
        else
            throw new TypeException("New: variable " + varName + " and expression " + expression + " are of different types.");

    }

    @Override
    public String toString() {
        return "New{" + varName + "," + expression + '}';
    }
}
