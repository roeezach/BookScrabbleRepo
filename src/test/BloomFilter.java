package test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

public class BloomFilter {
    BitSet bitSet;
    MessageDigest msgDigestHashBased;
    BigInteger bigInt;
    String[] hashAlgorithms;
    int modParm;

    public BloomFilter(int bits, String... hashAlgorithmsToActivate) {
        this.bitSet = new BitSet(bits);
        this.modParm = bits;
        this.hashAlgorithms= new String[hashAlgorithmsToActivate.length];
        System.arraycopy(hashAlgorithmsToActivate, 0, hashAlgorithms, 0, hashAlgorithmsToActivate.length);    
    }

    public void add(String word){

        for (String hashAlgoritem : hashAlgorithms) {
            int bigIntVal = getBigIntValBasedOnHash(word, hashAlgoritem);
            bitSet.set(Math.abs(bigIntVal)% modParm);
        }
    }

    public boolean contains(String word){
        for (String hashAlgoritem : hashAlgorithms) {
            int bigIntVal = getBigIntValBasedOnHash(word, hashAlgoritem);
            if (!bitSet.get(Math.abs(bigIntVal) % modParm))
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(bitSet.length());
        for(int i = 0; i < bitSet.length(); i++){
            if(bitSet.get(i)){
                stringBuilder.append("1");
            }
            else{
                stringBuilder.append("0");
            }
        }
        return stringBuilder.toString();
    }

    private int getBigIntValBasedOnHash(String word, String hashAlgoritem) {
        byte[] bytes;
        try
        {
            msgDigestHashBased = MessageDigest.getInstance(hashAlgoritem);
        }
        catch(NoSuchAlgorithmException e)
        {
            System.err.println("no such algorithem was found in bloom filter: " + e.getMessage());
        }
        bytes = msgDigestHashBased.digest(word.getBytes());
        bigInt = new BigInteger(bytes);
        int bigIntVal = bigInt.intValue();
        return bigIntVal;
    }
}
