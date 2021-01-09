package com.keelim.cnubus.feature.error

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.keelim.cnubus.feature.error.databinding.ActivityErrorBinding


class ErrorActivity : AppCompatActivity(R.layout.activity_error) {
    private lateinit var binding: ActivityErrorBinding

    private val lastActivityIntent by lazy {
        intent.getParcelableExtra<Intent>(EXTRA_INTENT)
    }

    private val errorText by lazy {
        intent.getStringExtra(EXTRA_ERROR_TEXT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityErrorBinding.inflate(layoutInflater)

        binding.tvErrorLog.text = errorText

        binding.btnReload.setOnClickListener {
            startActivity(lastActivityIntent)
            finish()
        }
    }

    companion object {
        const val EXTRA_INTENT = "EXTRA_INTENT"
        const val EXTRA_ERROR_TEXT = "EXTRA_ERROR_TEXT"
    }
}