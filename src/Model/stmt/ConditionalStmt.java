package Model.stmt;

import Model.Exceptions.InterpreterException;
import Model.Exceptions.TypeException;
import Model.PrgState;
import Model.adt.IDict;
import Model.exp.Exp;
import Model.types.BoolType;
import Model.types.IType;

public class ConditionalStmt implements IStmt {

    private String var;
    private Exp exp1, exp2, exp3;

    public ConditionalStmt(String var, Exp exp1, Exp exp2, Exp exp3) {
        this.var = var;
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.exp3 = exp3;
    }

    @Override
    public PrgState execute(PrgState state) throws InterpreterException {
        IStmt ifStmt = new IfStmt(exp1, new AssignStmt(var, exp2), new AssignStmt(var, exp3));
        state.getStk().push(ifStmt);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new ConditionalStmt(var, exp1, exp2, exp3);
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws InterpreterException {
        IType varType = typeEnv.lookup(var);
        if(!exp1.typeCheck(typeEnv).equals(new BoolType()))
            throw new TypeException("(conditional) " + exp1 + " does not evaluate to a boolean.");
        if(!((varType.equals(exp2.typeCheck(typeEnv))&& exp3.typeCheck(typeEnv).equals(varType))))
            throw new TypeException("(conditional) variable " + var + "and expressions " + exp2 + " and " + exp3 + " do not evaluate to the same type." );
        return typeEnv;
    }

    @Override
    public String toString() {
        return var + "=" + exp1+"?"+exp2+":"+exp3;
    }
}
