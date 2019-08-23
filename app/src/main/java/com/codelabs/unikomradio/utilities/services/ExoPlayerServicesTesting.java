//package com.codelabs.unikomradio.utilities.services;
//
//import android.content.Context;
//import android.net.Uri;
//import android.os.Handler;
//import com.google.android.exoplayer2.C;
//import com.google.android.exoplayer2.ExoPlayer;
//import com.google.android.exoplayer2.ExoPlayerFactory;
//import com.google.android.exoplayer2.SimpleExoPlayer;
//import com.google.android.exoplayer2.audio.AudioAttributes;
//import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
//import com.google.android.exoplayer2.extractor.ExtractorsFactory;
//import com.google.android.exoplayer2.source.ExtractorMediaSource;
//import com.google.android.exoplayer2.source.MediaSource;
//import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
//import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
//import com.google.android.exoplayer2.trackselection.TrackSelection;
//import com.google.android.exoplayer2.upstream.*;
//import com.google.android.exoplayer2.util.Util;
//import timber.log.Timber;
//
//public class ExoPlayerServicesTesting {
//    public ExoPlayerServicesTesting(Context context){
//
//        @C.AudioUsage int usage = Util.getAudioUsageForStreamType(C.STREAM_TYPE_MUSIC);
//        @C.AudioContentType int contentType = Util.getAudioContentTypeForStreamType(C.STREAM_TYPE_MUSIC);
//        AudioAttributes audioAttributes =
//                new AudioAttributes.Builder().setUsage(usage).setContentType(contentType).build();
//
//        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//        final ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
//        TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
//        DataSource.Factory dateSourceFactory = new DefaultDataSourceFactory(context,Util.getUserAgent(context,context.getPackageName()), (TransferListener<? super DataSource>) bandwidthMeter);
//        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse("http://hits.unikom.ac.id:9996/;listen.pls?sid=1"), dateSourceFactory, extractorsFactory, new Handler(), Throwable::printStackTrace);    // replace Uri with your song url
//        ExoPlayer exoPlayer = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector(trackSelectionFactory));
//        exoPlayer.prepare(mediaSource);
//        ((SimpleExoPlayer) exoPlayer).setAudioAttributes(audioAttributes);
//        exoPlayer.setPlayWhenReady(true);
//        ((SimpleExoPlayer) exoPlayer).setVolume(1f);
//
//
//        Timber.i("STATE EXOPLAYER : "+exoPlayer.getPlaybackState() );
//    }
//}
