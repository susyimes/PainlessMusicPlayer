/*
 * Copyright (C) 2017 Yaroslav Mytkalyk
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
package com.doctoror.fuckoffmusicplayer.wear;

import com.doctoror.commons.playback.PlaybackState;
import com.doctoror.fuckoffmusicplayer.queue.Media;
import com.doctoror.fuckoffmusicplayer.reporter.PlaybackReporter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Stub {@link PlaybackReporter} for wear. This product flavor does not support wear.
 */
public final class WearableMediaPlaybackReporter implements PlaybackReporter {

    public WearableMediaPlaybackReporter(@NonNull final Context context) {

    }

    @Override
    public void reportTrackChanged(@NonNull final Media media,
            final int positionInQueue) {
        // Stub
    }

    @Override
    public void reportPlaybackStateChanged(@PlaybackState.State final int state,
            @Nullable final CharSequence errorMessage) {
        // Stub
    }

    @Override
    public void reportPositionChanged(final long mediaId, final long position) {
        // Stub
    }

    @Override
    public void reportQueueChanged(@Nullable final List<Media> queue) {
        // Stub
    }

    @Override
    public void onDestroy() {
        // Stub
    }
}
