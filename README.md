# Chain API
A simple blockchain utility to permanently archive and visualize records of any type.

```
==================================================
==================================================
BLOCK ID: 0
TIMESTAMP: Thu Jul 23 17:21:20 PDT 2020
PRIOR HASH: 5646f1a87a6d20703d9dde40167e25f610b92aadfc6c79fbbbd6bca46ff2db49
BLOCK HASH: d75bc757e2c55673445f6935769fdab881535ffa295f3c4ad4661889012011e9

CONTENTS:
 - "PERL contributed $40" (0)
 - "RUBY contributed $12" (1)
 - "JAVA contributed $30" (2)
 - "JS withdrew $120" (3)
 - "JS was denied - insufficient funds" (4)
==================================================
==================================================
BLOCK ID: 1
TIMESTAMP: Thu Jul 23 17:21:21 PDT 2020
PRIOR HASH: d75bc757e2c55673445f6935769fdab881535ffa295f3c4ad4661889012011e9
BLOCK HASH: c2249922b79fe024227947736f786a0bed9c355a0a14f1e1348692e00bef1723

CONTENTS:
 - "RUST withdrew $20" (0)
 - "C++ contributed $10" (1)
==================================================
==================================================
```

# Whats This For?
You've probably heard of cryptocurrencies, which are money exchange systems that record transactions in large scale public blockchains. The reason blockchains are used is because any data stored in them is guaranteed to never change. And it turns out having a permanent log of un-alterable receipts is pretty useful in various applications. Some examples include...
* Product license certificates
* Money lending
* Insurance claims processing
* Orders to ship

However in the context of this java library, some smaller scale uses might be...
* Tracking automated functions
* Online voting
* Recording assigned UUIDs
* Game kill & death logs
* Database table creation

# How does it work?

![](https://spheregen.com/wp-content/uploads/2019/04/blockchain.png)

A [blockchain](https://en.wikipedia.org/wiki/Blockchain#Structure) is essentially just a series of blocks, or nodes. Each node contains an id, a timestamp, and a hash of the previous node, along with whatever data you store in it. Conventionally, every x entries to a block, or every predetermied time interval, the block is sealed. A hash for everything in this block is then generated, which is stored in the next block. This way every block contains a hash of the previous one, so if any alterations are made to the data in one block, that block's hash would need to be re-evaluated, changing the hash of the next block, and every block after that. So the validity of the information in the blocks after the most recent one can be easily verified.

Most large scale blockchains are decentralized, with each contributor having a local copy of the constantly-updated chain. This API allows for saving to and reading from files containing blockchains, making it easy to have decentralized blockchains on different runtimes all synchronizing with (and continuously verifying) one file.

# Usage

### Full docs [here](http://blockchain.edavalos.com/docs.html)

Make a new blockchain with only a genesis seed and a specified hash type.
``` java
// BlockChains can hold any type of objects in their blocks.
BlockChain bChain = new BlockChain("origin", Util.HashType.SHA_256);
// StringChains are explicitly for text.
StringChain sChain = new StringChain("seed", Util.HashType.SHA3_256);
```
Options for hash types are `SHA_256` and `SHA3_256`

Every chain has a head block, which is where new data is stored. When this block is sealed, a new one is created containing the hash of the previous one.
```java
// Add some data.
example_chain.add("[ party X ] transfered _$20_ to [ party A ]");
example_chain.add("[ party M ] transfered _$90_ to [ party D ]");
example_chain.add("[ party D ] transfered _$30_ to [ party F ]");

// Seal this block and move on to the next.
example_chain.nextBlock();

// Add more data.
example_chain.add("[ party A ] transfered _$5_ to [ party D ]");
```


Now if we visualize the blockchain at this state with `example_chain.toString()` we get:
```
==================================================
==================================================
BLOCK ID: 0
TIMESTAMP: Thu Jul 30 23:53:40 PDT 2020
PRIOR HASH: 5646f1a87a6d20703d9dde40167e25f610b92aadfc6c79fbbbd6bca46ff2db49
BLOCK HASH: ee5fe2dc09782035c3fb25c592ff82c5f613551ddda4b4abe43ef12d59b16f7f

CONTENTS:
 - "[ party X ] transfered _$20_ to [ party A ]" (0)
 - "[ party M ] transfered _$90_ to [ party D ]" (1)
 - "[ party D ] transfered _$30_ to [ party F ]" (2)
==================================================
==================================================
BLOCK ID: 1
TIMESTAMP: Thu Jul 30 23:53:41 PDT 2020
PRIOR HASH: ee5fe2dc09782035c3fb25c592ff82c5f613551ddda4b4abe43ef12d59b16f7f
BLOCK HASH: [ Not created yet ]

CONTENTS:
 - "[ party A ] transfered _$5_ to [ party D ]" (0)
==================================================
==================================================
```

The last block will never have a hash because it will always accept new data, until `example_chain.nextBlock()` is called, sealing the last block and making a new head block.

Chains have a multitude of methods to manipulate and obtain information from them. Some examples include `totalItems()`, `find()`, `toArray()`, and `verify()`. Read more at the [docs](http://blockchain.edavalos.com/index.html) or check out the blockchains' [interface file](https://github.com/mtxrii/Chain-API/blob/master/src/com/edavalos/Crypto/Chain.java) directly.
