package com.bluecatcode.jeff.touch;

import com.bluecatcode.jeff.agent.JeffAgent;
import com.bluecatcode.jeff.notifier.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.bluecatcode.jeff.notifier.EventType.EXCEPTION;

public class ExceptionTouchPoint {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionTouchPoint.class);

    public synchronized static void touchPoint(Long threadId, String className, String methodName) {
        if (logger.isDebugEnabled()) {
            // TODO add stacktrace to message log
            logger.debug("Exception thread id: {} invoked method: {}, class: {}", threadId, methodName, className);
        }

        Event event = new Event(className, methodName, EXCEPTION);
        JeffAgent.getNotifier().notifyEvent(event);
    }

}
