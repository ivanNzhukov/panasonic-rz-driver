package ru.home.builder.vlc.list;

import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.medialist.MediaListEventAdapter;

import javax.swing.*;

// This is not really ideal, there could be an update from a native thread between getSize() and something else...
// it's almost like each change should copy the native list to a java list really.
class PlaylistModel extends AbstractListModel {

    MediaList mediaList;

    protected PlaylistModel(MediaList mediaList) {
        this.mediaList = mediaList;
        mediaList.events().addMediaListEventListener(new MediaListEventAdapter() {
            @Override
            public void mediaListItemAdded(MediaList mediaList, MediaRef item, final int index) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        fireIntervalAdded(this, index, index);
                    }
                });
            }

            @Override
            public void mediaListItemDeleted(MediaList mediaList, MediaRef item, final int index) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        fireIntervalRemoved(this, index, index);
                    }
                });
            }
        });
    }

    @Override
    public int getSize() {
        return mediaList.media().count();
    }

    @Override
    public Object getElementAt(int i) {
        return mediaList.media().mrl(i);
    }

    public MediaList getMediaList() {
        return mediaList;
    }

    public void setMediaList(MediaList mediaList) {
        this.mediaList = mediaList;
    }
}
