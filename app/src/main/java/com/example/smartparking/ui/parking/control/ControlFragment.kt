package com.example.smartparking.ui.parking.control

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.smartparking.R
import com.example.smartparking.databinding.ControlFragmentBinding
import kotlinx.android.synthetic.main.control_fragment.*


class ControlFragment : Fragment() {

    private lateinit var binding: ControlFragmentBinding
    private val controlViewModel: ControlViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.control_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.controlViewModel = controlViewModel

        initStream()

    }

    private fun initStream() {
        controlViewModel.bitmap.observe(viewLifecycleOwner, Observer { bitmap ->
            if ( bitmap != null) {
                iv_mqtt.setImageBitmap(bitmap)
            }
        })
    }

}