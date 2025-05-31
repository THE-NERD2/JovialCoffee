package org.j2c;

class OtherClass {
    int otherField;
    DummyDependentClass firstClass;
}
public class DummyDependentClass {
    int randomField;
    OtherClass otherClass;
}