package com.bluecatcode.jeff.touch;

public class CodeProvider {

    public static void dumpTouchPoint(String className, String methodName) {
        Long threadId = (long) System.identityHashCode(Thread.currentThread());
        MethodTouchPoint.touchPoint(threadId, className, methodName);
    }

    public static void dumpException(Long threadId, String className, String methodName) {
        ExceptionTouchPoint.touchPoint(threadId, className, methodName);
    }

}
