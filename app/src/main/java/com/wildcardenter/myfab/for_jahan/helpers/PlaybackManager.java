package com.wildcardenter.myfab.for_jahan.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;

import com.wildcardenter.myfab.for_jahan.activities.MusicActivity;
import com.wildcardenter.myfab.for_jahan.SongService;

import java.util.ArrayList;
import java.util.HashMap;


public class PlaybackManager {
    public static final String songPref = "songPref";
    public static boolean isFirstLoad = true;
    public static ArrayList<HashMap<String, String>> songsList;
    public static ArrayList<Integer> shufflePosList;
    public static boolean isServiceRunning = false, isManuallyPaused = false;
    public static boolean goAhead = true;
    private static Context mContext;
    private static SharedPreferences sharedPref;
    private static PlaybackManager playbackManager;
    private Uri allsongsuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    private String[] projectionSongs = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DURATION};
    private String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
    private Listeners.LoadSongListener loadSongListener = new Listeners.LoadSongListener() {

        @Override
        public void onSongLoaded() {
            ((MusicActivity) mContext).setAllSongs();
        }
    };

    private PlaybackManager(Context mContext) {
        this.mContext = mContext;
        isFirstLoad = true;
        sharedPref = mContext.getSharedPreferences(songPref, mContext.MODE_PRIVATE);
        createPlayList();
    }

    private PlaybackManager(Context mContext, Listeners.LoadSongListener loadSongListener, Uri allsongsuri) {
        this.mContext = mContext;
        sharedPref = mContext.getSharedPreferences(songPref, mContext.MODE_PRIVATE);
        if (loadSongListener != null)
            this.loadSongListener = loadSongListener;
        if (allsongsuri != null)
            this.allsongsuri = allsongsuri;
        createPlayList();
    }

    public static PlaybackManager getInstance(Context mContext) {
        playbackManager = null;
        songsList = new ArrayList<>();
        shufflePosList = new ArrayList<>();
        goAhead = true;
        playbackManager = new PlaybackManager(mContext);
        return playbackManager;
    }

    public static void stopService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mContext.startForegroundService(
                    new Intent(mContext, SongService.class).setAction(SongService.ACTION_STOP));
        }
        else {
            mContext.startService(
                    new Intent(mContext, SongService.class).setAction(SongService.ACTION_STOP));
        }
    }

    public static void onStopService() {
        ((MusicActivity) mContext).setPlayPauseView(false);
        shufflePosList.clear();
        songsList.clear();
        songsList = null;
        shufflePosList = null;
        sharedPref = null;

        playbackManager = null;
        mContext = null;
        isServiceRunning = false;
        isFirstLoad = true;
        isManuallyPaused = false;

        if (BitmapPalette.smallBitmap != null) {
            BitmapPalette.smallBitmap.recycle();
            BitmapPalette.mediumBitmap.recycle();
        }
    }

    public static boolean playPauseEvent(boolean headphone, boolean isPlaying, boolean isResume, int seekProgress) {
        HashMap<String, String> hashMap = getPlayingSongPref();
        if (headphone || isPlaying) {
            goAhead = false;
            ((MusicActivity) mContext).setPlayPauseView(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mContext.startForegroundService(
                        new Intent(mContext, SongService.class).setAction(SongService.ACTION_PAUSE));
            }
            else {
                mContext.startService(
                        new Intent(mContext, SongService.class).setAction(SongService.ACTION_PAUSE));
            }
            hashMap.put(MusicActivity.SONG_PROGRESS, "" + SongService.getCurrPos());
            setPlayingSongPref(hashMap);
            return false;
        } else {
            isManuallyPaused = false;
            if (hashMap != null && hashMap.get(MusicActivity.SONG_ID) != null) {
                if (seekProgress != -1)
                    seekTo(seekProgress, isResume, hashMap);
                else playSong(hashMap);
                return true;
            }
        }
        hashMap.clear();
        return false;
    }

    public static void playSong(final HashMap<String, String> hashMap) {
        goAhead = false;
        isManuallyPaused = false;
        setPlayingSongPref(hashMap);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(mContext, SongService.class);
                i.setAction(SongService.ACTION_PLAY);
                i.putExtra(MusicActivity.SONG_PATH, hashMap.get(MusicActivity.SONG_PATH));
                i.putExtra(MusicActivity.SONG_TITLE, hashMap.get(MusicActivity.SONG_TITLE));
                i.putExtra(MusicActivity.ARTIST_NAME, hashMap.get(MusicActivity.ARTIST_NAME));
                i.putExtra(MusicActivity.ALBUM_NAME, hashMap.get(MusicActivity.ALBUM_NAME));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mContext.startForegroundService(i);
                }else {
                    mContext.startService(i);
                }
                int pos = Integer.parseInt(hashMap.get(MusicActivity.SONG_POS));
                if (!shufflePosList.contains(pos)) {
                    shufflePosList.add(pos);
                }
            }
        }).start();
        ((MusicActivity) mContext).loadSongInfo(hashMap, true);
    }

    private static void seekTo(final int progress, final boolean resume, final HashMap<String, String> hashMap) {
        if (goAhead) {
            goAhead = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(mContext, SongService.class);
                    if (hashMap != null) {
                        i.putExtra(MusicActivity.SONG_PATH, hashMap.get(MusicActivity.SONG_PATH));
                        i.putExtra(MusicActivity.SONG_TITLE, hashMap.get(MusicActivity.SONG_TITLE));
                        i.putExtra(MusicActivity.ARTIST_NAME, hashMap.get(MusicActivity.ARTIST_NAME));
                        i.putExtra(MusicActivity.ALBUM_NAME, hashMap.get(MusicActivity.ALBUM_NAME));
                    }
                    i.setAction(SongService.ACTION_SEEK);
                    i.putExtra("seekTo", progress);
                    if (resume) i.putExtra("resume", true);
                    else i.putExtra("resume", false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        mContext.startForegroundService(i);
                    }else {
                        mContext.startService(i);
                    }
                    int pos = Integer.parseInt(hashMap.get(MusicActivity.SONG_POS));
                    if (!shufflePosList.contains(pos)) {
                        shufflePosList.add(pos);
                    }
                }
            }).start();
