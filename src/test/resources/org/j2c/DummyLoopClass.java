package org.j2c;

public class DummyLoopClass {
    public void loop() {
        int i = 0;
        while(i < 10) {
            if(i < 5) {
                i = i + 1;
            } else {
                i = i + 2;
            }
        }
    }
    public void nested() {
        int i = 0;
        while(i < 10) {
            while(i < 6) {
                i = i + 2;
            }
            i = i + 1;
        }
    }
}