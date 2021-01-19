package com.android.youtubetips.category.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.youtubetips.R
import com.android.youtubetips.category.model.response.VideoItem
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.NativeAdOptions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_category.view.*

const val ARABIC_ITEM = 0
const val ENGLISH_ITEM = 1
const val AD_TYPE = 2

class CategoryRecyclerViewAdapter(
    private val values: List<VideoItem>,
    private val isArabic: Boolean,
    private val onVideoClick: (VideoItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val from = LayoutInflater.from(parent.context)
        val view = when (viewType) {
            ARABIC_ITEM -> from.inflate(R.layout.item_video_arabic, parent, false)
            ENGLISH_ITEM -> from.inflate(R.layout.item_video_english, parent, false)
            AD_TYPE -> from.inflate(R.layout.item_native_ad, parent, false)
            else -> from.inflate(R.layout.item_category, parent, false)
        }
        return if (viewType == AD_TYPE) AdViewHolder(view) else ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewType = getItemViewType(position)

        if (itemViewType == ARABIC_ITEM || itemViewType == ENGLISH_ITEM) {
            val item = values[position]
            (holder as ViewHolder).bind(item, position)
        } else {
            val adHolder = holder as AdViewHolder
            val adLoader: AdLoader = AdLoader.Builder(
                adHolder.itemView.context,
                adHolder.itemView.context.getString(R.string.native_ad_unit_id)
            )
                .forUnifiedNativeAd { unifiedNativeAd -> // Show the ad.

                    val styles = NativeTemplateStyle.Builder().build()
                    val template: TemplateView =
                        adHolder.itemView as TemplateView
                    template.setStyles(styles)
                    template.setNativeAd(unifiedNativeAd)


                }
                .withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(errorCode: Int) {
                        // Handle the failure by logging, altering the UI, and so on.
                    }
                })
                .withNativeAdOptions(
                    NativeAdOptions.Builder() // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build()
                )
                .build()
            adLoader.loadAd(AdRequest.Builder().build())
        }
    }

    override fun getItemCount(): Int = values.size

    override fun getItemViewType(position: Int) =
        if (position % 5 == 0) AD_TYPE else if (isArabic) ARABIC_ITEM else ENGLISH_ITEM

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(video: VideoItem, position: Int) = with(itemView) {
            tvCategoryTitle.text = video.snippet.title
            with(video.snippet.thumbnails) {
                standard?.let {
                    Picasso.get().load(standard.url).into(ivCategory)
                    return@with
                }
                high?.let {
                    Picasso.get().load(high.url).into(ivCategory)
                    return@with
                }
                medium?.let {
                    Picasso.get().load(medium.url).into(ivCategory)
                    return@with
                }
                default?.let {
                    Picasso.get().load(default.url).into(ivCategory)
                    return@with
                }
            }
            setOnClickListener { onVideoClick.invoke(video) }
        }

    }

    inner class AdViewHolder(view: View) : RecyclerView.ViewHolder(view) {}
}