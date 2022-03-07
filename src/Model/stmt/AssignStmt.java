package Model.stmt;

import Model.Exceptions.InterpreterException;
import Model.Exceptions.TypeException;
import Model.PrgState;
import Model.adt.IDict;
import Model.adt.IHeap;
import Model.exp.Exp;
import Model.types.IType;
import Model.value.IValue;

public class AssignStmt implements IStmt{

    String id;
    Exp expression;

    public AssignStmt(String id, Exp exp){
        this.id = id;
        this.expression = exp;
    }

    @Override
    public String toString(){
        return this.id + "=" + this.expression.toString();
    }

    public PrgState execute(PrgState state) throws InterpreterException {
        IDict<String, IValue> symTable = state.getSymTable();
        IHeap heap = state.getHeap();
        if (symTable.isDefined(id)){
            IValue val = expression.eval(symTable, heap);
            IType typeId = symTable.lookup(id).getType();
            if(val.getType().equals(typeId)){
                symTable.update(id, val);
            }
            else throw new TypeException("declared type of variable " +id + " and type of the assigned expression do not match");
        }
        else throw new TypeException("the used variable " + id + " was not declared before.");
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new AssignStmt(id, expression.deepCopy());
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws InterpreterException {
        IType typeVar = typeEnv.lookup(id);
        IType typeExp = expression.typeCheck(typeEnv);
        if(typeVar.equals(typeExp))
            return typeEnv;
        else
            throw new TypeException("Assignment: declared type of variable " +id + " and type of the assigned expression do not match");
    }
}
