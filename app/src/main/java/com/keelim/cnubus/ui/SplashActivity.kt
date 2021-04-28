/*
 * Designed and developed by 2020 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.cnubus.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.snackbar.Snackbar
import com.keelim.cnubus.BuildConfig
import com.keelim.cnubus.R
import com.keelim.cnubus.base.BaseActivity
import com.keelim.cnubus.databinding.ActivitySplashBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class SplashActivity : BaseActivity() {
    companion object {
        const val PREF_FIRST_START = "AppFirstLaunch"
        const val MULTIPLE_PERMISSIONS = 8888
    }

    private val binding: ActivitySplashBinding by binding(R.layout.activity_splash)
    private lateinit var settings: SharedPreferences
    private var mInterstitialAd: InterstitialAd? = null
    private val test = "ca -app-pub-3940256099942544/1033173712"
    private infix fun String.or(that: String): String = if (BuildConfig.DEBUG) this else that

    private val permissions = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addShortcut()

        if (hasPermissions(this, permissions)) { // 권한이 있는 경우
            goNext()
        } else {
            ActivityCompat.requestPermissions(this, permissions, MULTIPLE_PERMISSIONS)
        }
    }

    private fun addShortcut() {
        settings = getSharedPreferences(PREF_FIRST_START, 0)

        if (settings.getBoolean("AppFirstLaunch", true)) { // 아이콘이 두번 추가 안되도록 하기 위해서 필요한 체크입니다.
            settings.edit().putBoolean("AppFirstLaunch", false).apply()

            if (ShortcutManagerCompat.isRequestPinShortcutSupported(this)) {
                val shortcutInfo = ShortcutInfoCompat.Builder(this, "#1")
                    .setIntent(Intent(this, SplashActivity::class.java).setAction(Intent.ACTION_MAIN))
                    .setShortLabel(getString(R.string.app_name)) //  아이콘에 같이 보여질 이름
                    .setIcon(IconCompat.createWithResource(this, R.mipmap.ic_launcher)) // 아이콘에 보여질 이미지
                    .build()

                ShortcutManagerCompat.requestPinShortcut(this, shortcutInfo, null)
                Toast.makeText(this, "홈 화면에 바로가기를 추가하였습니다. ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
        permissions.forEach { permission ->
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            MULTIPLE_PERMISSIONS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(binding.root, "모든 권한이 승인 되었습니다. ", Snackbar.LENGTH_SHORT).show()

                    showAd()
                    goNext()
                } else {
                    // 하나라도 거부한다면.
                    AlertDialog.Builder(this)
                        .setTitle("앱 권한")
                        .setMessage("해당 앱의 원할한 기능을 이용하시려면 애플리케이션 정보>권한> 에서 모든 권한을 허용해 주십시오")
                        .setPositiveButton("권한설정") { dialog, which ->
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.parse("package:" + applicationContext.packageName)
                                startActivity(this)
                                dialog.cancel()
                            }
                        }
                        .setNegativeButton("취소") { dialog, which -> dialog.cancel() }
                        .show()
                }
            }
        }
    }

    private fun goNext() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish() // 앱을 종료한다.
        }
    }

    private fun showAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this, test or "ca-app-pub-3115620439518585/4013096159", adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Timber.d(adError.message)
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Timber.d("Ad was loaded.")
                    mInterstitialAd = interstitialAd

                    if (mInterstitialAd != null) {
                        mInterstitialAd!!.show(this@SplashActivity)
                    } else {
                        Timber.d("The interstitial ad wasn't ready yet.")
                    }
                }
            }
        )
    }

    override fun onBackPressed() {}
}
