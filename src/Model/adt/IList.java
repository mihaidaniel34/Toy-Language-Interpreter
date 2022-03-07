package Model.adt;

import Model.Exceptions.ListException;

import java.util.List;

public interface IList<T> {
    T get(int index) throws ListException;
    int size();
    void add(T v);
    String toString();
    boolean empty();
    void clear();
    List<T> getList();
}
