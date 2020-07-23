package com.edavalos.Crypto.Chains;

import com.edavalos.Crypto.Chain;
import com.edavalos.Crypto.Components.Block;
import com.edavalos.Crypto.Utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class BlockChain<T> implements Chain<T> {
    private final Utility.HashTypes hashType;
    private final List<Block<T>> blocks;
    private Block<T> current;

    public BlockChain(String genesisSeed, Utility.HashTypes hashType) {
        byte[] genesisHash = Utility.byteHash(genesisSeed, hashType);
        blocks = new ArrayList<>();
        current = new Block<>(0, genesisHash);
        this.hashType = hashType;
    }

    @Override
    public boolean nextBlock() {
        current.seal(hashType);
        if (current.getProofHash() == null) return false;

        blocks.add(current);
        current = new Block<>(blocks.size(), current.getProofHash());
        return true;
    }

    @Override
    public boolean add(T item) {
        return current.addItem(item);
    }

    @Override
    public boolean add(T[] items) {
        if (!current.addItem(items[0])) return false;
        for (int i = 1; i < items.length; i++) {
            current.addItem(items[i]);
        }
        return true;
    }

    @Override
    public int size() {
        return blocks.size() + 1;
    }

    @Override
    public int totalItems() {
        int counter = 0;
        for (Block<T> block : blocks) {
            counter += block.size();
        }
        return counter + current.size();
    }

    @Override
    public void discardBlock() {
        current = new Block<>(blocks.size(), current.getPriorHash());
    }

    @Override
    public int find(T item) {
        for (Block<T> block : blocks) {
            if (block.contains(item)) return block.getId();
        }
        if (current.contains(item)) return current.getId();
        return -1;
    }

    @Override
    public int soonestTo(Date timestamp) {
        long diff = Math.abs(timestamp.getTime() - current.getTimestamp().getTime());
        Block<T> comp = current;

        for (Block<T> block : blocks) {
            long newDiff = Math.abs(timestamp.getTime() - block.getTimestamp().getTime());
            if (newDiff < diff) {
                diff = newDiff;
                comp = block;
            }
        }

        return comp.getId();
    }

    @Override
    public T[] getContents(int blockID) {
        T[] contents;
        try {
            Block<T> b = blocks.get(blockID);
            b.resize(T[].class);
            contents = b.getItems();
        }
        catch (IndexOutOfBoundsException e) {
            if (current.getId() == blockID) {
                current.resize(T[].class);
                return current.getItems();
            }
            return null;
        }
        return contents;
    }

    @Override
    public boolean isCurrentEmpty() {
        return current.isEmpty();
    }

    @Override
    public boolean isEmpty() {
        for (Block<T> block : blocks) {
            if (!block.isEmpty()) return false;
        }
        return this.isCurrentEmpty();
    }

    @Override
    public T[][] toArray() {
        T[][] allBlocks = new T[blocks.size() + 1][];
        for (int i = 0; i < blocks.size(); i++) {
            Block<T> b = blocks.get(i);
            b.resize(T[].class);
            allBlocks[i] = b.getItems();
        }
        current.resize(T[].class);
        allBlocks[blocks.size()] = current.getItems();
        return allBlocks;
    }

    @Override
    public String toString() {
        String border = "==================================================\n" +
                        "==================================================\n";
        StringBuilder chain = new StringBuilder();
        for (Block<T> block : blocks) {
            chain.append(border).append(block.toString()).append("\n");
        }
        return chain + border + current.toString() + "\n" + border;
    }
}
