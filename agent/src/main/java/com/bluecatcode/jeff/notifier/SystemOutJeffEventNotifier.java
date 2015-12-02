package com.bluecatcode.jeff.notifier;

public class SystemOutJeffEventNotifier implements JeffEventNotifier {

    public SystemOutJeffEventNotifier() {
        /* EMPTY */
    }

    @Override
    public void notifyEvent(Event event) {
        try {
            String message = event.getEventType() + "#" + event.getTargetClassName() + "#" + event.getTargetMethodName();
            System.out.println(message);
        } catch (Exception e) {
            // handle soft and try again later
            e.printStackTrace();
        }
    }
}
