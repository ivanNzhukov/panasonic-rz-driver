package ru.home.builder.vlc.listeners;

import ru.home.builder.main.Main;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.*;

public class VlcMediaAdapter extends EmbeddedMediaPlayerComponent {

    private EmbeddedMediaPlayer player = Main.vlc.getPlayer();
    private EmbeddedMediaPlayerComponent component = Main.vlc.getComponent();
    private JFrame frame = Main.vlc.getFrame();

    @Override
    public void playing(MediaPlayer mediaPlayer) {
        SwingUtilities.invokeLater(this::showVideoView);
    }

    @Override
    public void finished(MediaPlayer mediaPlayer) {
        SwingUtilities.invokeLater(this::showDefaultView);
    }

    @Override
    public void error(MediaPlayer mediaPlayer) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                frame,
                "Failed to play media",
                "Error",
                JOptionPane.ERROR_MESSAGE
        ));
    }

    private void showVideoView() {
        player.submit(() -> player.media().play(""));
    }

    private void showDefaultView() {

    }


}
