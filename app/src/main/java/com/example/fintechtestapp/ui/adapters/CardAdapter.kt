package com.example.fintechtestapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fintechtestapp.data.model.ModuleState
import com.example.fintechtestapp.databinding.ItemModuleBinding

class CardAdapter(
    private var modules: List<ModuleState>,
    private val onItemClick: (ModuleState) -> Unit
): RecyclerView.Adapter<CardAdapter.ModuleViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardAdapter.ModuleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemModuleBinding.inflate(inflater, parent, false)
        return ModuleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardAdapter.ModuleViewHolder, position: Int) {
        holder.bind(modules[position])
    }

    override fun getItemCount(): Int = modules.size

    inner class ModuleViewHolder(val binding: ItemModuleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(moduleState: ModuleState) {
            binding.titleTextView.text = moduleState.module.title
            binding.root.setOnClickListener {
                onItemClick(moduleState)
            }
            if (moduleState.isAccessible) {
                binding.root.alpha = 1.0f
            } else {
                binding.root.alpha = 0.5f
            }
        }
    }


}