package com.example.coolshop.cart.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coolshop.cart.data.CartAdapter
import com.example.coolshop.cart.data.ClickListener
import com.example.coolshop.cart.databinding.FragmentCartBinding
import com.example.data.CoolShopModel
import com.example.database.utils.Mapper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment(), ClickListener {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding
    private val viewModel: CartViewModel by viewModels()
    private val cartAdapter by lazy { CartAdapter(this) }
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        viewModel.cartedProducts.observe(viewLifecycleOwner) {
            cartAdapter.submitList(it.map { Mapper.mapModelDBOtoModel(it) })
        }
        viewModel.totalPrice.observe(viewLifecycleOwner) {
            binding?.totalPrice?.text = it.toString().plus("$")
        }

    }

    private fun setupRecycler() {
        recyclerView = binding!!.cardRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        recyclerView.adapter = cartAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = CartFragment()
    }

    override fun clickItem(item: CoolShopModel) {
        viewModel.removeItem(Mapper.mapModeltoDBO(item))
    }
}
