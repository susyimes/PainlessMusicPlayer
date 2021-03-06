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
package com.doctoror.fuckoffmusicplayer.di;

import com.doctoror.fuckoffmusicplayer.App;
import com.doctoror.fuckoffmusicplayer.base.BaseActivity;
import com.doctoror.fuckoffmusicplayer.appwidget.SingleRowAppWidgetProvider;
import com.doctoror.fuckoffmusicplayer.home.PlaybackStatusFragment;
import com.doctoror.fuckoffmusicplayer.media.manager.MediaManagerService;
import com.doctoror.fuckoffmusicplayer.home.RecentActivityFragment;
import com.doctoror.fuckoffmusicplayer.home.HomeActivity;
import com.doctoror.fuckoffmusicplayer.library.albums.AlbumsFragment;
import com.doctoror.fuckoffmusicplayer.library.albums.conditional.ConditionalAlbumListFragment;
import com.doctoror.fuckoffmusicplayer.library.artistalbums.ArtistAlbumsFragment;
import com.doctoror.fuckoffmusicplayer.library.artists.ArtistsFragment;
import com.doctoror.fuckoffmusicplayer.library.genrealbums.GenreAlbumsFragment;
import com.doctoror.fuckoffmusicplayer.library.genres.GenresFragment;
import com.doctoror.fuckoffmusicplayer.library.playlists.PlaylistsFragment;
import com.doctoror.fuckoffmusicplayer.library.recentalbums.RecentAlbumsFragment;
import com.doctoror.fuckoffmusicplayer.library.tracks.TracksFragment;
import com.doctoror.fuckoffmusicplayer.media.browser.MediaBrowserImpl;
import com.doctoror.fuckoffmusicplayer.media.browser.SearchUtils;
import com.doctoror.fuckoffmusicplayer.media.session.MediaSessionHolder;
import com.doctoror.fuckoffmusicplayer.nowplaying.NowPlayingActivity;
import com.doctoror.fuckoffmusicplayer.nowplaying.NowPlayingActivityIntentHandler;
import com.doctoror.fuckoffmusicplayer.playback.PlaybackService;
import com.doctoror.fuckoffmusicplayer.queue.QueueActivity;
import com.doctoror.fuckoffmusicplayer.reporter.SLSPlaybackReporter;
import com.doctoror.fuckoffmusicplayer.reporter.ScrobbleDroidPlaybackReporter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Main Component
 */
@Singleton
@Component(modules = {
        AppContextModule.class,
        MediaStoreProvidersModule.class,
        MediaModule.class
})
public interface MainComponent {

    void inject(App target);

    void inject(BaseActivity target);

    void inject(HomeActivity target);

    void inject(QueueActivity target);

    void inject(RecentActivityFragment target);

    void inject(NowPlayingActivity target);

    void inject(NowPlayingActivityIntentHandler target);

    void inject(ArtistsFragment target);

    void inject(AlbumsFragment target);

    void inject(GenresFragment target);

    void inject(TracksFragment target);

    void inject(PlaylistsFragment target);

    void inject(PlaybackStatusFragment target);

    void inject(ConditionalAlbumListFragment target);

    void inject(ArtistAlbumsFragment target);

    void inject(GenreAlbumsFragment target);

    void inject(RecentAlbumsFragment target);

    void inject(MediaBrowserImpl target);

    void inject(SearchUtils target);

    void inject(PlaybackService target);

    void inject(ScrobbleDroidPlaybackReporter target);

    void inject(SLSPlaybackReporter target);

    void inject(MediaManagerService target);

    void inject(SingleRowAppWidgetProvider target);

    void inject(MediaSessionHolder target);
}
