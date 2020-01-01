package ru.home.builder.vlc.listeners;

import ru.home.builder.main.Main;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class VlcComands {


    EmbeddedMediaPlayer player = Main.vlc.getPlayer();


    public void setVolume(int volume) {
        player.audio().setVolume(volume);
    }

    public int getVolume() {
        return player.audio().volume();
    }

    public String getCurrentFilename() {
        return ""; // player.media().info().mrl(); приведет к NPE
    }

    public void playNextFile() {
        player.media().play("");
    }

    public void setFullScreen() {
        player.fullScreen().toggle();
    }
}
