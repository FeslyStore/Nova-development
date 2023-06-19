package dev.fesly.impl.client;


public record User(String username, String uid, String rank) {
    public String getAvatar() {
        return "https://media.tenor.com/Ka1BbVVWzNoAAAAC/anime-krul.gif";
    }
}