package com.smzdm.model;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Changdy on 2018/2/2.
 */
@Data
public class SimplePageContent<T> {
    private Long recordSize;
    private List<T> list;

    public SimplePageContent() {

    }

    public SimplePageContent(Long recordSize) {
        this.recordSize = recordSize;
    }

    public SimplePageContent(Long recordSize, List<T> list) {
        this.recordSize = recordSize;
        this.list = list;
    }

    public void addItem(T item) {
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(item);
    }

    public T getByIndex(int index) {
        return list.get(index);
    }

    public void addAll(List<T> list) {
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
        this.list.addAll(list);
    }
}