package com.example.coolshop.main.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coolshop.main.R
import com.example.coolshop.main.data.models.ClickListener
import com.example.coolshop.main.data.models.CoolShopAdapter
import com.example.coolshop.main.data.models.ItemOffsetDecoration
import com.example.coolshop.main.data.models.SetFavourites
import com.example.coolshop.main.databinding.FragmentCoolShopMainBinding
import com.example.data.CoolShopModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CoolShopMainFragment : Fragment(), SetFavourites, ClickListener {

    private var _binding: FragmentCoolShopMainBinding? = null
    private val binding get() = _binding
    private val coolShopAdapter by lazy {
        CoolShopAdapter(
            emptyList(),
            this@CoolShopMainFragment,
            this
        )
    }
    private lateinit var recyclerView: RecyclerView
    private val viewModel: CoolShopViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
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
        binding?.autoCompleteTextView?.setOnItemClickListener { parent, view, position, id ->
            if (parent.adapter.getItem(position) == "all") viewModel.loadProducts()
            else viewModel.loadCategoryProducts(parent?.adapter?.getItem(position).toString())
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.postStateFlow.collect { state ->
                    when (state) {
                        is com.example.state.ApiState.Success -> {
                            state.data.forEach { product ->
                                viewModel.loadFavourites(product)
                            }
                            coolShopAdapter.submitList(state.data)
                        }

                        is com.example.state.ApiState.Failure -> {
                            Log.d("TagError", "On Create ${state.message}")
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecycler() {
        recyclerView = binding!!.recyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = coolShopAdapter
        val itemDecoration = ItemOffsetDecoration(10)
        recyclerView.addItemDecoration(itemDecoration)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CoolShopMainFragment()
    }

    override fun like(product: CoolShopModel, position: Int) {
        product.isLiked = !product.isLiked
        viewLifecycleOwner.lifecycleScope.launch { viewModel.setFavourites(product) }
        coolShopAdapter.notifyItemChanged(position, Any())
    }

    override fun clickItem(item: CoolShopModel) {
        val bundle = bundleOf("id" to item.id.toString())
        view?.findNavController()?.navigate(
            com.example.navigation.R.id.action_coolShopMainFragment_to_coolShopDetailsFragment,
            bundle
        )
    }
}
