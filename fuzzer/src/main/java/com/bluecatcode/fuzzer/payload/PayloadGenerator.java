package com.bluecatcode.fuzzer.payload;

/**
 * Generate fixed size array of bytes payload.
 */
public interface PayloadGenerator {
    byte[] generate();
}
