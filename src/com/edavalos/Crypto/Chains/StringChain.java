package com.edavalos.Crypto.Chains;

import com.edavalos.Crypto.Chain;
import com.edavalos.Crypto.Components.Block;
import com.edavalos.Crypto.Utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class StringChain implements Chain<String> {
    private final byte[] genesisHash;
    private final List<Block<String>> blocks;
    private Block<String> current;

    public StringChain(String genesisSeed, Utility.HashTypes hashType) {
        genesisHash = Utility.byteHash(genesisSeed, hashType);
        blocks = new ArrayList<>();
        current = new Block<>(0, genesisHash);
    }

    @Override
    public boolean nextBlock() {
        return false;
    }

    @Override
    public boolean add(String item) {
        return false;
    }

    @Override
    public boolean add(String[] items) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public int totalItems() {
        return 0;
    }

    @Override
    public void discardBlock() {

    }

    @Override
    public int contains(String item) {
        return 0;
    }

    @Override
    public int soonestTo(Date timestamp) {
        return 0;
    }

    @Override
    public String getContents(int blockID) {
        return null;
    }

    @Override
    public boolean isCurrentEmpty() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
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
        return null;
    }
}
