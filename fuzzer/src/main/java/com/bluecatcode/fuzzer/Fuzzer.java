package com.bluecatcode.fuzzer;

import com.bluecatcode.fuzzer.payload.PayloadGenerator;
import com.bluecatcode.fuzzer.sender.PayloadSender;
import com.bluecatcode.fuzzer.sender.TCPSocketPayloadSender;
import com.bluecatcode.fuzzer.service.PayloadService;
import com.bluecatcode.fuzzer.payload.RandomBytesPayloadGenerator;
import com.bluecatcode.fuzzer.reader.AgentEventReader;

import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import static java.lang.String.format;

public class Fuzzer {

    public static void main(String[] args) throws IOException, InterruptedException {

        // default values
        String fuzzType = "TCP";
        String targetHost = "localhost";
        Integer targetPort = 7072;
        Integer fuzzerPort = 7071;

        for(int i = 0; i < args.length; i++) {
            if (args[i].equals("-fuzzType")) {
                fuzzType = args[i+1];
            }
            if (args[i].equals("-targetHost")) {
                targetHost = args[i+1];
            }
            if (args[i].equals("-targetPort")) {
                targetPort = Integer.valueOf(args[i+1]);
            }
            if (args[i].equals("-fuzzerPort")) {
                fuzzerPort = Integer.valueOf(args[i+1]);
            }
        }

        startFuzzer(targetHost, targetPort, fuzzerPort, fuzzType);
    }

    private static void startFuzzer(String targetHost, Integer targetPort, Integer fuzzerPort, String fuzzType) throws IOException {
        BlockingDeque<String> queue = new LinkedBlockingDeque<String>();
        AgentEventReader reader = new AgentEventReader(queue, fuzzerPort);
        PayloadGenerator generator = new RandomBytesPayloadGenerator();

        PayloadSender sender = null;
        if ("TCP".equals(fuzzType)) {
            sender = new TCPSocketPayloadSender(targetHost, targetPort);
        } else if ("HTTP".equals(fuzzType)) {
            // TODO implement payload generator for http
        } else if ("JMX".equals(fuzzType)) {
            // TODO implement payload generator for jmx
        }

        PayloadService service = new PayloadService(queue, generator, sender);

        System.out.println(format("Fuzzer has started on %s:%d", targetHost, targetPort));

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(reader);
        executorService.execute(service);
    }
}
