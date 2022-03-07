package Model.stmt;

import Model.Exceptions.InterpreterException;
import Model.Exceptions.TypeException;
import Model.PrgState;
import Model.adt.IDict;
import Model.exp.Exp;
import Model.exp.RelExp;
import Model.exp.VarExp;
import Model.types.IType;
import Model.types.IntType;

public class ForStmt implements IStmt{

    String var;
    Exp exp1,exp2,exp3;
    IStmt stmt;

    public ForStmt(String var,Exp exp1, Exp exp2, Exp exp3, IStmt stmt){
        this.var = var;
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.exp3 = exp3;
        this.stmt = stmt;
    }

    @Override
    public PrgState execute(PrgState state) throws InterpreterException {
        IStmt forStmt = new CompStmt(new AssignStmt(var, exp1),new WhileStmt(new RelExp("<", new VarExp(var), exp2), new CompStmt(stmt, new AssignStmt(var, exp3))));
        state.getStk().push(forStmt);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new ForStmt(var, exp1, exp2, exp3, stmt);
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws InterpreterException {
        IType typeVar = typeEnv.lookup(var);
        IType typeExp1 = exp1.typeCheck(typeEnv);
        IType typeExp2 = exp1.typeCheck(typeEnv);
        IType typeExp3 = exp1.typeCheck(typeEnv);
        if(typeVar.equals(new IntType()) && (typeExp1.equals(typeVar) && typeExp2.equals(typeVar) && typeExp3.equals(typeVar))) {
            return typeEnv;
        }
        else throw new TypeException("(for) The expressions do not evaluate to integers.");
    }

    @Override
    public String toString() {
        return "for(" +var + "=" + exp1 + ";"+ var + "<" + exp2 + ";" + var+"=" + exp3 + ") " + stmt ;
    }
}
