package com.bluecatcode.fuzzer.payload;

import java.util.Random;

/**
 * Generate random bytes array.
 */
public class RandomBytesPayloadGenerator implements PayloadGenerator {

    public byte[] generate() {

        Random random = new Random();
        int low = 10;
        int high = 1000;
        int size = random.nextInt(high-low) + low;

        byte[] bytes = new byte[size];
        random.nextBytes(bytes);

        return bytes;
    }

}
