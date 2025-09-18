package com.example.fintechtestapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fintechtestapp.data.model.ModuleState
import com.example.fintechtestapp.databinding.ItemModuleBinding

class CardAdapter(
    private var modules: List<ModuleState>,
    private val onItemClick: (ModuleState) -> Unit
) : RecyclerView.Adapter<CardAdapter.ModuleViewHolder>() {

    fun submitList(newModules: List<ModuleState>) {
        modules = newModules
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemModuleBinding.inflate(inflater, parent, false)
        return ModuleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ModuleViewHolder, position: Int) {
        holder.bind(modules[position])
    }

    override fun getItemCount(): Int = modules.size

    inner class ModuleViewHolder(val binding: ItemModuleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(moduleState: ModuleState) {
            binding.titleTextView.text = moduleState.module.title
            binding.root.setOnClickListener {
                onItemClick(moduleState)
            }
            binding.root.alpha = if (moduleState.isAccessible) 1.0f else 0.5f
        }
    }
}