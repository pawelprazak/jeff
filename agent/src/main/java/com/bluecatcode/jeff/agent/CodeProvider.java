package com.bluecatcode.jeff.agent;

public class CodeProvider {

    public void dump() {
        Counter.newNotifier().send();
        int count = Counter.INSTANCE.count();
        System.out.println("Call: " + count);
    }

    public void dump2() {
        System.out.println("msg");
    }

}
