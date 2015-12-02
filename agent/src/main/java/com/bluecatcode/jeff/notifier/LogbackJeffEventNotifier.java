package com.bluecatcode.jeff.notifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogbackJeffEventNotifier implements JeffEventNotifier {

    private static final Logger log = LoggerFactory.getLogger(LogbackJeffEventNotifier.class);

    public LogbackJeffEventNotifier() {
        /* EMPTY */
    }

    @Override
    public void notifyEvent(Event event) {
        try {
            String message = event.getEventType() + "#" + event.getTargetClassName() + "#" + event.getTargetMethodName();
            log.info(message);
        } catch (Exception e) {
            // handle soft and try again later
            e.printStackTrace();
        }
    }
}
