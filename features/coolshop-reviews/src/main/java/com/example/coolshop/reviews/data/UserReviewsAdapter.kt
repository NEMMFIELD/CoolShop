package com.example.coolshop.reviews.data

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.coolshop.reviews.databinding.ItemShowReviewBinding
import com.example.data.UserReviewModel
import com.google.android.material.shape.MaterialShapeDrawable

class UserReviewsAdapter(private val reviewsList:List<UserReviewModel>, private val context:Context) :
    ListAdapter<UserReviewModel, UserReviewsAdapter.ViewHolder>(UserReviewsDiffUtil()) {
    inner class ViewHolder(private val binding: ItemShowReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: UserReviewModel) {
            with(binding) {
                showedUser.text = model.user
                showedReview.text = model.review
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserReviewsAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemShowReviewBinding.inflate(inflater, parent, false)
        val shapeDrawable = MaterialShapeDrawable()
        shapeDrawable.fillColor = ContextCompat.getColorStateList(context,android.R.color.transparent)
        shapeDrawable.setStroke(6.0f,ContextCompat.getColor(context,android.R.color.holo_purple))
        shapeDrawable.setPadding(15,15,15,15)
        ViewCompat.setBackground(binding.sectionCommentary,shapeDrawable)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: UserReviewsAdapter.ViewHolder, position: Int) {
        viewHolder.bind(getItem(position))

    }

    class UserReviewsDiffUtil : DiffUtil.ItemCallback<UserReviewModel>() {
        private val payLoad = Any()
        override fun areItemsTheSame(oldItem: UserReviewModel, newItem: UserReviewModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserReviewModel, newItem: UserReviewModel): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: UserReviewModel, newItem: UserReviewModel): Any? {
            return payLoad
        }

    }
}
