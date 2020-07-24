# Chain API
A simple blockchain utility to permanently archive records of any type. Also allows for easy visualization of all the data.

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

### How does it work?

![](https://spheregen.com/wp-content/uploads/2019/04/blockchain.png)

A blockchain is essentially just a series of blocks, or nodes. Each node contains an id, a timestamp, and a hash of the previous node, along with whatever data you store in it. Conventionally, every x entries to a block, or every predetermied time interval, the block is sealed. A hash for everything in this block is then generated, which is stored in the next block. This way every block contains a hash of the previous one, so if any alterations are made to the data in one block, that block's hash would need to be re-evaluated, changing the hash of the next block, and every block after that. So the validity of the information in the blocks after the most recent one can be easily verified.
