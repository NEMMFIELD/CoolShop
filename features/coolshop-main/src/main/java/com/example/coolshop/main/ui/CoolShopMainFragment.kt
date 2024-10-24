package com.example.coolshop.main.ui

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coolshop.main.R
import com.example.coolshop.main.databinding.FragmentCoolShopMainBinding
import com.example.data.CoolShopModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CoolShopMainFragment : Fragment(), SetFavourites, ClickListener {
    private var _binding: FragmentCoolShopMainBinding? = null
    private val binding get() = _binding
    private var coolShopAdapter: CoolShopAdapter? = null
    private var recyclerView: RecyclerView? = null
    private val viewModel: CoolShopViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCoolShopMainBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view
    }

    override fun onResume() {
        super.onResume()
        //Смена списка в зависимости от выбранной категории
        val categories = resources.getStringArray(R.array.categories_array)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, categories)

        binding?.autoCompleteTextView?.setAdapter(arrayAdapter)

        binding?.autoCompleteTextView?.setOnItemClickListener { parent, _, position, _ ->
            if (parent.adapter.getItem(position) == "all") viewModel.loadProducts()
            else viewModel.loadCategoryProducts(parent?.adapter?.getItem(position).toString())
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.postProducts.collect { state ->
                    when (state) {
                        is com.example.state.State.Success -> {
                            state.data.forEach { product ->
                                viewModel.loadFavourites(product)
                            }
                            coolShopAdapter?.submitList(state.data)
                        }

                        is com.example.state.State.Failure -> {
                            Log.d("TagError", "On Create ${state.message}")
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        recyclerView = null
        coolShopAdapter = null
        super.onDestroyView()
    }

    private fun setupRecycler() {

        val spanCount = if (activity?.resources?.configuration?.orientation !=
            Configuration.ORIENTATION_PORTRAIT
        ) 4 else 2

        recyclerView = binding!!.recyclerViewProducts
        recyclerView?.layoutManager = GridLayoutManager(requireContext(), spanCount)
        coolShopAdapter = CoolShopAdapter(emptyList(), this, this)
        recyclerView?.adapter = coolShopAdapter

        val itemDecoration = ItemOffsetDecoration(10)
        recyclerView?.addItemDecoration(itemDecoration)
    }

    override fun like(product: CoolShopModel, position: Int) {
        product.isLiked = !product.isLiked
        viewLifecycleOwner.lifecycleScope.launch { viewModel.setFavourites(product) }
        coolShopAdapter?.notifyItemChanged(position, Any())
    }

    override fun clickItem(item: CoolShopModel) {
        val request = NavDeepLinkRequest.Builder
            .fromUri(requireContext().getString(R.string.nav_deep_link,item.id).toUri())
            .build()
        findNavController().navigate(request)
    }

}
