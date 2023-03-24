package com.alexlipin.myretrofitapp.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View.OnLayoutChangeListener
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.alexlipin.myretrofitapp.R
import com.alexlipin.myretrofitapp.databinding.FragmentMainBinding
import com.alexlipin.myretrofitapp.model.FactItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.*

@AndroidEntryPoint
class MainFragment: Fragment(), View.OnClickListener {

    private var viewModel: MainViewModel? = null
    private var binding: FragmentMainBinding? = null
    private var adapter: NumbersAdapter = NumbersAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView? = binding?.history
        recyclerView?.layoutManager = viewModel?.let { GridLayoutManager(activity, 1) }
        recyclerView?.adapter = adapter
        adapter.setOnClickListener(object : NumbersAdapter.OnClickListener {
            override fun onItemClick(item: FactItem) {
                val action = MainFragmentDirections.actionMainFragmentToDescriptionFragment(item.id ?: -1)
                findNavController().navigate(action)
            }
        })
        binding?.buttonGetNumber?.setOnClickListener(this)
        binding?.buttonGetNumberRandom?.setOnClickListener(this)

        viewModel?.getItems()?.observe(viewLifecycleOwner, adapter::submitList)
        viewModel?.getButtonState()?.observe(viewLifecycleOwner) { setProgressVisibility(it) }
        viewModel?.getToastInternetErrorState()?.observe(viewLifecycleOwner) { showToast ->
            if (showToast) {
                Toast.makeText(requireActivity().application, getString(R.string.internetConnectionError), Toast.LENGTH_LONG).show()
            }
        }
        recyclerView?.addOnLayoutChangeListener(OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            recyclerView.scrollToPosition(viewModel?.getItems()?.value?.size?.minus(1) ?: 0)
        })
        viewModel?.inputTextErrorsStatus?.observe(viewLifecycleOwner) { error ->
            binding?.number?.let {
                when (error) {
                    MainViewModel.ERR_SIZE -> it.error = getString(R.string.invalid_size)
                    MainViewModel.ERR_LESS_ZERO -> it.error = getString(R.string.must_be_positive_number_or_zero)
                    else -> it.error = null
                }
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding?.buttonGetNumber?.id -> {
                if (this.context?.let { isOnline(it.applicationContext) } == true) {
                    val number: String = binding?.number?.text?.toString()?.trim() ?: ""
                    viewModel?.receiveNumber(number)
                } else {
                    viewModel?.setToastInternetError()
                }
            }
            binding?.buttonGetNumberRandom?.id -> {
                if (this.context?.let { isOnline(it.applicationContext) } == true) {
                    viewModel?.receiveRandomNumber()
                } else {
                    viewModel?.setToastInternetError()
                }
            }
        }
    }

    private fun setProgressVisibility(isVisible: Boolean) {
        val newProgressAlpha = if (isVisible) 1f else 0f
        if (newProgressAlpha != binding?.progressBar?.alpha) {
            progressBar?.animate()?.alpha(newProgressAlpha)
        }
    }

    private fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}