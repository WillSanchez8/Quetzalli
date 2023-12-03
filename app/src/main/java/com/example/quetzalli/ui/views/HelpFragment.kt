package com.example.quetzalli.ui.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.quetzalli.databinding.FragmentHelpBinding
import com.example.quetzalli.ui.adapters.QuestionAdapter
import com.example.quetzalli.viewmodel.HelpVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HelpFragment : Fragment() {

    private lateinit var binding: FragmentHelpBinding
    private val helpVM: HelpVM by viewModels()
    private lateinit var adapter : QuestionAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding  = FragmentHelpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        registerEvents()
    }

    private fun init(){
        adapter = QuestionAdapter()
        binding.rvHelp.adapter = adapter
        helpVM.getFAQs()
    }

    private fun registerEvents(){
        helpVM.faqList.observe(viewLifecycleOwner) { faqList ->
            faqList?.let { adapter.submitList(it) }
        }
    }

}