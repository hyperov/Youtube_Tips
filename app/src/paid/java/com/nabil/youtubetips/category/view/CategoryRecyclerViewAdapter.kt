package com.nabil.youtubetips.category.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nabil.youtubetips.R
import com.nabil.youtubetips.category.model.response.VideoItem
import com.nabil.youtubetips.home.ARABIC_ITEM
import com.nabil.youtubetips.home.ENGLISH_ITEM
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryRecyclerViewAdapter(
    private val values: List<VideoItem>,
    private val isArabic: Boolean,
    private val onVideoClick: (VideoItem) -> Unit
) : RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val from = LayoutInflater.from(parent.context)
        val view = when (viewType) {
            ARABIC_ITEM -> from.inflate(R.layout.item_video_arabic, parent, false)
            ENGLISH_ITEM -> from.inflate(R.layout.item_video_english, parent, false)
            else -> from.inflate(R.layout.item_category, parent, false)
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemViewType = getItemViewType(position)

        if (itemViewType == ARABIC_ITEM || itemViewType == ENGLISH_ITEM) {
            val item = values[position]
            holder.bind(item, position)
        }
    }

    override fun getItemCount(): Int = values.size

    override fun getItemViewType(position: Int) =
        if (isArabic) ARABIC_ITEM else ENGLISH_ITEM


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

}