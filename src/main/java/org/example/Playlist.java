package org.example;

final public class Playlist implements State {
    String uri;
    public String getURI() {
        return this.uri;

    }
    Playlist(String uri) {
        this.uri = uri;
    }

}
