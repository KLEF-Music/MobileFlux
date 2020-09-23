package com.klefmusic.flux.ui.success.fragment1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding4.view.clicks
import com.klefmusic.flux.R
import com.klefmusic.rxfluxcore.StoreView
import kotlinx.android.synthetic.main.fragment_sensors.*
import org.koin.android.viewmodel.ext.android.viewModel

class Fragment1 : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_sensors, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        leftTrackerButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragment1_to_fragment2)
        }
    }
}


