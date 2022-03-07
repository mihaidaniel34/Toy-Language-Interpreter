package Model.adt;

import Model.Exceptions.ListException;

import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements IList<T> {
    List<T> list;

    public MyList(){
        this.list = new ArrayList<T>();
    }

    @Override
    public T get(int index) throws ListException {
        if(index < 0 || index >= list.size())
            throw new ListException("Invalid index");
        return list.get(index);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public void add(T v) {
        list.add(v);
    }

    @Override
    public boolean empty() {
        return this.list.isEmpty();
    }

    @Override
    public void clear(){
        this.list.clear();
    }

    @Override
    public List<T> getList() {
        return list;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("List: [");
        if(list.isEmpty())
            return stringBuilder + "]";
        stringBuilder.append(list.get(0));
        for(int i=1;i< list.size();i++)
            stringBuilder.append(",").append(list.get(i));
        return stringBuilder + "]";
    }
}
