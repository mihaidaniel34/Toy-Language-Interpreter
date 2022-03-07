package Model.types;

import Model.value.IValue;
import Model.value.RefValue;

import java.util.Objects;

public class RefType implements IType {

    IType inner;
    public RefType(IType inner){
        this.inner = inner;
    }

    public IType getInner() {
        return inner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefType refType = (RefType) o;
        return Objects.equals(inner, refType.inner);
    }

    @Override
    public String toString() {
        return "Ref(" + inner + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(inner);
    }

    @Override
    public IValue defaultValue() {
        return new RefValue(0, inner);
    }

    @Override
    public IType deepCopy() {
        return new RefType(inner);
    }
}
