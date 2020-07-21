package com.edavalos.Crypto.Components;

import com.edavalos.Crypto.Utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Block<T> {
    private final int id;
    private final Date timestamp;
    private byte[] proofHash;
    private final byte[] priorHash;
    private final List<Item<T>> items;
    private boolean sealed;

    public Block(int id, byte[] previousBlockHash) {
        this.id = id;
        timestamp = new Date();
        priorHash = previousBlockHash;
        items = new ArrayList<>();
        sealed = false;
    }

    public void addItem(T item) {
        if (!sealed) {
            Item<T> newItem = new Item<>(items.size(), item);
            items.add(newItem);
        }
    }

    public byte[] getPriorHash() {
        return priorHash;
    }

    public byte[] getProofHash() {
        return sealed ? proofHash : null;
    }

    public String[] getItems() {
        String[] itemList = new String[items.size()];
        for (int i = 0; i < items.size(); i++) {
            itemList[i] = items.get(i).toString();
        }
        return itemList;
    }

    public int getId() {
        return id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void encrypt(Utility.HashTypes hash) {
        sealed = true;
        proofHash = Utility.byteHash(this.toString(), hash);
    }

/** BLOCK ID: _id_
 *  TIMESTAMP: _timestamp_
 *  PROOF HASH: _proofHash_ (might be null)
 *  PRIOR HASH: _priorHash_
 *
 *  CONTENTS:
 *  - "transaction 1"
 *  - "transaction 2"
 *  - "transaction 3"
 *  - "transaction 4"
 *  - "transaction 5"
 */
    @Override
    public String toString() {
        String proof = (this.getProofHash() != null) ? Utility.bytesToHex(this.getProofHash()) : "[ Not created yet ]";
        String prior = Utility.bytesToHex(this.getPriorHash());

        StringBuilder items = new StringBuilder();
        for (Item<T> item : this.items) {
            items.append("- \"").append(item.toString()).append("\"");
        }

        return "BLOCK ID: " + id + "\n" +
               "TIMESTAMP: " + timestamp.toString() + "\n" +
               "PROOF HASH: " + proof + "\n" +
               "PRIOR HASH: " + prior + "\n" +
               "\n" +
               "CONTENTS:" + items.toString();
    }
}
