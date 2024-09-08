package com.example.coolshop.details.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.coolshop.details.R
import com.example.coolshop.details.databinding.FragmentCoolShopDetailsBinding
import com.example.database.models.CoolShopDBO
import com.example.state.ApiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CoolShopDetailsFragment : Fragment() {
    private var _binding: FragmentCoolShopDetailsBinding? = null
    private val binding get() = _binding
    private val viewModel: CoolShopDetailsViewModel by viewModels()
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
                               this?.imageView?.load(state.data.imgPath)
                               this?.detailsDescription?.text = state.data.description
                               this?.productCategory?.text =  state.data.category
                               this?.detailsPrice?.text = "Price: ".plus(state.data.price.toString()).plus("$")
                               this?.detailsRate?.text = "Rate: ".plus(state.data.rate.toString())
                           }
                            binding?.btnAddCart?.setOnClickListener{
                                addToCart(com.example.database.utils.Mapper.mapModeltoDBO(state.data))
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
    }

    private fun addToCart(coolShopDBO: CoolShopDBO) {
        viewModel.addToCardProduct(coolShopDBO)
    }

    private fun backToMainScreen() {
        findNavController().navigate(com.example.navigation.R.id.coolShopMainFragment)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = CoolShopDetailsFragment()
    }
}
