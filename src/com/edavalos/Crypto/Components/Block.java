package com.edavalos.Crypto.Components;

import com.edavalos.Crypto.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.lang.reflect.Array;

public class Block<T> {
    private final int id;
    private final Date timestamp;
    private String proofHash;
    private final String priorHash;
    private final List<Item<T>> items;
    private boolean sealed;
    private final boolean manual;

    private T[] itemArray;

    public Block(int id, String previousBlockHash) {
        this.id = id;
        timestamp = new Date();
        priorHash = previousBlockHash;
        items = new ArrayList<>();
        sealed = false;
        manual = false;
    }

    public Block(String id, String ts, String prevHash, String blockHash) {
        this.id = Integer.parseInt(id);
        this.timestamp = new Date(ts);
        this.priorHash = prevHash;
        items = new ArrayList<>();
        manual = true;

        if (!blockHash.equals("[ Not created yet ]")) {
            sealed = true;
            proofHash = blockHash;
        }
        else {
            sealed = false;
        }
    }

    public boolean addItem(T item) {
        if (sealed && !manual) return false;

        Item<T> newItem = new Item<>(items.size(), item);
        items.add(newItem);
        return true;
    }

    public String getPriorHash() {
        return priorHash;
    }

    public String getProofHash() {
        return sealed ? proofHash : null;
    }

    public T[] getItems() {
        if (itemArray == null || itemArray.length != items.size()) return null;

        for (int i = 0; i < items.size(); i++) {
            itemArray[i] = items.get(i).getContents();
        }
        return itemArray;
    }

    public int getId() {
        return id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public boolean isSealed() {
        return sealed;
    }

    public boolean contains(T thing) {
        for (Item<T> item : items) {
            if (item.getContents().equals(thing)) return true;
        }
        return false;
    }

    public int size() {
        return items.size();
    }

    public void seal(Util.HashType hashType) {
        if (sealed) return;

        sealed = true;
        byte[] hash = Util.byteHash(this.toString(), hashType);
        assert hash != null;
        proofHash = Util.bytesToHex(hash);
    }

/** BLOCK ID: _id_
 *  TIMESTAMP: _timestamp_
 *  PROOF HASH: _proofHash_ (might be null)
 *  PRIOR HASH: _priorHash_
 *
 *  CONTENTS:
 *   - "transaction 1" (0)
 *   - "transaction 2" (1)
 *   - "transaction 3" (2)
 *   - "transaction 4" (3)
 *   - "transaction 5" (4)
 */
    @Override
    public String toString() {
        String proof = (this.getProofHash() != null) ? this.getProofHash() : "[ Not created yet ]";
        String prior = this.getPriorHash();

        StringBuilder items = new StringBuilder();
        for (Item<T> item : this.items) {
            items.append("\n - \"").append(item.getContents().toString().replace("\"", "'"))
                    .append("\" (").append(item.getId()).append(")");
        }

        return Util.Token.BLOCK_ID.cont + id + "\n" +
               Util.Token.TIMESTAMP.cont + timestamp.toString() + "\n" +
               Util.Token.PRIOR_HASH.cont + prior + "\n" +
               Util.Token.BLOCK_HASH.cont + proof + "\n" +
               "\n" +
               Util.Token.CONTENTS.cont + items.toString();
    }

    // updates the item array size
    public void resize(Class<T[]> clazz) {
        int len = items.size();
        itemArray = clazz.cast(Array.newInstance(clazz.getComponentType(), len));
    }
}
