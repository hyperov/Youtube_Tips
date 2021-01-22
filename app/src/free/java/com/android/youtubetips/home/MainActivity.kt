package com.android.youtubetips.home

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.android.youtubetips.BuildConfig
import com.android.youtubetips.R
import com.google.ads.consent.*
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.ads.mediationtestsuite.MediationTestSuite
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.LoadAdError
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.tasks.Task
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.startapp.sdk.ads.splash.SplashConfig
import com.startapp.sdk.adsbase.StartAppAd
import com.startapp.sdk.adsbase.StartAppSDK
import com.unity3d.ads.metadata.MetaData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.tool_bar.*
import java.net.MalformedURLException
import java.net.URL
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    var reviewInfo: ReviewInfo? = null
    lateinit var manager: ReviewManager

    private var appUpdateInfoTask: Task<AppUpdateInfo>? = null
    private lateinit var appUpdateManager: AppUpdateManager

    private lateinit var mInterstitialAd: InterstitialAd

    private lateinit var googleConsentForm: ConsentForm

    private val sharedAdsViewModel: SharedAdsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.IS_TESTING)
            MediationTestSuite.launch(this)
        setSplashAd(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initReviews()
        setConsentData()

    }

    private fun resumeAfterAdConsent() {
        initializeAds()
        setupInterstitalAdsListeners()
        initUpdates()
    }

    private fun setConsentData() {
        val consentInformation = ConsentInformation.getInstance(this)
//        consentInformation.debugGeography = DebugGeography.DEBUG_GEOGRAPHY_EEA
        checkForConsentData(consentInformation)
    }

    private fun updateStartAppAdsConsent(showPersonalizedAll: Boolean) {
        StartAppSDK.setUserConsent(
            this,
            "pas",
            System.currentTimeMillis(),
            showPersonalizedAll
        )
    }

    private fun checkForConsentData(consentInformation: ConsentInformation) {
        val publisherIds = arrayOf(getString(R.string.admob_publisher_id))
        consentInformation.requestConsentInfoUpdate(
            publisherIds,
            object : ConsentInfoUpdateListener {
                override fun onConsentInfoUpdated(consentStatus: ConsentStatus?) {
                    Log.e("ad consent success", "success")
                    // User's consent status successfully updated.
                    updateConsentAds(consentInformation, consentStatus!!)
                }

                override fun onFailedToUpdateConsentInfo(errorDescription: String) {
                    // User's consent status failed to update.
                    Log.e("ad consent fail", errorDescription)
                }
            })
    }

    private fun showGoogleConsentForm(consentInformation: ConsentInformation) {
        var privacyUrl: URL? = null
        try {

            privacyUrl = URL("https://sites.google.com/view/ahmednabil/home")
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            // Handle error.
        }
        googleConsentForm = ConsentForm.Builder(this, privacyUrl)
            .withListener(object : ConsentFormListener() {
                override fun onConsentFormLoaded() {
                    // Consent form loaded successfully.
                    googleConsentForm.show()
                }

                override fun onConsentFormOpened() {
                    // Consent form was displayed.
                }

                override fun onConsentFormClosed(
                    consentStatus: ConsentStatus, userPrefersAdFree: Boolean
                ) {
                    // Consent form was closed.
                    updateConsentAds(consentInformation, consentStatus)
                }

                override fun onConsentFormError(errorDescription: String) {
                    // Consent form error.
                }
            })
            .withPersonalizedAdsOption()
            .withNonPersonalizedAdsOption()
            .withAdFreeOption()
            .build()

        googleConsentForm.load()
    }

    private fun updateConsentAds(
        consentInformation: ConsentInformation,
        consentStatus: ConsentStatus
    ) {
        when (consentStatus) {
            ConsentStatus.PERSONALIZED -> {
                updateStartAppAdsConsent(true)
                sharedAdsViewModel.isPersonalizedAds.value = true
                //unity gdpr
                val gdprMetaData = MetaData(this)
                gdprMetaData.set("gdpr.consent", true)
                gdprMetaData.commit()

                resumeAfterAdConsent()
            }
            ConsentStatus.NON_PERSONALIZED -> {
                updateStartAppAdsConsent(false)
                sharedAdsViewModel.apply {
                    isPersonalizedAds.value = false
                    extrasPersonalAdsBundle.value?.putString("npa", "1")
                    //unity no gdpr
                    val gdprMetaData = MetaData(this@MainActivity)
                    gdprMetaData["gdpr.consent"] = false
                    gdprMetaData.commit()
                    resumeAfterAdConsent()
                }

            }
            ConsentStatus.UNKNOWN -> {
                if (consentInformation.isRequestLocationInEeaOrUnknown) {
                    // is user in Europe
                    showGoogleConsentForm(consentInformation)
                } else {
                    updateStartAppAdsConsent(true)
                    sharedAdsViewModel.isPersonalizedAds.value = true
                    resumeAfterAdConsent()
                }

            }
        }
    }

    private fun setSplashAd(savedInstanceState: Bundle?) {
        StartAppAd.showSplash(
            this, savedInstanceState,
            SplashConfig()
                .setTheme(SplashConfig.Theme.BLAZE)
                .setAppName(getString(R.string.app_name))
                .setMinSplashTime(SplashConfig.MinSplashTime.SHORT)
                .setMaxAdDisplayTime(SplashConfig.MaxAdDisplayTime.SHORT)
        )
    }

    private fun setupInterstitalAdsListeners() {
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                FirebaseCrashlytics.getInstance().setCustomKey("INTERSTITAL_AD_LOADED", true)

            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
                FirebaseCrashlytics.getInstance().setCustomKey("INTERSTITAL_AD_LOADED", false)
            }

            override fun onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                var builder = AdRequest.Builder()
                if (sharedAdsViewModel.isPersonalizedAds.value!!.not()) {
                    builder = builder.addNetworkExtrasBundle(
                        AdMobAdapter::class.java,
                        sharedAdsViewModel.extrasPersonalAdsBundle.value
                    )
                }
                mInterstitialAd.loadAd(
                    builder.build()
                )
            }
        }
    }

    private fun initializeAds() {
        //interstital ads
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = getString(R.string.interstital_ad_unit_id)

        var builder = AdRequest.Builder()
        if (sharedAdsViewModel.isPersonalizedAds.value!!.not()) {
            builder = builder.addNetworkExtrasBundle(
                AdMobAdapter::class.java,
                sharedAdsViewModel.extrasPersonalAdsBundle.value
            )
        }

        mInterstitialAd.loadAd(
            builder
                .addNetworkExtrasBundle(
                    AdMobAdapter::class.java,
                    sharedAdsViewModel.extrasPersonalAdsBundle.value
                ).build()
        )
//        showAds()
    }

    private fun showAds() {
        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        } else {

        }
    }


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    override fun onResume() {
        super.onResume()
        resumeUpdate()
        if (Prefs.getInt(COUNTER_FOR_REVIEW, 0) >= MAX_COUNT_REVIEW_DIALOG_SHOW)
            askForReview(reviewInfo, manager)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.english -> {
                setLocale(this, Locale.ENGLISH.language)
            }
            R.id.arabic -> {
                setLocale(this, "ar")
            }
        }
        Prefs.putAny(COUNTER_FOR_REVIEW, Prefs.getInt(COUNTER_FOR_REVIEW, 0) + 1)
        return true
    }


    private fun setLocale(activity: Activity, languageCode: String?) {
        val locale = Locale(languageCode!!)
        Locale.setDefault(locale)
        val resources: Resources = activity.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun initReviews() {
        manager = ReviewManagerFactory.create(this)
        manager.requestReviewFlow().addOnCompleteListener { request ->
            if (request.isSuccessful) {
                reviewInfo = request.result
                FirebaseCrashlytics.getInstance().setCustomKey("REVIEWS_API_INIT", true)

            } else {
                FirebaseCrashlytics.getInstance().setCustomKey("REVIEWS_API_INIT", false)

            }
        }
    }


    // Call this when you want to show the dialog
    private fun askForReview(reviewInfo: ReviewInfo?, manager: ReviewManager) {
        if (reviewInfo != null) {
            manager.launchReviewFlow(this, reviewInfo).addOnFailureListener {

                // Log error and continue with the flow
                FirebaseCrashlytics.getInstance().setCustomKey("REVIEWS_API_DIALOG_SHOW", false)
                showAds()
            }.addOnCompleteListener { _ ->

                FirebaseCrashlytics.getInstance().setCustomKey("REVIEWS_API_DIALOG_SHOW", true)
                Prefs.putAny(COUNTER_FOR_REVIEW, 0)
                showAds()
            }
        }
    }

    private fun initUpdates() {
        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(this)

        // Returns an intent object that you use to check for an update.
        appUpdateInfoTask = appUpdateManager.appUpdateInfo

        // Checks that the platform will allow the specified type of update.
        checkForUpdates()
    }

    private fun checkForUpdates() {
        appUpdateInfoTask?.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                // For a flexible update, use AppUpdateType.FLEXIBLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                FirebaseCrashlytics.getInstance().setCustomKey("UPDATE_API_INIT", true)
                // Request the update.
                appUpdateManager.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                    AppUpdateType.IMMEDIATE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    MY_REQUEST_CODE
                )
            }
        }
    }

    private fun resumeUpdate() {

        appUpdateInfoTask
            ?.addOnSuccessListener { appUpdateInfo ->

                if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    FirebaseCrashlytics.getInstance()
                        .setCustomKey("UPDATE_API_RESUME_DOWNLOAD", true)
                    // If an in-app update is already running, resume the update.
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        this,
                        MY_REQUEST_CODE
                    );
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_CANCELED -> {
                    FirebaseCrashlytics.getInstance().setCustomKey(
                        "UPDATE_API_DOWNLOAD_CANCELED",
                        "UPDATE_API_DIALOG_CANCELED"
                    )
                    showAds()
                    checkForUpdates()
                }
                Activity.RESULT_OK -> {
                    Log.d(this::class.simpleName, "Update Success! Result code: $resultCode")
                    FirebaseCrashlytics.getInstance().setCustomKey(
                        "UPDATE_API_DOWNLOAD_SUCCESS",
                        "UPDATE_API_DOWNLOAD_SUCCESS"
                    )
                    showAds()
                }
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                    Log.d(this::class.simpleName, "Update flow failed! Result code: $resultCode")
                    FirebaseCrashlytics.getInstance().setCustomKey(
                        "UPDATE_API_DOWNLOAD_FAILED",
                        "UPDATE_API_DOWNLOAD_FAILED"
                    )
                    // If the update is cancelled or fails,
                    // you can request to start the update again.
                    // Request the update.
                    showAds()
                    checkForUpdates()
                }
            }
        }
    }


}