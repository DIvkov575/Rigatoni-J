package org.example;

final public class Song implements State {
    String uri;
    public String getURI() {
        return this.uri;

    }
    Song(String uri) {
        this.uri = uri;
    }

}
