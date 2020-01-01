package ru.home.builder.vlc.listeners;

import ru.home.builder.main.Main;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class VlcMouseAdapter extends MouseAdapter {

    private EmbeddedMediaPlayerComponent component = Main.vlc.getComponent();
    private EmbeddedMediaPlayer player = Main.vlc.getPlayer();
    private VlcComands comands = new VlcComands();

    @Override
    public void mouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            comands.setFullScreen();
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        int volume = player.audio().volume();
        if (volume > 1 && notches > 0) {
            comands.setVolume(volume - notches);
        } else if (volume < 200 && notches < 0) {
            comands.setVolume(volume - notches);
        }
    }
}
