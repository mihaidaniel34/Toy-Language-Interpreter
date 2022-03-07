package Model.adt;

import Model.Exceptions.DictionaryException;
import Model.value.IValue;

import java.util.Map;

public interface IHeap {

    IValue lookup(Integer address);
    Map<Integer, IValue> getContent();
    Integer add(IValue value);
    void update(Integer position, IValue value) throws DictionaryException;
    Integer getFreeLocation();
    void remove(Integer key) throws DictionaryException;
    boolean isDefined(Integer key);
    String toString();
    void setContent(Map<Integer, IValue> content);
}
