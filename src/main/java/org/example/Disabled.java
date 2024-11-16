package org.example;

final public class Disabled implements State {
    public String getURI() {
        throw new UnsupportedOperationException("URI cannot be assigned to the Disabled state.");
    };
    Disabled() {}

}
