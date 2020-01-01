package ru.home.builder.vlc.listeners;

import ru.home.builder.main.Main;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VlcWindowAdapter extends WindowAdapter {

    private EmbeddedMediaPlayerComponent component = Main.vlc.getComponent();

    @Override
    public void windowClosing(WindowEvent e) {
        component.release();
        System.exit(0);
    }


}