//            ((MusicActivity) mContext).loadSongInfo(hashMap, true);
        }
    }

    public static void playNext(boolean isShuffle) {
        if (goAhead) {
            goAhead = false;
            MusicActivity.shouldContinue = false;
            String lastPos = getPlayingSongPref().get(MusicActivity.SONG_POS);
            int pos = Integer.parseInt((lastPos == null) ? "0" : lastPos);
            if (isShuffle) {
                if (shufflePosList.contains(pos)) {
                    int index = shufflePosList.indexOf(pos);
                    if (index < (shufflePosList.size() - 1))
                        pos = shufflePosList.get(++index);
                    else pos = shufflePos(false);
                } else pos = shufflePos(false);
            } else pos += 1;

            if (pos > -1 && pos < songsList.size()) {
                HashMap<String, String> hashMap = songsList.get(pos);
                playSong(hashMap);
            }
        }
    }

    public static void playPrev(boolean isShuffle) {
        if (goAhead) {
            goAhead = false;
            MusicActivity.shouldContinue = false;
            int pos = Integer.parseInt(getPlayingSongPref().get(MusicActivity.SONG_POS));
            if (isShuffle && shufflePosList.contains(pos)) {
                int index = shufflePosList.indexOf(pos);
                if (index != 0) pos = shufflePosList.get(--index);
                else {
                    pos = shufflePos(true);
                }
            } else pos -= 1;
            if (pos > -1 && pos < songsList.size()) {
                HashMap<String, String> hashMap = songsList.get(pos);
                playSong(hashMap);
            }
        }
    }

    public static void mediaPlayerStarted(MediaPlayer mp) {
        ((MusicActivity) mContext).setSeekProgress();
    }

    public static void showNotif(boolean updateColors) {
        if (updateColors)
            ((MusicActivity) mContext).setBitmapColors();
        if (!isFirstLoad)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mContext.startForegroundService(new Intent(mContext, SongService.class).setAction(SongService.UPDATE_NOTIF));
            }
            else {
                mContext.startService(new Intent(mContext, SongService.class).setAction(SongService.UPDATE_NOTIF));
            }
    }

    public static HashMap<String, String> getPlayingSongPref() {
        HashMap<String, String> hashMap = new HashMap<>();
        if (sharedPref != null) {
            hashMap.put(MusicActivity.SONG_TITLE, sharedPref.getString(MusicActivity.SONG_TITLE, ""));
            hashMap.put(MusicActivity.SONG_ID, sharedPref.getString(MusicActivity.SONG_ID, ""));
            hashMap.put(MusicActivity.ARTIST_NAME, sharedPref.getString(MusicActivity.ARTIST_NAME, ""));
            hashMap.put(MusicActivity.ALBUM_NAME, sharedPref.getString(MusicActivity.ALBUM_NAME, ""));
            hashMap.put(MusicActivity.SONG_DURATION, sharedPref.getString(MusicActivity.SONG_DURATION, "" + 0));
            hashMap.put(MusicActivity.SONG_PATH, sharedPref.getString(MusicActivity.SONG_PATH, ""));
            hashMap.put(MusicActivity.SONG_POS, sharedPref.getString(MusicActivity.SONG_POS, -1 + ""));
            hashMap.put(MusicActivity.SONG_PROGRESS, sharedPref.getString(MusicActivity.SONG_PROGRESS, 0 + ""));
        }
        return hashMap;
    }

    private static void setPlayingSongPref(final HashMap<String, String> songDetail) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences.Editor prefEditor = sharedPref.edit();
                prefEditor.putString(MusicActivity.SONG_TITLE, songDetail.get(MusicActivity.SONG_TITLE))
                        .putString(MusicActivity.SONG_ID, songDetail.get(MusicActivity.SONG_ID))
                        .putString(MusicActivity.ARTIST_NAME, songDetail.get(MusicActivity.ARTIST_NAME))
                        .putString(MusicActivity.ALBUM_NAME, songDetail.get(MusicActivity.ALBUM_NAME))
                        .putString(MusicActivity.SONG_DURATION, songDetail.get(MusicActivity.SONG_DURATION))
                        .putString(MusicActivity.SONG_PATH, songDetail.get(MusicActivity.SONG_PATH))
                        .putString(MusicActivity.SONG_POS, songDetail.get(MusicActivity.SONG_POS))
                        .putString(MusicActivity.SONG_PROGRESS, songDetail.get(MusicActivity.SONG_PROGRESS));

                prefEditor.commit();
            }
        }).start();
    }

    private static int shufflePos(boolean isPrev) {
        int min = 0, max = (songsList.size() - 1);
        int range = (max - min) + 1;
        int shuffledPos = (int) (Math.random() * range) + min;
        if (!shufflePosList.contains(shuffledPos)) {
            if (!isPrev)
                shufflePosList.add(shuffledPos);
            else {
                int currPos = Integer.parseInt(getPlayingSongPref().get(MusicActivity.SONG_POS));
                if (shufflePosList.contains(currPos)) {
                    shufflePosList.add((shufflePosList.indexOf(currPos)), shuffledPos);
                }
            }
            return shuffledPos;
        }
//        if (shuffledPos != Integer.parseInt(getPlayingSongPref().get(MusicActivity.SONG_POS)))
//            return shuffledPos;
        else if (shufflePosList.size() < songsList.size())
            return shufflePos(isPrev);
        else {
            shufflePosList.clear();
            return shufflePos(isPrev);
        }
    }

    private void createPlayList() {
        new LoadSongsAsync().execute();
    }

    private class LoadSongsAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Cursor cursor = mContext.getContentResolver().query(allsongsuri, projectionSongs,
                    selection, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int i = 0;
                    do {
                        HashMap<String, String> song = new HashMap<String, String>();
                        String song_title = cursor
                                .getString(cursor
                                        .getColumnIndex(MediaStore.Audio.Media.TITLE));
                        String display_name = cursor
                                .getString(cursor
                                        .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                        int song_id = cursor.getInt(cursor
                                .getColumnIndex(MediaStore.Audio.Media._ID));

                        String song_path = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Audio.Media.DATA));

                        String album_name = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Audio.Media.ALBUM));

                        String artist_name = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Audio.Media.ARTIST));

                        int song_duration = cursor.getInt(cursor
                                .getColumnIndex(MediaStore.Audio.Media.DURATION));

                        song.put(MusicActivity.SONG_TITLE, song_title);
                        song.put(MusicActivity.DISPLAY_NAME, display_name);
                        song.put(MusicActivity.SONG_ID, "" + song_id);
                        song.put(MusicActivity.SONG_PATH, song_path);
                        song.put(MusicActivity.ALBUM_NAME, album_name);
                        song.put(MusicActivity.ARTIST_NAME, artist_name);
                        song.put(MusicActivity.SONG_DURATION, "" + song_duration);
                        song.put(MusicActivity.SONG_POS, "" + i);
                        songsList.add(song);
                        ++i;
                    } while (cursor.moveToNext());

                }
                cursor.close();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadSongListener.onSongLoaded();
        }
    }

}
