package com.edavalos.Crypto.Chains;

import com.edavalos.Crypto.Chain;
import com.edavalos.Crypto.Components.Block;
import com.edavalos.Crypto.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class StringChain implements Chain<String> {
    private final Util.HashType hashType;
    private final List<Block<String>> blocks;
    private Block<String> current;
    private final String genesisHash;

    public StringChain(String genesisSeed, Util.HashType hashType) {
        genesisHash = Util.bytesToHex(Util.byteHash(genesisSeed, hashType));
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
    public boolean add(String item) {
        return current.addItem(item);
    }

    @Override
    public boolean add(String... items) {
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
        current = new Block<>(blocks.size(), current.getPriorHash());
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
            Block<String> b = blocks.get(blockID);
            b.resize(String[].class);
            contents = b.getItems();
        }
        catch (IndexOutOfBoundsException e) {
            if (current.getId() == blockID) {
                current.resize(String[].class);
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
        for (Block<String> block : blocks) {
            if (!block.isEmpty()) return false;
        }
        return this.isCurrentEmpty();
    }

    @Override
    public String[][] toArray() {
        String[][] allBlocks = new String[blocks.size() + 1][];
        for (int i = 0; i < blocks.size(); i++) {
            Block<String> b = blocks.get(i);
            b.resize(String[].class);
            allBlocks[i] = b.getItems();
        }
        current.resize(String[].class);
        allBlocks[blocks.size()] = current.getItems();
        return allBlocks;
    }

    @Override
    public String serialize() {
        return this.toString()
                .replaceAll(Util.Token.BORDER.cont, Util.Token.SERIAL_BORDER.cont)
                .replaceAll(Util.Token.BLOCK_ID.cont, "")
                .replaceAll(Util.Token.TIMESTAMP.cont, "")
                .replaceAll(Util.Token.PRIOR_HASH.cont, "")
                .replaceAll(Util.Token.BLOCK_HASH.cont, "")
                .replaceFirst(Util.Token.SERIAL_BORDER.cont, "");
    }

    @Override
    public boolean verify() {
        String currentHash = genesisHash;
        for (Block<String> block : blocks) {
            String blockHash = block.getProofHash();
            String context = block.toString().replace(blockHash, "[ Not created yet ]");

            if (!Util.bytesToHex(Util.byteHash(context, Util.HashType.SHA_256)).equals(blockHash))
                if (!Util.bytesToHex(Util.byteHash(context, Util.HashType.SHA3_256)).equals(blockHash))
                    return false;

            if (!block.getPriorHash().equals(currentHash))
                return false;

            currentHash = block.getProofHash();
        }

        return current.getPriorHash().equals(currentHash);
    }

    @Override
    public String toString() {
        String border = Util.Token.BORDER.cont;
        StringBuilder chain = new StringBuilder();
        for (Block<String> block : blocks) {
            chain.append(border).append(block.toString()).append("\n");
        }
        return chain + border + current.toString() + "\n" + border;
    }
}
