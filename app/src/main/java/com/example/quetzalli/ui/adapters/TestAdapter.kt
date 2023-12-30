package com.example.quetzalli.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.quetzalli.data.models.TestRep
import com.example.quetzalli.databinding.TestItemBinding

class TestAdapter(private val tests: List<TestRep>) : RecyclerView.Adapter<TestAdapter.ViewHolder>() {

    class ViewHolder(private val binding: TestItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(test: TestRep) {
            binding.tvTitleTest.text = test.name
            Glide.with(binding.ivTest.context).load(test.image).into(binding.ivTest)
            binding.btnResults.setOnClickListener {
                // Aquí puedes manejar el clic en el botón "Ver resultados"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tests[position])
    }

    override fun getItemCount() = tests.size
}
