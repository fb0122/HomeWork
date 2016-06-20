package com.example.fb0122.shanbaywork;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by fb0122 on 2016/6/21.
 */
public class ListFactory<T> {

    int count;

    public ListFactory(int count){
        this.count = count;
    }

    public HashMap<Integer,ArrayList<T>> getList(){
        HashMap<Integer,ArrayList<T>> map = new HashMap<>();
        for (int i = 0;i < count;i++){
            map.put(i,new ArrayList<T>());
        }
        return map;
    }

}
