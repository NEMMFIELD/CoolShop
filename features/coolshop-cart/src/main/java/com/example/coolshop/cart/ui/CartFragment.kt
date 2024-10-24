package com.example.coolshop.cart.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coolshop.cart.R
import com.example.coolshop.cart.data.CartMapper
import com.example.coolshop.cart.databinding.FragmentCartBinding
import com.example.data.CoolShopModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment(), ClickListener {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding
    private val viewModel: CartViewModel by viewModels()
    private var cartAdapter: CartAdapter? = null
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.productsInCart.collect { productsListInDatabase ->
                    cartAdapter?.submitList(productsListInDatabase.map { productsInDatabase ->
                        CartMapper.mapModelDBOToModel(productsInDatabase)
                    })
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.totalPrice.collect { totalSum ->
                    binding?.totalPrice?.text =
                        totalSum.toString().plus(context?.getString(R.string.dollar))
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        cartAdapter = null
        recyclerView = null
        super.onDestroyView()
    }

    private fun setupRecycler() {
        recyclerView = binding!!.cardRecyclerview
        recyclerView?.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        cartAdapter = CartAdapter(this)
        recyclerView?.adapter = cartAdapter
    }


    override fun clickItem(item: CoolShopModel) {
        viewModel.removeItem(CartMapper.mapModelToDBO(item))
    }
}
