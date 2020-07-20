package com.edavalos.Crypto;

import java.util.Date;

public interface Chain<T> {

    // Seals current block and creates new one. Returns false if failed
    boolean nextBlock();

    // Adds something to the current block. Returns false if failed
    boolean add(T item);
    boolean add(T[] items);

    // Returns number of blocks
    int size();

    // Returns total number of items in every block
    int totalItems();

    // Discards current block and everything in it, and creates a new one to replace it
    void discardBlock();

    // If item is in this chain, returns the first block id of which it is found. Otherwise returns -1
    int contains(T item);

    // Returns id of block with timestamp nearest to the one provided, or -1 if chain is empty
    int soonestTo(Date timestamp);

    // Returns a list of every item in block of specified id. Returns null if id is out of range
    T getContents(int blockID);

    // Checks if current block is empty
    boolean isCurrentEmpty();

    // Checks if entire chain is empty
    boolean isEmpty();

    // Converts entire chain to a 2D array
    T[][] toArray();

}
