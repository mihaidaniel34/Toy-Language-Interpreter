package Model.adt;

import Model.Exceptions.InterpreterException;

import java.util.Map;
import java.util.Set;

public interface IDict<T1,T2>{

    void add(T1 v1, T2 v2);
    void update(T1 v1, T2 v2) throws InterpreterException;
    T2 lookup(T1 id);
    boolean isDefined(T1 id);
    String toString();
    void delete(T1 v1) throws InterpreterException;
    Set<T1> getKeys();
    Map<T1, T2> getContent();
    IDict<T1,T2> deepCopy();
}
