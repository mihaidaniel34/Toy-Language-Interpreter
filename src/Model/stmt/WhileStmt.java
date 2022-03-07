package Model.stmt;

import Model.Exceptions.InterpreterException;
import Model.Exceptions.TypeException;
import Model.PrgState;
import Model.adt.IDict;
import Model.exp.Exp;
import Model.types.BoolType;
import Model.types.IType;
import Model.value.BoolValue;
import Model.value.IValue;

public class WhileStmt implements IStmt {
    IStmt statement;
    Exp expression;

    public WhileStmt(Exp expression, IStmt statement) {
        this.statement = statement;
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws InterpreterException {
        IValue value = expression.eval(state.getSymTable(), state.getHeap());
        if (!value.getType().equals(new BoolType()))
            throw new TypeException("Expression " + expression + " doesn't evaluate to a BoolType.");
        BoolValue boolValue = (BoolValue) value;
        if (boolValue.getValue()) {
            state.getStk().push(this);
            state.getStk().push(statement);
        }
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new WhileStmt(expression, statement);
    }

    @Override
    public IDict<String, IType> typeCheck(IDict<String, IType> typeEnv) throws InterpreterException {
        IType typeExp = expression.typeCheck(typeEnv);
        if (typeExp.equals(new BoolType())) {
            statement.typeCheck(typeEnv.deepCopy());
            return typeEnv;
        } else
            throw new TypeException("Expression " + expression + " does not evaluate to a boolean type.");

    }

    @Override
    public String toString() {
        return "while(" + expression + "){" + statement + "}";
    }
}
