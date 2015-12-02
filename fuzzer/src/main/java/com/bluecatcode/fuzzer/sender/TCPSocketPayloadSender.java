package com.bluecatcode.fuzzer.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Send payload to server over TCP Socket.
 */
public class TCPSocketPayloadSender implements com.bluecatcode.fuzzer.sender.PayloadSender {

    private static final Logger logger = LoggerFactory.getLogger(TCPSocketPayloadSender.class);

    private String hostname;
    private Integer port;
    private Socket socket;

    public TCPSocketPayloadSender(String hostname, Integer port) throws IOException {
        this.hostname = checkNotNull(hostname);
        this.port = checkNotNull(port);
    }

    public void send(byte[] bytes) {
        try {
            // TODO create socket and send bytes
        } catch (Exception e) {
            // handle soft and try again later
            logger.error("Cannot send payload", e);
        }
    }
}
