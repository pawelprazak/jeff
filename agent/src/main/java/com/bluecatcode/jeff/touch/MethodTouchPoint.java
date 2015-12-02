package com.bluecatcode.jeff.touch;

import com.bluecatcode.jeff.agent.JeffAgent;
import com.bluecatcode.jeff.notifier.Event;
import com.bluecatcode.jeff.notifier.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class MethodTouchPoint {

    private static final Logger logger = LoggerFactory.getLogger(MethodTouchPoint.class);

    private static List<String> touchPoints = new LinkedList<String>();

    public synchronized static void touchPoint(Long threadId, final String className, final String methodName) {
        if (logger.isDebugEnabled()) {
//            logger.debug("thread id: {} invoked method: {}, class: {}", threadId, methodName, className);
        }

        // store information about uncaught exceptions
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread thread, Throwable throwable) {
                Long threadId = (long) System.identityHashCode(thread);
                CodeProvider.dumpException(threadId, className, methodName);
                throwable.printStackTrace(System.err);
            }
        });

        if (!touchPoints.contains(className + "#" + methodName)) {
            // new state transitions, notify fuzzer
            Event event = new Event(className, methodName, EventType.CALL);
            JeffAgent.getNotifier().notifyEvent(event);
            touchPoints.add(className + "#" + methodName);
        }

    }
}
