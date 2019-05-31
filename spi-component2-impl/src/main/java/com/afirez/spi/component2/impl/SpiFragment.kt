package com.afirez.spi.component2.impl


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.afirez.spi.ExtensionLoader
import com.afirez.spi.SPI
import com.afirez.spi.component1.SpiRouter1Provider

/**
 * https://github.com/afirez/spi
 */
@SPI(path = "/spi/fragment/spi")
class SpiFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spi, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.tvSpiFragment).setOnClickListener {
            activity ?: return@setOnClickListener

            val spiRouter1Provider = ExtensionLoader.getInstance().loadExtension(SpiRouter1Provider::class.java)
            val tips = if (spiRouter1Provider?.spiJavaProvider() == null) {
                ""
            } else {
                spiRouter1Provider.spiJavaProvider().helloJava()
            }
            Toast.makeText(activity?.applicationContext, "SpiActivity $tips", Toast.LENGTH_SHORT).show()
        }

    }
}
