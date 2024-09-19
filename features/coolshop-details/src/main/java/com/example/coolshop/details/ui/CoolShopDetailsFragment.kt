package com.example.coolshop.details.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.coolshop.details.databinding.FragmentCoolShopDetailsBinding
import com.example.coolshop.reviews.ui.AddingUserReviewFragment
import com.example.database.models.CoolShopDBO
import com.example.state.ApiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CoolShopDetailsFragment : Fragment() {
    private var _binding: FragmentCoolShopDetailsBinding? = null
    private val binding get() = _binding
    private val viewModel: CoolShopDetailsViewModel by viewModels()
    private val bundle = Bundle()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCoolShopDetailsBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.postStateFlow.collect { state ->
                    when (state) {
                        is ApiState.Success -> {
                           with (binding) {
                               this?.detailsTitle?.text = state.data.title
                               this?.detailsProductImage?.load(state.data.imgPath)
                               this?.detailsDescription?.text = state.data.description
                               this?.productCategory?.text =  state.data.category
                               this?.detailsPrice?.text = "Price: ".plus(state.data.price.toString()).plus("$")
                               this?.detailsRate?.text = "Rate: ".plus(state.data.rate.toString())
                           }
                            binding?.btnAddCart?.setOnClickListener{
                                addToCart(com.example.utils.Mapper.mapModelToDBO(state.data))
                            }
                        }
                        is ApiState.Failure -> {
                            Log.d("TagError","Error ${state.message}")
                        }
                        else -> {}
                    }
                }
            }
        }
        binding?.btnBack?.setOnClickListener {
           backToMainScreen()
        }

        binding?.dialogShow?.setOnClickListener {
          createDialogFragment()
        }

        binding?.btnShowReviews?.setOnClickListener {
           navigateToReviews()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun createDialogFragment() {
        val showPopUp = AddingUserReviewFragment()
        bundle.putString("productId",viewModel.id)
        showPopUp.arguments = bundle
        showPopUp.show((activity as AppCompatActivity).supportFragmentManager,"showPopUp")
    }

    private fun navigateToReviews() {
        val request = NavDeepLinkRequest.Builder
            .fromUri("android-app://com.example.coolshop.reviews.ui/showingReviewsFragment/${viewModel.id}".toUri())
            .build()
        findNavController().navigate(request)
    }

    private fun addToCart(coolShopDBO: CoolShopDBO) {
        viewModel.addToCardProduct(coolShopDBO)
    }

    private fun backToMainScreen() {
       findNavController().popBackStack()
    }

    companion object {
        @JvmStatic
        fun newInstance() = CoolShopDetailsFragment()
    }
}
