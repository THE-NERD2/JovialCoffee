package dev.the_nerd2.jovialcoffee.jastgen;

class OtherClass {
    int otherField;
    DummyDependentClass firstClass;
}
public class DummyDependentClass {
    int randomField;
    OtherClass otherClass;
}