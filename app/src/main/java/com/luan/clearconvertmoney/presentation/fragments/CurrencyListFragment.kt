package com.luan.clearconvertmoney.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luan.clearconvertmoney.R
import com.luan.clearconvertmoney.presentation.ConvertViewModel
import com.luan.clearconvertmoney.presentation.MainActivity
import com.luan.clearconvertmoney.presentation.adapter.CurrencyAdapter
import com.luan.common.base.BaseFragment
import com.luan.common.extension.textChanges
import com.luan.domain.model.Currency
import kotlinx.android.synthetic.main.currency_list_fragment.*
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CurrencyListFragment : BaseFragment() {

    private val viewModel: ConvertViewModel by sharedViewModel()
    private val navArgs =  navArgs<CurrencyListFragmentArgs>()
    private val adapter = CurrencyAdapter(this::onItemClicked)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return View.inflate(context,R.layout.currency_list_fragment,null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupViews()
        setupObservers()
    }

    private fun setupViews(){
        query.textChanges()
                .distinctUntilChanged()
                .debounce(300)
                .onEach { viewModel.search(it?.toString()) }
                .launchIn(lifecycleScope)


        list.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            adapter = this@CurrencyListFragment.adapter
        }

        viewModel.getCurrenciesList()?.let { adapter.items = it }

    }

    private fun setupObservers(){
        viewModel.run {
            getCurrenciesFiltered.observe(viewLifecycleOwner, Observer {
                adapter.items = it
            })
        }
    }

    private fun onItemClicked(currency: Currency){
        when(navArgs.value.listType){
            MainActivity.ListType.FROM -> viewModel.setCurrencyFrom(currency)
            MainActivity.ListType.TO -> viewModel.setCurrencyTo(currency)
        }

        findNavController().popBackStack()
    }


}