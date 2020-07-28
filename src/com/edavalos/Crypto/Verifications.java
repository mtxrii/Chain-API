package com.edavalos.Crypto;


import com.edavalos.Crypto.Chains.BlockChain;
import com.edavalos.Crypto.Chains.StringChain;

import java.util.Arrays;
import java.util.Date;

public final class Verifications {
    public static void main(String[] args) throws InterruptedException {
        StringChainTest();
//
//        IntegerChainTest();

//        StringChain chain = new StringChain("start", Util.HashType.SHA_256);
//        chain.add("Thing 1");
//        chain.add("Thing 2");
//        chain.add("Thing 3");
//        chain.nextBlock();
//        chain.add("Thing 4");
//        chain.add("Thing 5");
//
//        System.out.println(chain.toString());
//        System.out.println("\nverify: " + chain.verify());
    }

    private static void StringChainTest()  throws InterruptedException {
        StringChain chain = new StringChain("start", Util.HashType.SHA3_256);

        chain.add("PERL contributed $40");
        chain.add("RUBY contributed $12");
        chain.add("JAVA contributed $30");
        chain.add("JS withdrew $120",
                  "JS was denied - insufficient funds");

        Thread.sleep(1000);

        chain.nextBlock();
        chain.add("RUST withdrew $20");
        chain.add("C++ contributed $10");

        Thread.sleep(1000);

        chain.nextBlock();
        chain.add("ERROR - SECURITY BREACH");
        chain.add("nvm");
        chain.discardBlock();
        chain.add("...business as usual");

        Thread.sleep(1000);
        chain.nextBlock();

        System.out.println(chain.toString());
        System.out.println("\n number of blocks: " + chain.size());
        System.out.println("\n total number of items: " + chain.totalItems());
        System.out.println("\n block with 'RUST': " + chain.find("RUST withdrew $20"));
        System.out.println("\n block with 'HTML': " + chain.find("HTML did summ"));
        System.out.println("\n contents of block 1: " + Arrays.toString(chain.getContents(1)));
        System.out.println("\n is every block empty: " + chain.isEmpty());
        System.out.println("\n is current block empty: " + chain.isCurrentEmpty());
        System.out.println("\n block created soonest to now: " + chain.soonestTo(new Date()));
        System.out.println("\n verified: " + chain.verify());

        System.out.println("\n all contents: ");
        String[][] blocks = chain.toArray();
        for (String[] contents : blocks) {
            System.out.println(Arrays.toString(contents));
        }
    }

    private static void IntegerChainTest() throws InterruptedException {
        BlockChain chain = new BlockChain("start", Util.HashType.SHA3_256);

        chain.add(11111);
        chain.add(11112);
        chain.add(11123);
        chain.add(11234,
                  12345);

        Thread.sleep(1000);

        chain.nextBlock();
        chain.add(99999);
        chain.add(99998);

        Thread.sleep(1000);

        chain.nextBlock();
        chain.add(69);
        chain.add(420);
        chain.discardBlock();
        chain.add(404);

        Thread.sleep(1000);
        chain.nextBlock();

        System.out.println(chain.toString());
        System.out.println("\n number of blocks: " + chain.size());
        System.out.println("\n total number of items: " + chain.totalItems());
        System.out.println("\n block with 'RUST': " + chain.find("RUST withdrew $20"));
        System.out.println("\n block with 'HTML': " + chain.find("HTML did summ"));
        System.out.println("\n contents of block 1: " + Arrays.toString(chain.getContents(1)));
        System.out.println("\n is every block empty: " + chain.isEmpty());
        System.out.println("\n is current block empty: " + chain.isCurrentEmpty());
        System.out.println("\n block created soonest to now: " + chain.soonestTo(new Date()));

        System.out.println("\n all contents: ");
        Object[][] blocks = chain.toArray();
        for (Object[] contents : blocks) {
            System.out.println(Arrays.toString(contents));
        }

    }

    private static void SerializeTest() {

    }
}
