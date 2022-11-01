package com.appiadev.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.appiadev.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }

fun Context.noNetworkConnectivityError(): AppResult.Error {
    return AppResult.Error(Exception(this.resources.getString(R.string.no_network_connectivity)))
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int, backStackTag: String? = null) {
    supportFragmentManager.inTransaction {
        add(frameId, fragment)
        backStackTag?.let { addToBackStack(fragment.javaClass.name) }
    }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int, backStackTag: String? = null) {
    supportFragmentManager.inTransaction {
        replace(frameId, fragment)
        backStackTag?.let { addToBackStack(fragment.javaClass.name) }
    }
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}

inline fun <T> MutableStateFlow<T>.update(function: (T) -> T) {
    while (true) {
        val prevValue = value
        val nextValue = function(prevValue)
        if (compareAndSet(prevValue, nextValue)) {
            return
        }
    }
}

fun ProgressBar.showProgressBar(isToShow: Boolean) {
    this.visibility = if (isToShow) View.VISIBLE else View.GONE
}

inline fun Fragment.launchAndRepeatWithViewLifecycle(
    mainActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline codeBlock: suspend CoroutineScope.() -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(mainActiveState) {
            codeBlock()
        }
    }
}

fun Fragment.showToastMessage(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showAlertDialogErrorApi(controller: NavController) {
    MaterialAlertDialogBuilder(this.requireContext())
        .setCancelable(false)
        .setTitle(this.resources.getString(R.string.error_api_generic_title))
        .setMessage(this.resources.getString(R.string.error_api_generic_message))
        .setPositiveButton(android.R.string.ok) { dialog, _ ->
            dialog.dismiss()
            controller.popBackStack()
        }.show()
}

fun Fragment.openFirstTrailerOnYoutube(key: String) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$key")))
    } catch (e: ActivityNotFoundException) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=$key")))
    }
}
