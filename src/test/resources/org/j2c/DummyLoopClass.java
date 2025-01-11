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
}