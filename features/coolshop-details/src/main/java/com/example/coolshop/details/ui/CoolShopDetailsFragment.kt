package com.example.coolshop.details.ui

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.coolshop.details.R
import com.example.coolshop.details.data.CoolShopDetailsMapper
import com.example.coolshop.details.databinding.FragmentCoolShopDetailsBinding
import com.example.coolshop.reviews.ui.AddingUserReviewFragment
import com.example.database.models.CoolShopDBO
import com.example.state.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TOKEN = "token"
const val PRODUCT_ID = "productId"
@AndroidEntryPoint
class CoolShopDetailsFragment : Fragment() {
    private var _binding: FragmentCoolShopDetailsBinding? = null
    private val binding get() = _binding
    private val viewModel: CoolShopDetailsViewModel by viewModels()
    private val bundle = Bundle()

    @Inject
    lateinit var sharedPreferences: SharedPreferences
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
        val currentUser = sharedPreferences.getString(TOKEN, null)
        binding?.dialogShow?.isEnabled = !currentUser.isNullOrEmpty()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.postProduct.collect { state ->
                    when (state) {
                        is State.Success -> {
                            with(binding) {
                                this?.detailsTitle?.text = state.data.title
                                this?.detailsProductImage?.load(state.data.imgPath)
                                this?.detailsDescription?.text = state.data.description
                                this?.productCategory?.text = state.data.category
                                this?.detailsPrice?.text =
                                    requireContext().getString(R.string.prefixPrice)
                                        .plus(state.data.price.toString()).plus(
                                            requireContext().getString(
                                                R.string.dollar
                                            )
                                        )

                                this?.detailsRate?.text =
                                    requireContext().getString(R.string.prefixRate)
                                        .plus(state.data.rate.toString())
                            }
                            binding?.buttonAddToCart?.setOnClickListener {
                                addToCart(CoolShopDetailsMapper.mapModelToDBO(state.data))
                            }
                        }

                        is State.Failure -> {
                            Log.d("TagError", "Error ${state.message}")
                        }

                        else -> {}
                    }
                }
            }
        }
        binding?.buttonBack?.setOnClickListener {
            backToMainScreen()
        }

        binding?.dialogShow?.setOnClickListener {
            createDialogFragment()
        }

        binding?.buttonShowReviews?.setOnClickListener {
            navigateToReviews()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun createDialogFragment() {
        val showPopUp = AddingUserReviewFragment()
        bundle.putString(PRODUCT_ID, viewModel.id)
        showPopUp.arguments = bundle
        showPopUp.show((activity as AppCompatActivity).supportFragmentManager, "showPopUp")
    }

    private fun navigateToReviews() {
        val uri = Uri.Builder()
            .scheme("android-app")
            .authority("com.example.coolshop.reviews.ui")
            .appendPath("showingReviewsFragment")
            .appendPath(viewModel.id.toString())  // Используйте id из ViewModel
            .build()

        val request = NavDeepLinkRequest.Builder
            .fromUri(uri)
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
