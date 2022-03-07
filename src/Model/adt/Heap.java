package Model.adt;

import Model.Exceptions.DictionaryException;
import Model.value.IValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


public class Heap implements IHeap{

    Map<Integer, IValue> map;
    Stack<Integer> freePositions;

    public Heap(){
        this.map = new HashMap<>();
        this.freePositions = new Stack<>();
        freePositions.add(1);
    }

    @Override
    public IValue lookup(Integer address) {
        return map.get(address);
    }

    @Override
    public Map<Integer, IValue> getContent() {
        return map;
    }

    @Override
    public Integer add(IValue value) {
        Integer position = getFreeLocation();
        map.put(position,value);
        return position;
    }

    @Override
    public void update(Integer position, IValue value) throws DictionaryException {
        if(!map.containsKey(position))
            throw new DictionaryException("Key not found.");
        map.replace(position,value);
    }

    @Override
    public Integer getFreeLocation() {
        Integer toReturn = freePositions.pop();
        if(freePositions.empty())
            freePositions.add(toReturn+1);
        return toReturn;
    }

    @Override
    public void remove(Integer key) throws DictionaryException {
        if(!map.containsKey(key))
            throw new DictionaryException("Key not found.");
        map.remove(key);
        freePositions.add(key);
    }

    @Override
    public boolean isDefined(Integer key) {
        return map.containsKey(key);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Dict{");
        for(Integer key: map.keySet()){
            stringBuilder.append(key).append("->").append(map.get(key)).append(",");
        }
        int index = stringBuilder.lastIndexOf(",");
        if(index!=-1)
            stringBuilder.deleteCharAt(index);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    @Override
    public void setContent(Map<Integer, IValue> content) {
        this.map = content;
    }
}
