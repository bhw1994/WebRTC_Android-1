package com.webrtc.boyj;

import org.junit.Test;
import org.mockito.Mockito;

import io.reactivex.subjects.PublishSubject;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MockitoExampleUnitTest {
    public static class MyTestClass {

        public PublishSubject<String> subject = PublishSubject.create();

        public MyTestClass() {
            init();
        }

        public void init() {
            subject.subscribe(s -> {
                testMethod4();
            });
        }

        public void testMethod1() {
        }

        public void testMethod2() {
            testMethod3();
        }

        public void testMethod3() {
        }

        public void testMethod4() {
        }
    }

    @Test
    public void test1() {
        //MyTestClass testClass=spy(new MyTestClass());
        MyTestClass testClass = spy(MyTestClass.class);
        testClass.init();

        testClass.testMethod1();
        verify(testClass, times(1)).testMethod1();

        testClass.testMethod2();
        verify(testClass, times(1)).testMethod2();
        verify(testClass, times(1)).testMethod3();
    }

    @Test
    public void test2() {
        MyTestClass testClass = spy(MyTestClass.class);
        //testClass.init();

        testClass.subject.onNext("hello");
        verify(testClass, times(1)).testMethod4();
    }

    @Test
    public void test3() {
        MyTestClass testClass = spy(new MyTestClass());
        testClass.init();

        testClass.subject.onNext("hello");
        verify(testClass, times(1)).testMethod4();
    }

    @Test
    public void test4() {
        MyTestClass testClass = spy(new MyTestClass());
        testClass.init();
        Mockito.reset(testClass);

        testClass.subject.onNext("hello");
        verify(testClass, times(1)).testMethod4();
    }
}
