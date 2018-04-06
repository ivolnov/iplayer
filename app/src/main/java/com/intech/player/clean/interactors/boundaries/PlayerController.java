package com.intech.player.clean.interactors.boundaries;

import com.intech.player.clean.entities.Track;

import io.reactivex.CompletableSource;
import io.reactivex.Observable;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 05.04.18
 */
public interface PlayerController {

    CompletableSource init(String url);
    CompletableSource start();
    CompletableSource seek(long position);
    CompletableSource pause();
    CompletableSource stop();
    Observable<Event> getPlayerEvents();


    enum Event {
        Start,
        Pause,
        Progress;

        private Track track;
        private int progress;

        void setTrack(Track track) {
            this.track = track;
        }

        void setProgress(int progress) {
            this.progress = progress;
        }

        public Track getTrack() {
            return track;
        }

        public int getProgress() {
            return progress;
        }
    }
}
