package Model.types;

import Model.value.IValue;
import Model.value.StringValue;

public class StringType implements IType {
    @Override
    public IValue defaultValue() {
        return new StringValue("");
    }

    @Override
    public IType deepCopy() {
        return new StringType();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass() == this.getClass();
    }

    @Override
    public String toString() {
        return "string";
    }
}
