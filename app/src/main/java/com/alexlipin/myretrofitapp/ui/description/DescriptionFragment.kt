package com.alexlipin.myretrofitapp.ui.description

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.alexlipin.myretrofitapp.R

import com.alexlipin.myretrofitapp.databinding.FragmentDescriptionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DescriptionFragment: Fragment(), View.OnClickListener {

    private var viewModel: DescriptionViewModel? = null
    private var binding: FragmentDescriptionBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[DescriptionViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDescriptionBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments?.let { DescriptionFragmentArgs.fromBundle(it) }
        val id = args?.idArg
        if (id != null) {
            viewModel?.getItemById(id)
        }
        binding?.myToolbar?.setOnClickListener(this)

        viewModel?.getItem()?.observe(viewLifecycleOwner) { item ->
            binding?.descriptionText?.text = item.text
        }
        viewModel?.getDataBaseErrorState()?.observe(viewLifecycleOwner) { isDatabaseError ->
            if (isDatabaseError) {
                Toast.makeText(requireActivity().application,
                    getString(R.string.databaseError), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onClick(v: View?) {
        requireActivity().onBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}