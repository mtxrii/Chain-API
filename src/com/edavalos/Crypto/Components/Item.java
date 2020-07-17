package com.edavalos.Crypto.Components;

public class Item<T> {
    private final int id;
    private final T contents;

    public Item(int id, T contents) {
        this.id = id;
        this.contents = contents;
    }

    public int getId() {
        return id;
    }

    public T getContents() {
        return contents;
    }
}
