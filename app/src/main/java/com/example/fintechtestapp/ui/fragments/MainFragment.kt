package com.example.fintechtestapp.ui.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fintechtestapp.R
import com.example.fintechtestapp.databinding.FragmentMainBinding
import com.example.fintechtestapp.ui.adapters.CardAdapter
import com.example.fintechtestapp.ui.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val viewModel: MainViewModel by viewModel()
    private lateinit var cardAdapter: CardAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardAdapter = CardAdapter(emptyList()) { moduleState ->
            val message = if (moduleState.isAccessible) {
                "Navigating to ${moduleState.module.title}"
            } else {
                "Access denied: ${if (viewModel.isCoolingPeriod()) "cooling period" else "no permission"}"
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
        binding.cardRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cardAdapter
        }

        viewModel.loadUserAndModules()

        viewModel.user.observe(viewLifecycleOwner) { user ->
            if (viewModel.isCoolingPeriod()) {
                binding.coolingCv.visibility = View.VISIBLE
                binding.coolingPeriodTv.text = viewModel.getRemainingCoolingTime()
            } else {
                binding.coolingCv.visibility = View.GONE
            }
        }

        viewModel.modules.observe(viewLifecycleOwner) { modules ->
            cardAdapter.submitList(modules)
        }
    }
}