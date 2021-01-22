package com.android.youtubetips.categories.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.youtubetips.R
import com.android.youtubetips.categories.model.CategoryItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_category.view.*


class CategoryListRecyclerViewAdapter(
    private val values: List<CategoryItem>,
    private val onCategoryItemClick: ((category: CategoryItem) -> Unit)
) : RecyclerView.Adapter<CategoryListRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.bind(item, position)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(category: CategoryItem, position: Int) = with(itemView) {
            tvCategoryTitle.text = category.name
            Picasso.get().load(category.imageRes).into(ivCategory)
            setOnClickListener { onCategoryItemClick.invoke(category) }
        }

    }

}