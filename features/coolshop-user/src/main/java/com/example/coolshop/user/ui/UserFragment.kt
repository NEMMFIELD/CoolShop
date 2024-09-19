package com.example.coolshop.user.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.coolshop.user.databinding.FragmentUserBinding
import dagger.hilt.android.AndroidEntryPoint

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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.token.observe(viewLifecycleOwner) { token ->
            if (token.isNotEmpty()) {
                viewModel.saveToken(token)
                binding?.idTVResponse?.text = "Current user is: ${
                    viewModel.account.username
                }"
            }
        }
        with(binding) {
            this?.idBtnPost?.setOnClickListener {
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
        viewModel.account.username = binding?.idEdtName?.text.toString()
        viewModel.account.password = binding?.idEdtPass?.text.toString()
    }

}
