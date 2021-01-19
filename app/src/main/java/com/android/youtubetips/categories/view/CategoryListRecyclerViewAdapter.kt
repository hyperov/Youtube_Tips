package com.android.youtubetips.categories.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.youtubetips.R
import com.android.youtubetips.categories.model.CategoryItem
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.NativeAdOptions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_category.view.*


const val ITEM_TYPE = 0
const val AD_TYPE = 1

class CategoryListRecyclerViewAdapter(
    private val values: List<CategoryItem>,
    private val onCategoryItemClick: ((category: CategoryItem) -> Unit)
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val res = if (viewType == ITEM_TYPE) R.layout.item_category else R.layout.item_native_ad
        val view = LayoutInflater.from(parent.context).inflate(res, parent, false)

        return if (viewType == ITEM_TYPE) ViewHolder(view) else AdViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewType = getItemViewType(position)

        if (itemViewType == ITEM_TYPE) {
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

    override fun getItemViewType(position: Int): Int {
        return if (position % 5 == 0)
            AD_TYPE
        else
            ITEM_TYPE
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(category: CategoryItem, position: Int) = with(itemView) {
            tvCategoryTitle.text = category.name
            Picasso.get().load(category.imageRes).into(ivCategory)
            setOnClickListener { onCategoryItemClick.invoke(category) }
        }

    }

    inner class AdViewHolder(view: View) : RecyclerView.ViewHolder(view) {}
}