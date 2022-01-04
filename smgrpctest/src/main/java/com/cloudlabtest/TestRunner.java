package com.cloudlabtest;

import com.cloudlabtest.tests.SmokeTest;

public class TestRunner {
    public static void main(String[] args) throws InterruptedException {
        SmokeTest test = new SmokeTest();
        test.runTest();
    }
}
