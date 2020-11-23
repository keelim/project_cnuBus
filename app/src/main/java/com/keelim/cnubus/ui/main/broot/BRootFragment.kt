package com.keelim.cnubus.ui.main.broot

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.FragmentBRootBinding
import com.keelim.cnubus.ui.MapsActivity

class BRootFragment : Fragment(R.layout.fragment_b_root) {
    private lateinit var rootList: Array<String>
    private lateinit var intentList: Array<String>
    private var fragmentBRootBinding: FragmentBRootBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentBRootBinding.bind(view)
        fragmentBRootBinding = binding

        rootList = resources.getStringArray(R.array.broot)
        intentList = resources.getStringArray(R.array.b_intent_array)
        binding.lvBroot.adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, rootList)

        binding.lvBroot.setOnItemClickListener { _, _, i, _ ->
            Toast.makeText(activity, rootList[i] + "정류장 입니다.", Toast.LENGTH_SHORT).show()

            Intent(activity, MapsActivity::class.java).apply {
                putExtra("location", intentList[i])
                startActivity(this)
            }
        }

    }

    override fun onDestroyView() {
        fragmentBRootBinding = null
        super.onDestroyView()
    }
}