package com.edavalos.Crypto.Chains;

import com.edavalos.Crypto.Chain;
import com.edavalos.Crypto.Components.Block;
import com.edavalos.Crypto.Utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class StringChain implements Chain<String> {
    private final byte[] genesisHash;
    private final Utility.HashTypes hashType;
    private final List<Block<String>> blocks;
    private Block<String> current;

    public StringChain(String genesisSeed, Utility.HashTypes hashType) {
        genesisHash = Utility.byteHash(genesisSeed, hashType);
        blocks = new ArrayList<>();
        current = new Block<>(0, genesisHash);
        this.hashType = hashType;
    }

    @Override
    public boolean nextBlock() {
        if (current.getProofHash() == null) return false;

        current.seal(hashType);
        blocks.add(current);
        current = new Block<>(blocks.size(), current.getProofHash());
        return true;
    }

    @Override
    public boolean add(String item) {
        return current.addItem(item);
    }

    @Override
    public boolean add(String[] items) {
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
        for (Block<String> block : blocks) {
            counter += block.size();
        }
        return counter + current.size();
    }

    @Override
    public void discardBlock() {
        current = new Block<>(blocks.size(), current.getProofHash());
    }

    @Override
    public int find(String item) {
        for (Block<String> block : blocks) {
            if (block.contains(item)) return block.getId();
        }
        if (current.contains(item)) return current.getId();
        return -1;
    }

    @Override
    public int soonestTo(Date timestamp) {
        long diff = Math.abs(timestamp.getTime() - current.getTimestamp().getTime());
        Block<String> comp = current;

        for (Block<String> block : blocks) {
            long newDiff = Math.abs(timestamp.getTime() - block.getTimestamp().getTime());
            if (newDiff < diff) {
                diff = newDiff;
                comp = block;
            }
        }

        return comp.getId();
    }

    @Override
    public String[] getContents(int blockID) {
        String[] contents;
        try {
            contents = blocks.get(blockID).getItems();
        }
        catch (IndexOutOfBoundsException e) {
            if (current.getId() == blockID) {
                return current.getItems();
            }
            return null;
        }
        return contents;
    }

    @Override
    public boolean isCurrentEmpty() {
        return (current.getItems().length < 1);
    }

    @Override
    public boolean isEmpty() {
        for (Block<String> block : blocks) {
            if (block.getItems().length >= 1) return false;
        }
        return this.isCurrentEmpty();
    }

    @Override
    public String[][] toArray() {
        String[][] allBlocks = new String[blocks.size() + 1][];
        for (int i = 0; i < blocks.size(); i++) {
            allBlocks[i] = blocks.get(i).getItems();
        }
        allBlocks[blocks.size()] = current.getItems();
        return allBlocks;
    }

    @Override
    public String toString() {
        String border = "==================================================\n" +
                        "==================================================\n";
        StringBuilder chain = new StringBuilder();
        for (Block<String> block : blocks) {
            chain.append(border).append(block.toString()).append("\n");
        }
        return chain + border + current.toString() + "\n" + border;
    }
}
