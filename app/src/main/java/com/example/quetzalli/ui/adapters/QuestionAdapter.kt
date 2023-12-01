package com.example.quetzalli.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.example.quetzalli.data.models.FAQ
import com.example.quetzalli.databinding.QuestionItemBinding
import com.example.quetzalli.databinding.QuestionItemLoadingBinding

class QuestionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var expandedPosition = -1

    private val differ = AsyncListDiffer(this, QuestionDiffCallback())

    fun submitList(list: List<FAQ?>) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_LOADING) {
            // Crea un ViewHolder para el efecto de carga
            val binding = QuestionItemLoadingBinding.inflate(layoutInflater, parent, false)
            LoadingViewHolder(binding)
        } else {
            // Crea un ViewHolder para las preguntas
            val binding = QuestionItemBinding.inflate(layoutInflater, parent, false)
            QuestionViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (differ.currentList[position] == null) {
            VIEW_TYPE_LOADING
        } else {
            VIEW_TYPE_ITEM
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is QuestionViewHolder) {
            val question = differ.currentList[position]
            holder.render(question)
            holder.itemView.setOnClickListener {
                if (expandedPosition >= 0 && expandedPosition != position) {
                    differ.currentList[expandedPosition].isExpandable = false
                    notifyItemChanged(expandedPosition)
                }
                question.isExpandable = !question.isExpandable
                notifyItemChanged(position)
                expandedPosition = if (question.isExpandable) position else -1
            }
        }
    }

    companion object {
        const val VIEW_TYPE_LOADING = 0
        const val VIEW_TYPE_ITEM = 1
    }
}
