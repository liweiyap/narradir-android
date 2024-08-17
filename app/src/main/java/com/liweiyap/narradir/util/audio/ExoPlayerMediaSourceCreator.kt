package com.liweiyap.narradir.util.audio

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Assertions
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSpec
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.extractor.ExtractorsFactory
import com.liweiyap.narradir.R

object ExoPlayerMediaSourceCreator {
    @OptIn(UnstableApi::class)
    fun createProgressiveMediaSourceFromRes(context: Context?, resName: String?, extractorsFactory: ExtractorsFactory?): ProgressiveMediaSource? {
        if ((context == null) || (resName == null) || (extractorsFactory == null)) {
            return null
        }

        try {
            val resUri: Uri = buildRawResourceUri(context, resName)
                ?: return null

            val dataSource = RawResourceDataSource(context)
            val dataSpec = DataSpec(resUri)
            dataSource.open(dataSpec)

            Assertions.checkNotNull(dataSource.uri)
            val mediaItem: MediaItem = MediaItem.fromUri(dataSource.uri!!)
            val mediaSourceFactory: MediaSource.Factory = DefaultMediaSourceFactory(context, extractorsFactory)
            return mediaSourceFactory.createMediaSource(mediaItem) as ProgressiveMediaSource
        }
        catch (e: RawResourceDataSource.RawResourceDataSourceException) {
            e.printStackTrace()
        }

        return null
    }

    @OptIn(UnstableApi::class)
    private fun buildRawResourceUri(context: Context?, resName: String?): Uri? {
        if (context == null || resName == null) {
            return null
        }

        val rawResourceUriBuilder: Uri.Builder = Uri.Builder().scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)

        // does not look elegant but it's safe because RawRes ID integers are non-constant from Gradle version >4.
        // I think that the URI that is itself loaded and returned wouldn't be non-constant, right??
        return when (resName) {
            context.getString(R.string.avalonintrosegment0nomerlin_key) -> rawResourceUriBuilder.path(R.raw.avalonintrosegment0nomerlin.toString()).build()
            context.getString(R.string.avalonintrosegment0withmerlin_key) -> rawResourceUriBuilder.path(R.raw.avalonintrosegment0withmerlin.toString()).build()
            context.getString(R.string.avalonintrosegment1nooberon_key) -> rawResourceUriBuilder.path(R.raw.avalonintrosegment1nooberon.toString()).build()
            context.getString(R.string.avalonintrosegment1withoberon_key) -> rawResourceUriBuilder.path(R.raw.avalonintrosegment1withoberon.toString()).build()
            context.getString(R.string.avalonintrosegment2_key) -> rawResourceUriBuilder.path(R.raw.avalonintrosegment2.toString()).build()
            context.getString(R.string.avalonintrosegment3nomerlin_key) -> rawResourceUriBuilder.path(R.raw.avalonintrosegment3nomerlin.toString()).build()
            context.getString(R.string.avalonintrosegment3nomordred_key) -> rawResourceUriBuilder.path(R.raw.avalonintrosegment3nomordred.toString()).build()
            context.getString(R.string.avalonintrosegment3withmordred_key) -> rawResourceUriBuilder.path(R.raw.avalonintrosegment3withmordred.toString()).build()
            context.getString(R.string.avalonintrosegment4_key) -> rawResourceUriBuilder.path(R.raw.avalonintrosegment4.toString()).build()
            context.getString(R.string.avalonintrosegment5nopercival_key) -> rawResourceUriBuilder.path(R.raw.avalonintrosegment5nopercival.toString()).build()
            context.getString(R.string.avalonintrosegment5withpercivalnomorgana_key) -> rawResourceUriBuilder.path(R.raw.avalonintrosegment5withpercivalnomorgana.toString()).build()
            context.getString(R.string.avalonintrosegment5withpercivalwithmorgana_key) -> rawResourceUriBuilder.path(R.raw.avalonintrosegment5withpercivalwithmorgana.toString()).build()
            context.getString(R.string.avalonintrosegment6withpercivalnomorgana_key) -> rawResourceUriBuilder.path(R.raw.avalonintrosegment6withpercivalnomorgana.toString()).build()
            context.getString(R.string.avalonintrosegment6withpercivalwithmorgana_key) -> rawResourceUriBuilder.path(R.raw.avalonintrosegment6withpercivalwithmorgana.toString()).build()
            context.getString(R.string.avalonintrosegment7_key) -> rawResourceUriBuilder.path(R.raw.avalonintrosegment7.toString()).build()
            context.getString(R.string.secrethitlerintrosegment0small_key) -> rawResourceUriBuilder.path(R.raw.secrethitlerintrosegment0small.toString()).build()
            context.getString(R.string.secrethitlerintrosegment1small_key) -> rawResourceUriBuilder.path(R.raw.secrethitlerintrosegment1small.toString()).build()
            context.getString(R.string.secrethitlerintrosegment2small_key) -> rawResourceUriBuilder.path(R.raw.secrethitlerintrosegment2small.toString()).build()
            context.getString(R.string.secrethitlerintrosegment3small_key) -> rawResourceUriBuilder.path(R.raw.secrethitlerintrosegment3small.toString()).build()
            context.getString(R.string.secrethitlerintrosegment0large_key) -> rawResourceUriBuilder.path(R.raw.secrethitlerintrosegment0large.toString()).build()
            context.getString(R.string.secrethitlerintrosegment1large_key) -> rawResourceUriBuilder.path(R.raw.secrethitlerintrosegment1large.toString()).build()
            context.getString(R.string.secrethitlerintrosegment2large_key) -> rawResourceUriBuilder.path(R.raw.secrethitlerintrosegment2large.toString()).build()
            context.getString(R.string.secrethitlerintrosegment3large_key) -> rawResourceUriBuilder.path(R.raw.secrethitlerintrosegment3large.toString()).build()
            context.getString(R.string.secrethitlerintrosegment4large_key) -> rawResourceUriBuilder.path(R.raw.secrethitlerintrosegment4large.toString()).build()
            else -> null
        }
    }
}