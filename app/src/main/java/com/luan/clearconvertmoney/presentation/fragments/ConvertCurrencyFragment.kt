package com.luan.clearconvertmoney.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.luan.clearconvertmoney.R
import com.luan.clearconvertmoney.databinding.ConverterFragmentBinding
import com.luan.clearconvertmoney.presentation.ConvertViewModel
import com.luan.common.base.BaseFragment
import com.luan.common.base.ViewState
import com.luan.common.extension.handleLoading
import com.luan.common.extension.textChanges
import com.luan.domain.exceptions.CurrencyListEmptyException
import com.luan.domain.exceptions.CurrencyServiceException
import kotlinx.android.synthetic.main.converter_fragment.*
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class ConvertCurrencyFragment : BaseFragment() {

    enum class ListType {
        FROM,
        TO
    }

    private lateinit var binding: ConverterFragmentBinding
    private val viewModel: ConvertViewModel by sharedViewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<ConverterFragmentBinding>(
            inflater,
            R.layout.converter_fragment,
            container,
            false
        ).also {
            it.lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        buttonCurrencyFrom.setOnClickListener {
            findNavController().navigate(
                ConvertCurrencyFragmentDirections.actionHomeToList(ListType.FROM)
            )
        }
        buttonCurrencyTo.setOnClickListener {
            findNavController().navigate(
                ConvertCurrencyFragmentDirections.actionHomeToList(ListType.TO)
            )
        }
    }

    private fun setupObservers() {

        value.textChanges()
            .distinctUntilChanged()
            .debounce(300)
            .onEach { viewModel.convert() }
            .launchIn(lifecycleScope)

        viewModel.run {
            convertResult.observe(viewLifecycleOwner, Observer {
                handleLoading(it.status, loadingView)
            })

            getCurrenciesResponse.observe(viewLifecycleOwner, Observer {
                handleLoading(it.status, loadingView)
                when (it.status) {
                    ViewState.Status.LOADED -> {

                    }
                    ViewState.Status.ERROR -> {
                        it.exception?.let { exception ->
                            when (exception) {
                                is CurrencyListEmptyException -> { /*TODO: Snackbar with retry */
                                }
                                is CurrencyServiceException -> { /*TODO: Snackbar with retry */
                                }
                                else -> { /*TODO: Snackbar with retry */
                                }
                            }
                        }
                    }
                    else -> {}
                }
            })

        }
    }

}