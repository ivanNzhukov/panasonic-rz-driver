package ru.home.builder.main;

import ru.home.builder.vlc.players.CustomVlc;
import ru.home.builder.web.Server;

public class Main {

    public static CustomVlc vlc = new CustomVlc();
    public static Server server = new Server();
   /* public static EmbeddedMediaPlayer player = ru.home.builder.vlc.getPlayer();
    public static EmbeddedMediaPlayerComponent component = ru.home.builder.vlc.getComponent();*/

    public static void main(String[] args) {

        vlc.start();
        server.start();
    }


}
