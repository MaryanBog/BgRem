package com.bgrem.presentation.background.select.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bgrem.app.R
import com.bgrem.app.databinding.ItemBackgroundBinding
import com.bgrem.domain.background.model.BackgroundGroup
import com.bgrem.presentation.background.select.model.SelectableBackgroundUi
import com.bgrem.presentation.common.extensions.loadImageByUrl

class BackgroundAdapter(
    private val listener: BackgroundAdapterListener
) : ListAdapter<SelectableBackgroundUi, RecyclerView.ViewHolder>(DIFF) {

    private companion object {
        val DIFF = object : DiffUtil.ItemCallback<SelectableBackgroundUi>() {
            override fun areItemsTheSame(
                oldItem: SelectableBackgroundUi,
                newItem: SelectableBackgroundUi
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: SelectableBackgroundUi,
                newItem: SelectableBackgroundUi
            ): Boolean = oldItem == newItem
        }
    }

    private val addNewBgViewType = R.layout.item_add_new_bg
    private val bgItemViewType = R.layout.item_background

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is SelectableBackgroundUi.AddNewBg -> addNewBgViewType
        else -> bgItemViewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            bgItemViewType -> BackgroundViewHolder(view)
            addNewBgViewType -> AddNewBgViewHolder(view)
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BackgroundViewHolder -> getItem(position)?.let { holder.bind(it) }
            is AddNewBgViewHolder -> Unit
        }
    }

    inner class BackgroundViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemBackgroundBinding.bind(view)

        init {
            initView()
        }

        fun bind(data: SelectableBackgroundUi) = with(binding.backgroundImage) {
            isSelected = data.isSelected
            isVisible = data !is SelectableBackgroundUi.Transparent

            when (data) {
                is SelectableBackgroundUi.Color -> setColor(data)
                is SelectableBackgroundUi.Image -> loadPreview(data.previewUrl)
                is SelectableBackgroundUi.Video -> loadPreview(data.previewUrl)
                is SelectableBackgroundUi.User -> loadPreview(data.previewUrl)
                is SelectableBackgroundUi.Transparent,
                is SelectableBackgroundUi.AddNewBg -> Unit
            }
        }

        private fun loadPreview(url: String) = with(binding.backgroundImage) {
            loadImageByUrl(url)
        }

        private fun setColor(data: SelectableBackgroundUi.Color) = with(binding.backgroundImage) {
            setImageDrawable(ColorDrawable(Color.parseColor(data.color)))
        }

        private fun initView() = with(binding.root) {
            setOnClickListener {
                val item = getItem(bindingAdapterPosition) ?: return@setOnClickListener
                listener.onSelectedBackground(item.id, item.group)
            }
        }
    }

    inner class AddNewBgViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                listener.onAddNewBgClick()
            }
        }
    }

    interface BackgroundAdapterListener {
        fun onSelectedBackground(id: String, group: BackgroundGroup)
        fun onAddNewBgClick()
    }
}