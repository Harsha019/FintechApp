package com.example.fintechtestapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fintechtestapp.R
import com.example.fintechtestapp.databinding.FragmentMainBinding
import com.example.fintechtestapp.ui.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val viewModel: MainViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadUser()

        viewModel.user.observe(viewLifecycleOwner) { user ->
            if (viewModel.isCoolingPeriod()) {
                binding.coolingCv.visibility = View.VISIBLE
                binding.coolingPeriodTv.text = viewModel.getRemainingCoolingTime()
            } else {
                binding.coolingCv.visibility = View.GONE
            }
        }
    }
}