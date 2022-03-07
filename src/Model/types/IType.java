package Model.types;

import Model.value.IValue;

public interface IType {
    IValue defaultValue();
    IType deepCopy();
    boolean equals(Object o);
}
