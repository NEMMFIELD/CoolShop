package com.example.coolshop.reviews.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.coolshop.reviews.data.CoolShopReviewsMapper
import com.example.coolshop.reviews.databinding.FragmentAddingReviewBinding
import com.example.data.UserReviewModel
import com.example.database.models.UserReviewDBO
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddingUserReviewFragment : DialogFragment() {
    private var _binding: FragmentAddingReviewBinding? = null
    private val binding get() = _binding
    private val viewModel: AddingUserReviewViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddingReviewBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.buttonCancel?.setOnClickListener { dismiss() }
        viewLifecycleOwner.lifecycleScope.launch {
            binding?.buttonAddReview?.setOnClickListener {
                addReview(
                    CoolShopReviewsMapper.mapReviewModelToReviewDbo(
                        UserReviewModel(
                            id = viewModel.userReviewModel.value.id,
                            user = binding?.userName?.text.toString(),
                            review = binding?.textReview?.text.toString(),
                            productId = viewModel.productId?.toInt()
                        )
                    )
                )
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun addReview(reviewDBO: UserReviewDBO) {
        viewModel.addReview(reviewDBO)
    }
}
