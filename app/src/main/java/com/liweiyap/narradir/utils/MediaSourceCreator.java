package com.liweiyap.narradir.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSourceFactory;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.util.Assertions;

import org.jetbrains.annotations.Nullable;

public class MediaSourceCreator
{
    public static @Nullable ProgressiveMediaSource createProgressiveMediaSourceFromResId(@NonNull Context context, @RawRes int resId, ExtractorsFactory extractorsFactory)
    {
        if (extractorsFactory == null)
        {
            return null;
        }

        try
        {
            RawResourceDataSource dataSource = new RawResourceDataSource(context);
            DataSpec dataSpec = new DataSpec(RawResourceDataSource.buildRawResourceUri(resId));
            dataSource.open(dataSpec);

            Assertions.checkNotNull(dataSource.getUri());
            MediaItem mediaItem = MediaItem.fromUri(dataSource.getUri());
            MediaSourceFactory mediaSourceFactory = new DefaultMediaSourceFactory(context, extractorsFactory);
            return (ProgressiveMediaSource) mediaSourceFactory.createMediaSource(mediaItem);
        }
        catch (RawResourceDataSource.RawResourceDataSourceException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}