package ru.home.builder.vlc.list;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListRef;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class OwnMediaListPlayer /*extends VlcjTest*/ {

    private final MediaPlayerFactory mediaPlayerFactory;

    private final EmbeddedMediaPlayer mediaPlayer;

    private final Canvas canvas;

    // private final VideoSurface videoSurface;

    private final MediaList mediaList;

    private final MediaListPlayer mediaListPlayer;

    private final JFrame mainFrame;

    private final JFrame listFrame;

    public static void main(String[] args) {
        // UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

        OwnMediaListPlayer app = new OwnMediaListPlayer();
    }

    public OwnMediaListPlayer() {
        this.mediaPlayerFactory = new MediaPlayerFactory();
        this.mediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
        this.canvas = new Canvas();
        //  this.videoSurface = mediaPlayerFactory.videoSurfaces().newVideoSurface(canvas);
        this.mediaList = mediaPlayerFactory.media().newMediaList();
        this.mediaListPlayer = mediaPlayerFactory.mediaPlayers().newMediaListPlayer();

        //MediaRef mediaRef = mediaPlayerFactory.media().newMediaRef("https://www.youtube.com/watch?v=zGt444zwSAM");
        //mediaList.media().add(mediaRef);
        //mediaRef.release();

        MediaListRef mediaListRef = mediaList.newMediaListRef();
        try {
            mediaListPlayer.list().setMediaList(mediaListRef);
        } finally {
            mediaListRef.release();
        }

        //    mediaPlayer.videoSurface().set(videoSurface);
        mediaListPlayer.mediaPlayer().setMediaPlayer(mediaPlayer);

        this.mainFrame = new MainFrame(canvas);
        this.listFrame = new ListFrame(mediaList, mediaListPlayer, mainFrame);

        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowIconified(WindowEvent e) {
                listFrame.setVisible(false);
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                listFrame.setVisible(true);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                mediaListPlayer.release();
                mediaList.release();
                mediaPlayer.release();
                System.exit(0);
            }
        });

        mainFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                listFrame.setLocation(mainFrame.getX() + mainFrame.getWidth() + 4, mainFrame.getY());
                listFrame.setSize(listFrame.getWidth(), mainFrame.getHeight());
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                listFrame.setLocation(mainFrame.getX() + mainFrame.getWidth() + 4, mainFrame.getY());
            }

            @Override
            public void componentShown(ComponentEvent e) {
                listFrame.setVisible(true);
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                listFrame.setVisible(false);
            }

        });

        mainFrame.setVisible(true);
    }

}
