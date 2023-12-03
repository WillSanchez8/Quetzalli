package com.example.quetzalli.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.quetzalli.data.models.FAQ

class QuestionDiffCallback : DiffUtil.ItemCallback<FAQ>(){

    override fun areItemsTheSame(oldItem: FAQ, newItem: FAQ): Boolean {
        // Los elementos son los mismos si tienen el mismo id
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FAQ, newItem: FAQ): Boolean {
        // Los contenidos son los mismos si todos los campos, excepto el id, son iguales
        return oldItem.question == newItem.question &&
                oldItem.answer == newItem.answer &&
                oldItem.isExpandable == newItem.isExpandable
    }
}