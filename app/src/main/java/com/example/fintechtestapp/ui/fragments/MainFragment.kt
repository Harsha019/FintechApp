package com.example.fintechtestapp.ui.fragments

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
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

    val viewModel: MainViewModel by viewModel()
    lateinit var cardAdapter: CardAdapter
    private var coolingTimer: CountDownTimer? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardAdapter = CardAdapter(emptyList()) { moduleState ->
            val message = when {
                viewModel.isCoolingPeriod() -> "Access denied: cooling period"
                !moduleState.isAccessible -> "Access denied: no permission"
                else -> "Navigating to ${moduleState.module.title}"
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
                val remainingMillis = viewModel.getRemainingMillis()
                startCoolingCountdown(remainingMillis)
            } else {
                binding.coolingCv.visibility = View.GONE
            }
        }

        viewModel.modules.observe(viewLifecycleOwner) { modules ->
            cardAdapter.submitList(modules)
        }
    }

    private fun startCoolingCountdown(millis: Long) {
        coolingTimer?.cancel()

        coolingTimer = object : CountDownTimer(millis, 1000) {
            override fun onTick(remainingMillis: Long) {
                val seconds = (remainingMillis / 1000) % 60
                val minutes = (remainingMillis / (1000 * 60)) % 60
                binding.coolingPeriodTv.text = String.format( "Cooling ends in %02d:%02d", minutes, seconds )
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onFinish() {
                binding.coolingCv.visibility = View.GONE
                viewModel.loadUserAndModules()
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        coolingTimer?.cancel()
    }

}
