package Model.stmt;

import Model.Exceptions.InterpreterException;
import Model.Exceptions.TypeException;
import Model.PrgState;
import Model.adt.IDict;
import Model.exp.Exp;
import Model.exp.NotExp;
import Model.types.BoolType;
import Model.types.IType;


public class RepeatStmt implements IStmt{
    IStmt stmt1;
    Exp exp2;

    public RepeatStmt(IStmt stmt1, Exp exp2){
        this.stmt1 = stmt1;
        this.exp2 = exp2;
    }

    @Override
    public PrgState execute(PrgState state) throws InterpreterException {
        IStmt repeatStmt = new CompStmt(stmt1, new WhileStmt(new NotExp(exp2), stmt1));
        state.getStk().push(repeatStmt);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new RepeatStmt(stmt1, exp2);
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws InterpreterException {
        IType typeExp = exp2.typeCheck(typeEnv);
        if(typeExp.equals(new BoolType())){
            stmt1.typeCheck(typeEnv.deepCopy());
            return typeEnv;
        }
        else
            throw new TypeException("(repeat) Expression " + exp2 + " does not evaluate to a boolean type.");

    }

    @Override
    public String toString() {
        return "repeat{" + stmt1 +  "}until(" + exp2 + ")";
    }
}
