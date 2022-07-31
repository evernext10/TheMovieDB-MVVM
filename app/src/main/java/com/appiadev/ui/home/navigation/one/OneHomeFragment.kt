package com.appiadev.ui.home.navigation.one

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.appiadev.R

class OneHomeFragment : Fragment(R.layout.fragment_one_home) {

    private lateinit var text: TextView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text = view.findViewById(R.id.go_to_two)

        with(text) {
            setOnClickListener {
                findNavController().navigate(R.id.action_go_to_two)
            }
        }
    }
}
