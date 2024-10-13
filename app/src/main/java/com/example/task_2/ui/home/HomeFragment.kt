package com.example.task_2.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.task_2.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.buttonCalculateTask1.setOnClickListener { Task1() }

        return root
    }

    private fun round(num: Double) = "%.4f".format(num)

    val storageFuel: Map<String, Map<String, Double>> = mapOf(

        "fuel_oil" to mapOf(
            "qr" to 40.40,
            "av" to 1.0,
            "ar" to 0.15,
            "gv" to 0.0
        ),

        "coal" to mapOf(
            "qr" to 20.47,
            "av" to 0.8,
            "ar" to 25.20,
            "gv" to 1.5
        ),

        "natural_gas" to mapOf(
            "qr" to 33.08,
            "av" to 1.25,
            "ar" to 0.0,
            "gv" to 0.0
        )
    )

    fun emisionCalculate(fuelId: String, value: Double): List<Double> {
        val fuelData = storageFuel[fuelId] ?: error("Fuel ID not found")
        val qr = fuelData["qr"] ?: error("QR not found")
        val av = fuelData["av"] ?: error("Avnot found")
        val ar = fuelData["ar"] ?: error("AR not found")
        val gv = fuelData["gv"] ?: error("Gv not found")
        val ktv = (1e6 / qr) * av * (ar / (100 - gv)) * (1 - 0.985)
        val etv = 1e-6 * ktv * qr * value
        return listOf(ktv, etv).map { String.format("%.2f", it).toDouble() }
    }

    private fun Task1()
    {

        val coal = binding.coal.text.toString().toDoubleOrNull()
        val fuelOil = binding.fuelOil.text.toString().toDoubleOrNull()
        val naturalGas = binding.naturalGas.text.toString().toDoubleOrNull()

        var output = "";

        if (coal != null)
        {
            var ktv1Etv1 = emisionCalculate("coal", coal);
            output += "Показник емісії твердих частинок при спалюванні вугілля становить: ${ktv1Etv1[0]} г/ГДж\n";
            output += "Валовий викид при спалюванні вугілля становить: ${ktv1Etv1[1]} т\n";
        }

        if (fuelOil != null)
        {
            var ktv2Etv2 = emisionCalculate("fuel_oil", fuelOil);
            output += ("Показник емісії твердих частинок при спалюванні мазуту становить: ${ktv2Etv2[0]} г/ГДж\n");
            output += ("Валовий викид при спалюванні мазуту становить: ${ktv2Etv2[1]} т\n");
        }

        if (naturalGas != null)
        {
            var ktv3Etv3 = emisionCalculate("natural_gas", naturalGas);
            output += ("Показник емісії твердих частинок при спалюванні природнього газу становить: ${ktv3Etv3[0]} г/ГДж\n");
            output += ("Валовий викид при спалюванні природнього газу становить: ${ktv3Etv3[1]} т\n");
        }

        binding.outputTask1.text = output;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}