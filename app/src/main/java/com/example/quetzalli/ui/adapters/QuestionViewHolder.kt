package com.example.quetzalli.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.quetzalli.R
import com.example.quetzalli.data.models.FAQ

import com.example.quetzalli.databinding.QuestionItemBinding

class QuestionViewHolder(val binding: QuestionItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun render(faq: FAQ) {
            binding.tvQuestion.text = faq.question
            binding.tvAnswer.text = faq.answer

            binding.questionDetails.visibility = if (faq.isExpandable) View.VISIBLE else View.GONE
            binding.ivExpand.setImageResource(if (faq.isExpandable) R.drawable.ic_expand_up else R.drawable.ic_expand)
            binding.root.setOnClickListener {
                faq.isExpandable = !faq.isExpandable
                binding.questionDetails.visibility =
                    if (faq.isExpandable) View.VISIBLE else View.GONE

                binding.ivExpand.setImageResource(if (faq.isExpandable) R.drawable.ic_expand_up else R.drawable.ic_expand)
            }
        }
}