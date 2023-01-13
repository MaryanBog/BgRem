package com.bgrem.presentation.background.select.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bgrem.app.R
import com.bgrem.app.databinding.ItemBackgroundGroupBinding
import com.bgrem.presentation.background.select.model.SelectableBackgroundUi
import com.bgrem.presentation.common.decoration.HorizontalItemDecoration
import com.bgrem.presentation.common.extensions.dpToPx

class BackgroundGroupAdapter(
    private val backgroundAdapterListener: BackgroundAdapter.BackgroundAdapterListener
) : RecyclerView.Adapter<BackgroundGroupAdapter.ViewHolder>() {

    private val groups = mutableListOf<List<SelectableBackgroundUi>>()

    override fun getItemCount(): Int = groups.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_background_group, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(groups[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(data: List<List<SelectableBackgroundUi>>) {
        groups.apply {
            clear()
            addAll(data)
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding by viewBinding(ItemBackgroundGroupBinding::bind)
        private val backgroundAdapter = BackgroundAdapter(backgroundAdapterListener)

        init {
            binding.backgroundRecycler.apply {
                itemAnimator = null
                adapter = backgroundAdapter
                addItemDecoration(HorizontalItemDecoration(
                    horizontalMargin = 16.dpToPx,
                    firstStartMargin = 8.dpToPx,
                    lastEndMargin = 8.dpToPx
                ))
            }
        }

        fun bind(data: List<SelectableBackgroundUi>) {
            backgroundAdapter.submitList(data)
        }
    }
}