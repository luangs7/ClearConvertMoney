package com.luan.clearconvertmoney.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.luan.clearconvertmoney.R
import com.luan.clearconvertmoney.presentation.ConvertViewModel
import com.luan.clearconvertmoney.presentation.MainActivity
import com.luan.common.base.BaseFragment
import com.luan.common.base.ViewState
import com.luan.common.extension.handleLoading
import com.luan.common.extension.hideKeyboard
import com.luan.common.extension.textChanges
import com.luan.domain.exceptions.CurrencyListEmptyException
import com.luan.domain.exceptions.CurrencyServiceException
import kotlinx.android.synthetic.main.converter_fragment.*
import kotlinx.android.synthetic.main.converter_fragment.rootView
import kotlinx.android.synthetic.main.currency_item.*
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class ConvertCurrencyFragment : BaseFragment() {

    private val viewModel: ConvertViewModel by sharedViewModel()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return View.inflate(context,R.layout.converter_fragment,null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        buttonCurrencyFrom.setOnClickListener {
            findNavController().navigate(
                    ConvertCurrencyFragmentDirections.actionHomeToList(MainActivity.ListType.FROM)
            )
        }
        buttonCurrencyTo.setOnClickListener {
            findNavController().navigate(
                    ConvertCurrencyFragmentDirections.actionHomeToList(MainActivity.ListType.TO)
            )
        }
    }

    private fun setupObservers() {

        value.textChanges()
            .distinctUntilChanged()
            .debounce(300)
            .onEach { viewModel.convert(it?.toString()) }
            .launchIn(lifecycleScope)

        viewModel.run {
            convertResult.observe(viewLifecycleOwner, Observer {
                handleLoading(it.status, loadingView)
                when(it.status){
                    ViewState.Status.LOADED -> it.data?.let { value ->
                        valueFormatted.setText(value)
                        rootView.hideKeyboard()
                    }
                    ViewState.Status.ERROR -> valueFormatted.setText("")
                    else -> {}
                }
            })

            getCurrenciesResponse.observe(viewLifecycleOwner, Observer {
                handleLoading(it.status, loadingView)
                when (it.status) {
                    ViewState.Status.LOADED -> viewModel.enableButtons()
                    ViewState.Status.ERROR -> {
                        it.exception?.let { exception ->
                            when (exception) {
                                is CurrencyListEmptyException -> showSnackbar(getString(R.string.empty_exception), onAction = { viewModel.getCurrencies() })
                                is CurrencyServiceException -> showSnackbar(getString(R.string.service_exception), onAction = { viewModel.getCurrencies() })
                                else -> showSnackbar(getString(R.string.generic_exception), onAction = { viewModel.getCurrencies() })
                            }
                        }
                    }
                    else -> {
                    }
                }
            })

            enableButtons.observe(viewLifecycleOwner, Observer {
                buttonCurrencyTo.isEnabled = it
                buttonCurrencyFrom.isEnabled = it
            })

            currencyTo.observe(viewLifecycleOwner, Observer { currencyToSymbol.text = it.symbol })
            currencyFrom.observe(viewLifecycleOwner, Observer { currencyFromSymbol.text = it.symbol })
        }
    }

    private fun showSnackbar(text: String, onAction: () -> Unit) {
        Snackbar.make(rootView,
                text, Snackbar.LENGTH_LONG).also {
            it.setAction("Retry") { onAction.invoke() }
            it.show()
        }

    }

}