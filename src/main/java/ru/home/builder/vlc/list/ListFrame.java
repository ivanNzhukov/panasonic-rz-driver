package ru.home.builder.vlc.list;


import ru.home.builder.main.Main;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ListFrame extends JFrame {

    EmbeddedMediaPlayer player = Main.vlc.getPlayer();
    JFrame mainFrame;
    MediaList mediaList;
    MediaListPlayer mediaListPlayer;

    public ListFrame(MediaList mediaList, MediaListPlayer mediaListPlayer, JFrame mainFrame) {
        setTitle("Playlist");
        setBounds(204, 100, 400, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        this.mainFrame = mainFrame;
        this.mediaList = mediaList;
        this.mediaListPlayer = mediaListPlayer;

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        JList list = new JList();
        list.setModel(new PlaylistModel(mediaList));

        JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setViewportView(list);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JPanel controlsPane = new JPanel();
        controlsPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton playButton = new JButton("Play");
        controlsPane.add(playButton);
        JButton nextButton = new JButton("Next");
        controlsPane.add(nextButton);
        JButton previousButton = new JButton("Previous");
        controlsPane.add(previousButton);
        JButton pauseButton = new JButton("Pause");
        controlsPane.add(pauseButton);
        JButton stopButton = new JButton("Stop");
        controlsPane.add(stopButton);
        JButton clearButton = new JButton("Clear");
        controlsPane.add(clearButton);
        contentPane.add(controlsPane, BorderLayout.SOUTH);

        setContentPane(contentPane);

        list.setTransferHandler(new MediaTransferHandler() {
            @Override
            protected void onMediaDropped(String[] uris) {
                for (String uri : uris) {
                    mediaList.media().add(uri);
                }
                System.out.println("IS READ ONLY RETURNS " + mediaList.media().isReadOnly());
            }
        });

        list.addListSelectionListener(listSelectionEvent -> {
        });

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JList list = (JList) e.getSource();
                if (e.getClickCount() == 2) {
                    int index = list.locationToIndex(e.getPoint());
                    if (index != -1) {
                        mediaListPlayer.controls().play(index);
                        mainFrame.setTitle(mediaList.media().mrl(index));
                    }
                }
            }
        });

        playButton.addActionListener(actionEvent -> {
            mediaListPlayer.controls().play();
            checkTitle();
        });

        nextButton.addActionListener(actionEvent -> {
            mediaListPlayer.controls().playNext();
            checkTitle();
        });

        previousButton.addActionListener(actionEvent -> {
            mediaListPlayer.controls().playPrevious();
            checkTitle();
        });

        pauseButton.addActionListener(actionEvent -> mediaListPlayer.controls().pause());

        stopButton.addActionListener(actionEvent -> mediaListPlayer.controls().stop());

        clearButton.addActionListener(actionEvent -> mediaList.media().clear());
    }

    private void checkTitle() {
        if (!mainFrame.getTitle().equals(player.media().info().mrl()))
            mainFrame.setTitle(player.media().info().mrl());
    }
}
