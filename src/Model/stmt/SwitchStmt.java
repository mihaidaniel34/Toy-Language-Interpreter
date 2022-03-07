package Model.stmt;

import Model.Exceptions.InterpreterException;
import Model.Exceptions.TypeException;
import Model.PrgState;
import Model.adt.IDict;
import Model.exp.Exp;
import Model.exp.RelExp;
import Model.types.BoolType;
import Model.types.IType;

public class SwitchStmt implements IStmt{
    Exp exp, exp1, exp2;
    IStmt stmt1, stmt2, stmt3;

    public SwitchStmt(Exp exp, Exp exp1, IStmt stmt1, Exp exp2, IStmt stmt2, IStmt stmt3){
        this.exp = exp;
        this.exp1 = exp1;
        this.stmt1 = stmt1;
        this.exp2 = exp2;
        this.stmt2 = stmt2;
        this.stmt3 = stmt3;
    }

    @Override
    public PrgState execute(PrgState state) throws InterpreterException {
        IStmt ifStmt = new IfStmt(new RelExp("==", exp, exp1),stmt1, new IfStmt(new RelExp("==", exp1, exp2), stmt2, stmt3));
        state.getStk().push(ifStmt);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new SwitchStmt(exp,exp1,stmt1,exp2,stmt2,stmt3);
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws InterpreterException {
        IType type = exp.typeCheck(typeEnv);
        IType type1 = exp1.typeCheck(typeEnv);
        IType type2 = exp2.typeCheck(typeEnv);
        if(!((type.equals(type1) && type.equals(type2))))
            throw new TypeException("(switch) The expressions do not evaluate to the same type");
        stmt1.typeCheck(typeEnv);
        stmt2.typeCheck(typeEnv);
        stmt3.typeCheck(typeEnv);
        return typeEnv;
    }

    @Override
    public String toString() {
        return "switch("  + exp+ ") (case " + exp1 + ": " + stmt1 + ")(case "+ exp2 + ": " + stmt2 + ")(default: " + stmt3+")" ;
    }
}
