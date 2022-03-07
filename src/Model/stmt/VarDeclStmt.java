package Model.stmt;


import Model.Exceptions.DeclarationException;
import Model.Exceptions.InterpreterException;
import Model.PrgState;
import Model.adt.IDict;
import Model.types.IType;
import Model.value.IValue;

public class VarDeclStmt implements IStmt{
    String name;
    IType type;

    public VarDeclStmt(String name, IType type){
        this.name = name;
        this.type = type;
    }


    @Override
    public PrgState execute(PrgState state) throws InterpreterException {
        IDict<String, IValue> symTable = state.getSymTable();
        if( !symTable.isDefined(name)){
            symTable.add(name, type.defaultValue());
        }
        else
            throw new DeclarationException("variable " + name + " is already declared.");
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new VarDeclStmt(name, type.deepCopy());
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws InterpreterException {
        typeEnv.add(name, type);
        return typeEnv;
    }

    @Override
    public String toString() {
        return type.toString() + " " + name;
    }
}
