package com.example.coolshop.cart.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.coolshop.cart.databinding.CartItemBinding
import com.example.data.CoolShopModel

class CartAdapter internal constructor(private val itemClickListener: ClickListener) :
    ListAdapter<CoolShopModel, CartAdapter.ViewHolder>(
        CoolShopDiffUtil()
    ) {
    inner class ViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: CoolShopModel) {
            with(binding) {
                cartTitle.text = model.title
                cartImage.load(model.imgPath)
                cartPrice.text = model.price.toString().plus(" $")
                cartRemove.setOnClickListener { itemClickListener.clickItem(model) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CartItemBinding.inflate(inflater, parent, false)
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

internal interface ClickListener {
    fun clickItem(item: CoolShopModel)
}
