package com.appiadev.ui.home.navigation.two

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.appiadev.R

class TwoHomeFragment : Fragment(R.layout.fragment_two_home) {

    private lateinit var text: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text = view.findViewById(R.id.go_to_one)

        with(text) {
            setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}
