package com.liweiyap.narradir.util.audio

import android.content.Context
import android.net.Uri

import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.google.android.exoplayer2.util.Assertions

import com.liweiyap.narradir.R

object ExoPlayerMediaSourceCreator {
    @JvmStatic
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
        } catch (e: RawResourceDataSource.RawResourceDataSourceException) {
            e.printStackTrace()
        }

        return null
    }

    private fun buildRawResourceUri(context: Context?, resName: String?): Uri? {
        if (context == null || resName == null) {
            return null
        }

        // does not look elegant but it's safe because RawRes ID integers are non-constant from Gradle version >4.
        // I think that the URI that is itself loaded and returned wouldn't be non-constant, right??
        return when (resName) {
            context.getString(R.string.avalonintrosegment0nomerlin_key) -> RawResourceDataSource.buildRawResourceUri(R.raw.avalonintrosegment0nomerlin)
            context.getString(R.string.avalonintrosegment0withmerlin_key) -> RawResourceDataSource.buildRawResourceUri(R.raw.avalonintrosegment0withmerlin)
            context.getString(R.string.avalonintrosegment1nooberon_key) -> RawResourceDataSource.buildRawResourceUri(R.raw.avalonintrosegment1nooberon)
            context.getString(R.string.avalonintrosegment1withoberon_key) -> RawResourceDataSource.buildRawResourceUri(R.raw.avalonintrosegment1withoberon)
            context.getString(R.string.avalonintrosegment2_key) -> RawResourceDataSource.buildRawResourceUri(R.raw.avalonintrosegment2)
            context.getString(R.string.avalonintrosegment3nomerlin_key) -> RawResourceDataSource.buildRawResourceUri(R.raw.avalonintrosegment3nomerlin)
            context.getString(R.string.avalonintrosegment3nomordred_key) -> RawResourceDataSource.buildRawResourceUri(R.raw.avalonintrosegment3nomordred)
            context.getString(R.string.avalonintrosegment3withmordred_key) -> RawResourceDataSource.buildRawResourceUri(R.raw.avalonintrosegment3withmordred)
            context.getString(R.string.avalonintrosegment4_key) -> RawResourceDataSource.buildRawResourceUri(R.raw.avalonintrosegment4)
            context.getString(R.string.avalonintrosegment5nopercival_key) -> RawResourceDataSource.buildRawResourceUri(R.raw.avalonintrosegment5nopercival)
            context.getString(R.string.avalonintrosegment5withpercivalnomorgana_key) -> RawResourceDataSource.buildRawResourceUri(R.raw.avalonintrosegment5withpercivalnomorgana)
            context.getString(R.string.avalonintrosegment5withpercivalwithmorgana_key) -> RawResourceDataSource.buildRawResourceUri(R.raw.avalonintrosegment5withpercivalwithmorgana)
            context.getString(R.string.avalonintrosegment6withpercivalnomorgana_key) -> RawResourceDataSource.buildRawResourceUri(R.raw.avalonintrosegment6withpercivalnomorgana)
            context.getString(R.string.avalonintrosegment6withpercivalwithmorgana_key) -> RawResourceDataSource.buildRawResourceUri(R.raw.avalonintrosegment6withpercivalwithmorgana)
            context.getString(R.string.avalonintrosegment7_key) -> RawResourceDataSource.buildRawResourceUri(R.raw.avalonintrosegment7)
            context.getString(R.string.secrethitlerintrosegment0small_key) -> RawResourceDataSource.buildRawResourceUri(R.raw.secrethitlerintrosegment0small)
            context.getString(R.string.secrethitlerintrosegment1small_key) -> RawResourceDataSource.buildRawResourceUri(R.raw.secrethitlerintrosegment1small)
            context.getString(R.string.secrethitlerintrosegment2small_key) -> RawResourceDataSource.buildRawResourceUri(R.raw.secrethitlerintrosegment2small)
            context.getString(R.string.secrethitlerintrosegment3small_key) -> RawResourceDataSource.buildRawResourceUri(R.raw.secrethitlerintrosegment3small)
            context.getString(R.string.secrethitlerintrosegment0large_key) -> RawResourceDataSource.buildRawResourceUri(R.raw.secrethitlerintrosegment0large)
            context.getString(R.string.secrethitlerintrosegment1large_key) -> RawResourceDataSource.buildRawResourceUri(R.raw.secrethitlerintrosegment1large)
            context.getString(R.string.secrethitlerintrosegment2large_key) -> RawResourceDataSource.buildRawResourceUri(R.raw.secrethitlerintrosegment2large)
            context.getString(R.string.secrethitlerintrosegment3large_key) -> RawResourceDataSource.buildRawResourceUri(R.raw.secrethitlerintrosegment3large)
            context.getString(R.string.secrethitlerintrosegment4large_key) -> RawResourceDataSource.buildRawResourceUri(R.raw.secrethitlerintrosegment4large)
            else -> null
        }
    }
}