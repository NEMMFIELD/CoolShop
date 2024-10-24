package com.example.coolshop.main.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.coolshop.main.R
import com.example.coolshop.main.databinding.RecyclerviewItemBinding
import com.example.data.CoolShopModel


internal class CoolShopAdapter(
    private val data: List<CoolShopModel>,
    private val favouritesListener: SetFavourites,
    private val itemClickListener: ClickListener,
) : ListAdapter<CoolShopModel, CoolShopAdapter.ViewHolder>(CoolShopDiffUtil()) {

    inner class ViewHolder(private val binding: RecyclerviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: CoolShopModel) {
            with(binding) {
                titleProduct.text = model.title
                imageProduct.load(model.imgPath)

                priceProduct.text =
                    model.price.toString().plus(root.context.getString(R.string.dollar))

                rateProduct.text =
                    root.context.getString(R.string.rating).plus(model.rate.toString())

                if (model.isLiked) favouriteProduct.setImageResource(R.drawable.hearton)
                else favouriteProduct.setImageResource(R.drawable.heart)

               favouriteProduct.setOnClickListener {
                    favouritesListener.like(
                        model,
                        position = absoluteAdapterPosition
                    )
                }
                imageProduct.setOnClickListener { itemClickListener.clickItem(model) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerviewItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
    }


    class CoolShopDiffUtil : DiffUtil.ItemCallback<CoolShopModel>() {
        private val payLoad = Any()
        override fun areItemsTheSame(
            oldItem: CoolShopModel,
            newItem: CoolShopModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CoolShopModel,
            newItem: CoolShopModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(
            oldItem: CoolShopModel,
            newItem: CoolShopModel
        ): Any {
            return payLoad
        }
    }
}

interface ClickListener {
    fun clickItem(item: CoolShopModel)
}

internal interface SetFavourites {
    fun like(product: CoolShopModel, position: Int)
}
