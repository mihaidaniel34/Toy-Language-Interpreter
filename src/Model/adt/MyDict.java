package Model.adt;

import Model.Exceptions.DictionaryException;
import Model.Exceptions.InterpreterException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MyDict<T1, T2> implements IDict<T1, T2> {
    Map<T1, T2> dictionary;

    public MyDict() {
        dictionary = new HashMap<>();
    }


    @Override
    public void add(T1 v1, T2 v2){
        dictionary.put(v1, v2);
    }

    @Override
    public void update(T1 v1, T2 v2) throws DictionaryException {
        if (!dictionary.containsKey(v1))
            throw new DictionaryException("Key doesnt exist.");
        dictionary.replace(v1, v2);
    }

    @Override
    public T2 lookup(T1 id) {
        return dictionary.get(id);
    }

    @Override
    public boolean isDefined(T1 id) {
        return dictionary.containsKey(id);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Dict: {");
        if (dictionary.isEmpty())
            return stringBuilder + "}";
        Iterator<Map.Entry<T1, T2>> it = dictionary.entrySet().iterator();
        Map.Entry<T1, T2> pair = it.next();
        stringBuilder.append(pair.getKey()).append(":").append(pair.getValue());
        while (it.hasNext()) {
            pair = it.next();
            stringBuilder.append(",").append(pair.getKey()).append(":").append(pair.getValue());
        }
        return stringBuilder + "}";
    }

    @Override
    public void delete(T1 v1) throws InterpreterException {
        if(!dictionary.containsKey(v1))
            throw new InterpreterException("Key does not exists");
        dictionary.remove(v1);
    }

    @Override
    public Set<T1> getKeys() {
        return dictionary.keySet();
    }

    @Override
    public Map<T1, T2> getContent() {
        return dictionary;
    }

    @Override
    public IDict<T1, T2> deepCopy() {
        IDict<T1,T2> copy = new MyDict<>();
        for(T1 key: dictionary.keySet())
            copy.add(key, dictionary.get(key));
        return copy;
    }
}
