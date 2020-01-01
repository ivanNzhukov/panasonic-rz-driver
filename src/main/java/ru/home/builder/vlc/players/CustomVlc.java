package ru.home.builder.vlc.players;

import ru.home.builder.vlc.list.ListFrame;
import ru.home.builder.vlc.listeners.VlcMouseAdapter;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListRef;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.fullscreen.adaptive.AdaptiveFullScreenStrategy;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CustomVlc {

    private String file = "C:\\Users\\Public\\Videos\\Sample Videos\\Живая природа.wmv";
    private JFrame frame;
    private EmbeddedMediaPlayer player;
    private EmbeddedMediaPlayerComponent component;
    private MediaList mediaList;
    private MediaListPlayer mediaListPlayer;
    private VlcMouseAdapter vlcMouseAdapter;
    private JFrame listFrame;

    public CustomVlc() {
        try {
            component = new EmbeddedMediaPlayerComponent();
            player = component.mediaPlayer();

        } catch (UnsatisfiedLinkError error) {
            System.out.println(" Check bit of your VLC and your OS is equal.");
            System.exit(1);
        }
    }

    public void start() {

        vlcMouseAdapter = new VlcMouseAdapter();
        frame = new JFrame();
        frame.setBounds(100, 100, 800, 600);

        /*   * If you do not want to use native discovery, use the {@link #MediaPlayerFactory(NativeDiscovery, String...)}
         * constructor instead, passing <code>null</code>.*/
/*        try {
            URL res = getClass().getClassLoader().getResource("lib\\x64");
            File file = Paths.get(res.toURI()).toFile();
            NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), file.getAbsolutePath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }*/


        frame.setContentPane(component);
        component.videoSurfaceComponent().addMouseListener(vlcMouseAdapter);
        component.videoSurfaceComponent().addMouseWheelListener(vlcMouseAdapter);

        mediaList = component.mediaPlayerFactory().media().newMediaList();
        mediaListPlayer = component.mediaPlayerFactory().mediaPlayers().newMediaListPlayer();

        MediaListRef mediaListRef = mediaList.newMediaListRef();
        try {
            mediaListPlayer.list().setMediaList(mediaListRef);
        } finally {
            mediaListRef.release();
        }

        mediaListPlayer.mediaPlayer().setMediaPlayer(player);

        listFrame = new ListFrame(mediaList, mediaListPlayer, frame);
        player.fullScreen().strategy(new AdaptiveFullScreenStrategy(frame));

        frame.addWindowListener(new WindowAdapter() {
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
                component.release();
                System.exit(0);
            }
        });
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                listFrame.setLocation(frame.getX() + frame.getWidth() + 4, frame.getY());
                listFrame.setSize(listFrame.getWidth(), frame.getHeight());
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                listFrame.setLocation(frame.getX() + frame.getWidth() + 4, frame.getY());
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

        frame.setTitle(file);
        frame.setVisible(true);
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public EmbeddedMediaPlayer getPlayer() {
        return player;
    }

    public void setPlayer(EmbeddedMediaPlayer player) {
        this.player = player;
    }

    public EmbeddedMediaPlayerComponent getComponent() {
        return component;
    }

    public void setComponent(EmbeddedMediaPlayerComponent component) {
        this.component = component;
    }
}
