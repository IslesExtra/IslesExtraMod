package com.kyllian.skyblockisles.islesextra.client.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        Test test = new Test();
        test.register(new TestEvent());
        test.invokeEvent(new HelloEvent("Hello world!"));
    }

    public List<EventListener> listeners = new ArrayList<>();
    public <T extends EventListener> void register(T listener) {
        listeners.add(listener);
    }

    public void invokeEvent(Event event) throws InvocationTargetException, IllegalAccessException {
        for (EventListener listener : listeners) {
            for (Method method : listener.getClass().getMethods()) {
                if (method.getParameters().length == 1 && method.getParameters()[0].getType().equals(event.getClass())) {
                    method.invoke(listener, event);
                }
            }
        }
    }

    public static class Event {

    }

    public static class HelloEvent extends Event {
        private final String m;
        public HelloEvent(String message) {
            m = message;
        }
        public String getMessage() { return m; }
    }

}
