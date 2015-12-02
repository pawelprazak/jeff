package com.bluecatcode.fuzzer.sender;

/**
 * Send payload to server.
 */
public interface PayloadSender {
    void send(byte[] payload);
}
