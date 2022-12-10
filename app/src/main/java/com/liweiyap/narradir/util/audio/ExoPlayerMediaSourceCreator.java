package com.liweiyap.narradir.util.audio;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.util.Assertions;
import com.liweiyap.narradir.R;

public final class ExoPlayerMediaSourceCreator
{
    private ExoPlayerMediaSourceCreator(){}

    public static @Nullable ProgressiveMediaSource createProgressiveMediaSourceFromRes(final Context context, final String resName, final ExtractorsFactory extractorsFactory)
    {
        if ((context == null) || (resName == null) || (extractorsFactory == null))
        {
            return null;
        }

        try
        {
            final Uri resUri = buildRawResourceUri(context, resName);
            if (resUri == null)
            {
                return null;
            }

            RawResourceDataSource dataSource = new RawResourceDataSource(context);
            DataSpec dataSpec = new DataSpec(resUri);
            dataSource.open(dataSpec);

            Assertions.checkNotNull(dataSource.getUri());
            MediaItem mediaItem = MediaItem.fromUri(dataSource.getUri());
            MediaSource.Factory mediaSourceFactory = new DefaultMediaSourceFactory(context, extractorsFactory);
            return (ProgressiveMediaSource) mediaSourceFactory.createMediaSource(mediaItem);
        }
        catch (RawResourceDataSource.RawResourceDataSourceException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    private static @Nullable Uri buildRawResourceUri(final Context context, final String resName)
    {
        if ((context == null) || (resName == null))
        {
            return null;
        }

        // does not look elegant but it's safe because RawRes ID integers are non-constant from Gradle version >4.
        // I think that the URI that is itself loaded and returned wouldn't be non-constant, right??
        if (resName.equals(context.getString(R.string.avalonintrosegment0_key)))
        {
            return RawResourceDataSource.buildRawResourceUri(R.raw.avalonintrosegment0);
        }
        else if (resName.equals(context.getString(R.string.avalonintrosegment1nooberon_key)))
        {
            return RawResourceDataSource.buildRawResourceUri(R.raw.avalonintrosegment1nooberon);
        }
        else if (resName.equals(context.getString(R.string.avalonintrosegment1withoberon_key)))
        {
            return RawResourceDataSource.buildRawResourceUri(R.raw.avalonintrosegment1withoberon);
        }
        else if (resName.equals(context.getString(R.string.avalonintrosegment2_key)))
        {
            return RawResourceDataSource.buildRawResourceUri(R.raw.avalonintrosegment2);
        }
        else if (resName.equals(context.getString(R.string.avalonintrosegment3nomerlin_key)))
        {
            return RawResourceDataSource.buildRawResourceUri(R.raw.avalonintrosegment3nomerlin);
        }
        else if (resName.equals(context.getString(R.string.avalonintrosegment3nomordred_key)))
        {
            return RawResourceDataSource.buildRawResourceUri(R.raw.avalonintrosegment3nomordred);
        }
        else if (resName.equals(context.getString(R.string.avalonintrosegment3withmordred_key)))
        {
            return RawResourceDataSource.buildRawResourceUri(R.raw.avalonintrosegment3withmordred);
        }
        else if (resName.equals(context.getString(R.string.avalonintrosegment4_key)))
        {
            return RawResourceDataSource.buildRawResourceUri(R.raw.avalonintrosegment4);
        }
        else if (resName.equals(context.getString(R.string.avalonintrosegment5nopercival_key)))
        {
            return RawResourceDataSource.buildRawResourceUri(R.raw.avalonintrosegment5nopercival);
        }
        else if (resName.equals(context.getString(R.string.avalonintrosegment5withpercivalnomorgana_key)))
        {
            return RawResourceDataSource.buildRawResourceUri(R.raw.avalonintrosegment5withpercivalnomorgana);
        }
        else if (resName.equals(context.getString(R.string.avalonintrosegment5withpercivalwithmorgana_key)))
        {
            return RawResourceDataSource.buildRawResourceUri(R.raw.avalonintrosegment5withpercivalwithmorgana);
        }
        else if (resName.equals(context.getString(R.string.avalonintrosegment6withpercivalnomorgana_key)))
        {
            return RawResourceDataSource.buildRawResourceUri(R.raw.avalonintrosegment6withpercivalnomorgana);
        }
        else if (resName.equals(context.getString(R.string.avalonintrosegment6withpercivalwithmorgana_key)))
        {
            return RawResourceDataSource.buildRawResourceUri(R.raw.avalonintrosegment6withpercivalwithmorgana);
        }
        else if (resName.equals(context.getString(R.string.avalonintrosegment7_key)))
        {
            return RawResourceDataSource.buildRawResourceUri(R.raw.avalonintrosegment7);
        }
        else if (resName.equals(context.getString(R.string.secrethitlerintrosegment0small_key)))
        {
            return RawResourceDataSource.buildRawResourceUri(R.raw.secrethitlerintrosegment0small);
        }
        else if (resName.equals(context.getString(R.string.secrethitlerintrosegment1small_key)))
        {
            return RawResourceDataSource.buildRawResourceUri(R.raw.secrethitlerintrosegment1small);
        }
        else if (resName.equals(context.getString(R.string.secrethitlerintrosegment2small_key)))
        {
            return RawResourceDataSource.buildRawResourceUri(R.raw.secrethitlerintrosegment2small);
        }
        else if (resName.equals(context.getString(R.string.secrethitlerintrosegment3small_key)))
        {
            return RawResourceDataSource.buildRawResourceUri(R.raw.secrethitlerintrosegment3small);
        }
        else if (resName.equals(context.getString(R.string.secrethitlerintrosegment0large_key)))
        {
            return RawResourceDataSource.buildRawResourceUri(R.raw.secrethitlerintrosegment0large);
        }
        else if (resName.equals(context.getString(R.string.secrethitlerintrosegment1large_key)))
        {
            return RawResourceDataSource.buildRawResourceUri(R.raw.secrethitlerintrosegment1large);
        }
        else if (resName.equals(context.getString(R.string.secrethitlerintrosegment2large_key)))
        {
            return RawResourceDataSource.buildRawResourceUri(R.raw.secrethitlerintrosegment2large);
        }
        else if (resName.equals(context.getString(R.string.secrethitlerintrosegment3large_key)))
        {
            return RawResourceDataSource.buildRawResourceUri(R.raw.secrethitlerintrosegment3large);
        }
        else if (resName.equals(context.getString(R.string.secrethitlerintrosegment4large_key)))
        {
            return RawResourceDataSource.buildRawResourceUri(R.raw.secrethitlerintrosegment4large);
        }

        return null;
    }
}