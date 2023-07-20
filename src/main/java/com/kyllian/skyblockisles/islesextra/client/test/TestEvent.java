package com.kyllian.skyblockisles.islesextra.client.test;

public class TestEvent extends EventListener {

    public void test1(Test.HelloEvent event) {
        System.out.println("Event: " + event.getMessage());
    }

    public void test2(String s) {
        System.out.println("shouldn't be triggered");
    }

}
