package com.example.coolshop.reviews.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coolshop.reviews.databinding.FragmentShowingReviewsBinding
import com.example.state.State
import com.example.utils.Logger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ShowingUserReviewsFragment : Fragment() {
    private var _binding: FragmentShowingReviewsBinding? = null
    private val binding get() = _binding
    private val viewModel: ShowUserReviewsViewModel by viewModels()
    private  var recyclerView: RecyclerView? = null
    private var userReviewsAdapter: UserReviewsAdapter? = null
    @Inject
    lateinit var logger: Logger

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowingReviewsBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUserReviewsRecycler()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.reviewsStateFlow.collect { state ->
                    when (state) {
                        is State.Success -> {
                            viewModel.loadReviews(viewModel.productId?.toInt())
                            userReviewsAdapter?.submitList(state.data)
                        }

                        is State.Failure -> logger.d("TagError", "On Create ${state.message}")
                        else -> {}
                    }
                }
            }
        }
        binding?.reviewsButtonToBack?.setOnClickListener { getBack() }
    }

    override fun onDestroyView() {
        _binding = null
        recyclerView = null
        super.onDestroyView()
    }
    private fun setupUserReviewsRecycler() {
        userReviewsAdapter =  UserReviewsAdapter(emptyList(),requireContext())
        recyclerView = binding!!.showedReviewsRecyclerview
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        recyclerView?.adapter = userReviewsAdapter
    }

    private fun getBack() {
        findNavController().popBackStack()
    }

    companion object {
        @JvmStatic
        fun newInstance() = ShowingUserReviewsFragment()
    }
}
