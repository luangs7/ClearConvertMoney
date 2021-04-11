package com.luan.common.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope



abstract class BaseActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    fun showFragment(@IdRes containerIdRes: Int, fragment: Fragment, arguments: Bundle? = null, shouldAddToBackStack: Boolean = false, vararg sharedElements: View){
        arguments?.let {
            fragment.arguments = arguments
        }

        val transaction = supportFragmentManager.beginTransaction().apply {
            replace(containerIdRes, fragment)
            if (shouldAddToBackStack) addToBackStack(fragment.javaClass.name)
        }

        sharedElements.forEach {element ->
            if (!element.transitionName.isNullOrEmpty()) {
                transaction.addSharedElement(element, element.transitionName)
            }
        }

        transaction.commit()
    }

    protected fun showToast(message: String, time: Int = Toast.LENGTH_SHORT){
        Toast.makeText(this, message, time).show()
    }

}

