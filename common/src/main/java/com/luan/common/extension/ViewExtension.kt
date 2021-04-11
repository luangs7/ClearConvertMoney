package com.luan.common.extension

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.setImage(url:String?){
    url?.let {   Glide.with(this.context).load(url).into(this) }
}

fun View.show(){
    visibility = View.VISIBLE
}

fun View.hide(){
    visibility = View.GONE
}