package com.abhinav.mobicalc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import net.objecthunter.exp4j.ExpressionBuilder
import android.util.Log
import android.view.View
import com.facebook.drawee.backends.pipeline.Fresco
import com.inmobi.ads.InMobiBanner
import com.inmobi.sdk.InMobiSdk
import com.inmobi.ads.InMobiAdRequestStatus
import com.inmobi.ads.listeners.BannerAdEventListener
import com.inmobi.ads.InMobiInterstitial
import com.inmobi.ads.listeners.InterstitialAdEventListener

class MainActivity : AppCompatActivity() {

    private val logTag = "Test Ad Banner : "
    private var mCanShowAd = false
    private lateinit var interstitialAd: InMobiInterstitial
    private lateinit var bannerAd: InMobiBanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Fresco.initialize(this)

        //Initialize InMobi sdk
        InMobiSdk.init(this, "d94ee7a33e2c401d942285199383ab6f")
        InMobiSdk.setLogLevel(InMobiSdk.LogLevel.DEBUG)

        setContentView(R.layout.activity_main)

        //Banner Instance
        mobiBannerAdSetup()

        //Interstital Ad Instance
        interstitialAd = InMobiInterstitial(this, 1564154302187 , mInterstitialAdEventListener)
        interstitialAd.load()

        //Numbers
        tvOne.setOnClickListener { appendOnExpression("1", true) }
        tvTwo.setOnClickListener { appendOnExpression("2", true) }
        tvThree.setOnClickListener { appendOnExpression("3", true) }
        tvFour.setOnClickListener { appendOnExpression("4", true) }
        tvFive.setOnClickListener { appendOnExpression("5", true) }
        tvSix.setOnClickListener { appendOnExpression("6", true) }
        tvSeven.setOnClickListener { appendOnExpression("7", true) }
        tvEight.setOnClickListener { appendOnExpression("8", true) }
        tvNine.setOnClickListener { appendOnExpression("9", true) }
        tvZero.setOnClickListener { appendOnExpression("0", true) }
        tvDot.setOnClickListener { appendOnExpression(".", true) }

        //Operators
        tvPlus.setOnClickListener { appendOnExpression("+", false) }
        tvMinus.setOnClickListener { appendOnExpression("-", false) }
        tvMul.setOnClickListener { appendOnExpression("*", false) }
        tvDivide.setOnClickListener { appendOnExpression("/", false) }
        tvOpen.setOnClickListener { appendOnExpression("(", false) }
        tvClose.setOnClickListener { appendOnExpression(")", false) }

        tvClear.setOnClickListener {
            tvExpression.text = ""
            tvResult.text = ""
        }

        tvBack.setOnClickListener {
            val string = tvExpression.text.toString()
            if(string.isNotEmpty()){
                tvExpression.text = string.substring(0,string.length-1)
            }
            tvResult.text = ""
        }

        tvEquals.setOnClickListener {

            if (mCanShowAd)
                interstitialAd.show()

            try {
                val expression = ExpressionBuilder(tvExpression.text.toString()).build()
                val result = expression.evaluate()
                val longResult = result.toLong()

                if (result == longResult.toDouble())
                    tvResult.text = longResult.toString()
                else
                    tvResult.text = result.toString()

            }catch (e:Exception){
                Log.d("Exception"," message : " + e.message )
                tvResult.text = ""
                tvResult.text = e.message
            }
        }

    }

    private fun appendOnExpression(string: String, canClear: Boolean) {

        if(tvResult.text.isNotEmpty()){
            tvExpression.text = ""
        }

        if (canClear) {
            tvResult.text = ""
            tvExpression.append(string)
        } else {
            tvExpression.append(tvResult.text)
            tvExpression.append(string)
            tvResult.text = ""
        }
    }

    private fun mobiBannerAdSetup() {
        bannerAd = banner as InMobiBanner

        bannerAd.setListener(object : BannerAdEventListener() {
            override fun onAdLoadSucceeded(inMobiBanner: InMobiBanner) {
                super.onAdLoadSucceeded(inMobiBanner)
                Log.d(logTag, "onAdLoadSucceeded")
            }

            override fun onAdLoadFailed(inMobiBanner: InMobiBanner?, inMobiAdRequestStatus: InMobiAdRequestStatus) {
                super.onAdLoadFailed(inMobiBanner, inMobiAdRequestStatus)
                Log.d(logTag, "Banner ad failed to load with error: " + inMobiAdRequestStatus.message)
            }

            override fun onAdClicked(inMobiBanner: InMobiBanner?, map: Map<Any, Any>?) {
                super.onAdClicked(inMobiBanner, map)
                Log.d(logTag, "onAdClicked")
            }

            override fun onAdDisplayed(inMobiBanner: InMobiBanner?) {
                super.onAdDisplayed(inMobiBanner)
                Log.d(logTag, "onAdDisplayed")
            }

            override fun onAdDismissed(inMobiBanner: InMobiBanner?) {
                super.onAdDismissed(inMobiBanner)
                Log.d(logTag, "onAdDismissed")
            }

            override fun onUserLeftApplication(inMobiBanner: InMobiBanner?) {
                super.onUserLeftApplication(inMobiBanner)
                Log.d(logTag, "onUserLeftApplication")
            }

            override fun onRewardsUnlocked(inMobiBanner: InMobiBanner?, map: Map<Any, Any>) {
                super.onRewardsUnlocked(inMobiBanner, map)
                Log.d(logTag, "onRewardsUnlocked")
            }
        })

        bannerAd.load()
    }

    private var mInterstitialAdEventListener: InterstitialAdEventListener = object : InterstitialAdEventListener() {
        // implementation for other events
        // onAdReceived, onAdLoaFailed, etc

        override fun onAdLoadFailed(inMobiInterstitial: InMobiInterstitial, inMobiAdRequestStatus: InMobiAdRequestStatus) {
            super.onAdLoadFailed(inMobiInterstitial, inMobiAdRequestStatus)
            Log.d("Test Interstitial : ","Unable to load interstitial ad (onAdLoadFailed: " + inMobiAdRequestStatus.message)
        }
        override fun onAdLoadSucceeded(inMobiInterstitial: InMobiInterstitial?) {
            Log.d("Test Interstitial : ", "Ad can now be shown!")
            mCanShowAd = true
        }

        override fun onAdDismissed(inMobiInterstitial: InMobiInterstitial) {
            super.onAdDismissed(inMobiInterstitial)
            interstitialAd.load()
            Log.d("Test Interstitial : ", "onAdDismissed $inMobiInterstitial")
        }

        override fun onAdDisplayFailed(inMobiInterstitial: InMobiInterstitial) {
            super.onAdDisplayFailed(inMobiInterstitial)
            interstitialAd.load()
            Log.d("Test Interstitial : ", "onAdDisplayFailed")
        }
    }
}