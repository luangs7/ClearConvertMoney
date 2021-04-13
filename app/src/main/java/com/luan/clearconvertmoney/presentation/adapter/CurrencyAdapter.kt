package com.luan.clearconvertmoney.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.luan.clearconvertmoney.R
import com.luan.domain.model.Currency


class CurrencyAdapter(private val onItemClick : (Currency) -> Unit) :
    RecyclerView.Adapter<CurrencyAdapter.AvatarViewHolder>() {

    var items: List<Currency> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.currency_item, parent, false)
        return AvatarViewHolder(view, onItemClick)
    }

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }


    class AvatarViewHolder(itemView: View, private val onItemClick : (Currency) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val symbol = itemView.findViewById<TextView>(R.id.symbol)
        private val extendedName = itemView.findViewById<TextView>(R.id.extendedName)
        private val rootView = itemView.findViewById<ConstraintLayout>(R.id.rootView)

        fun bind(item: Currency) {
            symbol.text = item.symbol
            extendedName.text = item.extendedName
            rootView.setOnClickListener { onItemClick(item) }
        }

    }
}