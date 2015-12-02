package com.bluecatcode.fuzzer.service;

import com.bluecatcode.fuzzer.payload.PayloadGenerator;
import com.bluecatcode.fuzzer.sender.PayloadSender;
import com.google.common.collect.EvictingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;

import static com.google.common.base.Preconditions.checkNotNull;

public class PayloadService extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(PayloadService.class);

    private final BlockingDeque<String> queue;
    private final PayloadGenerator generator;
    private final PayloadSender sender;

    private boolean initialized = false;

    // store all exceptions from agent
    private List<String> exceptions = new LinkedList<String>();

    // store all unique methods invocation from agent
    private List<String> methods = new LinkedList<String>();

    // store last 100 payloads
    private Queue<byte[]> payloads = EvictingQueue.create(100);

    public PayloadService(BlockingDeque<String> queue,
                          PayloadGenerator generator,
                          PayloadSender sender) {
        this.queue = checkNotNull(queue);
        this.generator = checkNotNull(generator);
        this.sender = checkNotNull(sender);
    }

    @Override
    public void run() {
        while (true) {
            try {
                // take next message from agent queue
                String message = queue.take();

                if (message.contains("AGENT_READY")) {
                    initialized = true;
                    logger.debug("Java agent is ready, starting fuzzing");
                    continue;
                }

                if (initialized) {

                    // check whether we go deeper in flow execution
                    if (message.contains("NORMAL_INVOCATION")) {
                        if (!methods.contains(message)) {
                            // TODO we need to repeat last payloads to determine specific value
                            logger.debug("New state transitions = {} payload = {}", message, payloads.peek());
                            methods.add(message);
                        }
                    }

                    // check whether we find new exception payload
                    if (message.contains("EXCEPTION_INVOCATION")) {
                        if (!exceptions.contains(message)) {
                            // TODO we need to repeat last payloads to determine specific value
                            logger.debug("New exception = {} payload = {}", message, payloads.peek());
                            exceptions.add(message);
                        }
                    }

                    byte[] payload = generator.generate();
                    sender.send(payload);
                    payloads.add(payload);
                }
            } catch (Exception e) {
                throw new IllegalStateException("Payload service error", e);
            }
        }
    }
}
