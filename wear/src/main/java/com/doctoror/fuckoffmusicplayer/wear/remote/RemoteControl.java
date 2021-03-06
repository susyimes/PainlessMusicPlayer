/*
 * Copyright (C) 2016 Yaroslav Mytkalyk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.doctoror.fuckoffmusicplayer.wear.remote;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import com.doctoror.commons.util.Log;
import com.doctoror.commons.util.ProtoUtils;
import com.doctoror.commons.wear.DataPaths;
import com.doctoror.commons.wear.nano.WearQueueFromSearch;
import com.doctoror.commons.wear.nano.WearSearchData;
import com.doctoror.fuckoffmusicplayer.R;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Set;

/**
 * Sends control messages to handheld
 */
public final class RemoteControl {

    private static final String TAG = "RemoteControl";

    private static final RemoteControl INSTANCE = new RemoteControl();

    @NonNull
    public static RemoteControl getInstance() {
        return INSTANCE;
    }

    private final Object mCapabilityLock = new Object();

    public interface PlaybackNodeListener {
        void onNodeConnectionStateChanged(boolean nodeConnected);
    }

    private String mPlaybackControlNodeId;
    private GoogleApiClient mGoogleApiClient;

    private PlaybackNodeListener mPlaybackNodeListener;

    private RemoteControl() {

    }

    public void setPlaybackNodeListener(@Nullable final PlaybackNodeListener playbackNodeListener) {
        mPlaybackNodeListener = playbackNodeListener;
    }

    public void onGoogleApiClientConnected(@NonNull final Context context,
            @NonNull final GoogleApiClient googleApiClient) {
        mGoogleApiClient = googleApiClient;

        final String capability = context.getString(R.string.wear_capability_playback_control);
        Wearable.CapabilityApi.getCapability(googleApiClient, capability,
                CapabilityApi.FILTER_REACHABLE).setResultCallback(
                result -> updateRemoteControlCapability(result.getCapability()));

        Wearable.CapabilityApi.addCapabilityListener(
                googleApiClient,
                mCapabilityListener,
                capability);
    }

    public void onGoogleApiClientDisconnected() {
        if (mGoogleApiClient != null) {
            Wearable.CapabilityApi.removeListener(mGoogleApiClient, mCapabilityListener);
            mGoogleApiClient = null;
        }
        if (mPlaybackControlNodeId != null && mPlaybackNodeListener != null) {
            mPlaybackNodeListener.onNodeConnectionStateChanged(false);
        }
        synchronized (mCapabilityLock) {
            mPlaybackControlNodeId = null;
        }
    }

    public void playPause() {
        sendMessageIfPossible(DataPaths.Messages.PLAY_PAUSE, null);
    }

    public void prev() {
        sendMessageIfPossible(DataPaths.Messages.PREV, null);
    }

    public void next() {
        sendMessageIfPossible(DataPaths.Messages.NEXT, null);
    }

    public void seek(final float seekPercent) {
        sendMessageIfPossible(DataPaths.Messages.SEEK,
                ByteBuffer.allocate(4).putFloat(seekPercent).array());
    }

    public void playMediaFromQueue(final long mediaId) {
        sendMessageIfPossible(DataPaths.Messages.PLAY_FROM_QUEUE,
                ByteBuffer.allocate(8).putLong(mediaId).array());
    }

    public void search(@NonNull final String query) {
        sendMessageIfPossible(DataPaths.Messages.SEARCH,
                query.getBytes(Charset.forName("UTF-8")));
    }

    public void playAlbum(final long albumId) {
        sendMessageIfPossible(DataPaths.Messages.PLAY_ALBUM,
                ByteBuffer.allocate(8).putLong(albumId).array());
    }

    public void playArtist(final long artistId) {
        sendMessageIfPossible(DataPaths.Messages.PLAY_ARTIST,
                ByteBuffer.allocate(8).putLong(artistId).array());
    }

    public void playTrack(@NonNull final WearSearchData.Track[] tracks,
            final long trackId) {
        final WearQueueFromSearch.Queue queue = new WearQueueFromSearch.Queue();
        final long[] trackIds = new long[tracks.length];
        for (int i = 0; i < tracks.length; i++) {
            trackIds[i] = tracks[i].id;
        }
        queue.queue = trackIds;
        queue.selectedId = trackId;
        final byte[] data;
        try {
            data = ProtoUtils.toByteArray(queue);
        } catch (IOException e) {
            Log.w(TAG, e);
            return;
        }
        sendMessageIfPossible(DataPaths.Messages.PLAY_TRACK, data);
    }

    private void sendMessageIfPossible(@NonNull final String path,
            @Nullable final byte[] data) {
        synchronized (mCapabilityLock) {
            final GoogleApiClient googleApiClient = mGoogleApiClient;
            if (googleApiClient != null && googleApiClient.isConnected()
                    && mPlaybackControlNodeId != null) {
                Wearable.MessageApi.sendMessage(googleApiClient, mPlaybackControlNodeId,
                        path, data);
            }
        }
    }

    private void updateRemoteControlCapability(@Nullable final CapabilityInfo capabilityInfo) {
        if (capabilityInfo != null) {
            final String playbackNodeId = pickBestNodeId(capabilityInfo.getNodes());
            mPlaybackNodeListener.onNodeConnectionStateChanged(playbackNodeId != null);
            synchronized (mCapabilityLock) {
                mPlaybackControlNodeId = playbackNodeId;
            }
        }
    }

    @Nullable
    private static String pickBestNodeId(@Nullable final Set<Node> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            return null;
        }
        String bestNodeId = null;
        for (final Node node : nodes) {
            if (node.isNearby()) {
                return node.getId();
            }
            bestNodeId = node.getId();
        }
        return bestNodeId;
    }

    private final CapabilityApi.CapabilityListener mCapabilityListener
            = this::updateRemoteControlCapability;
}
