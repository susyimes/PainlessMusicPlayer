package com.doctoror.fuckoffmusicplayer.db.playlist;

import com.doctoror.fuckoffmusicplayer.db.media.MediaStoreMediaProvider;
import com.doctoror.fuckoffmusicplayer.db.tracks.MediaStoreTracksProvider;
import com.doctoror.fuckoffmusicplayer.queue.Media;

import android.provider.MediaStore;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by Yaroslav Mytkalyk on 06.01.17.
 */
public final class PlaylistProviderRecentlyScannedMediaStore
        implements PlaylistProviderRecentlyScanned {

    @NonNull
    private final MediaStoreMediaProvider mMediaProvider;

    public PlaylistProviderRecentlyScannedMediaStore(
            @NonNull final MediaStoreMediaProvider mediaProvider) {
        mMediaProvider = mediaProvider;
    }

    @Override
    public List<Media> recentlyScannedPlaylist() {
        return mMediaProvider.load(
                MediaStoreTracksProvider.SELECTION_NON_HIDDEN_MUSIC,
                null,
                MediaStore.Audio.Media.DATE_ADDED + " DESC",
                QueueConfig.MAX_PLAYLIST_SIZE);
    }
}