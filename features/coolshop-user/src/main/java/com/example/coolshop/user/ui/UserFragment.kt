package com.example.coolshop.user.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.coolshop.user.databinding.FragmentUserBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserFragment : Fragment() {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding
    private val viewModel: UserViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.token.collect { token ->
                    if (!token.isNullOrEmpty()) {
                        viewModel.saveToken(token)
                        binding?.currentUser?.text = (viewModel.account.username)
                    }
                }
            }
        }

        with(binding) {
            this?.buttonLogin?.setOnClickListener {
                setupAccount()
                viewModel.login(
                    viewModel.account
                )
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setupAccount() {
        viewModel.account.username = binding?.fieldEnterName?.text.toString()
        viewModel.account.password = binding?.fieldEnterPassword?.text.toString()
    }
}
