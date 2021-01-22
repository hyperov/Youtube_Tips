package com.android.youtubetips.home

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.android.youtubetips.R
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.tool_bar.*
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    var reviewInfo: ReviewInfo? = null
    lateinit var manager: ReviewManager

    private var appUpdateInfoTask: Task<AppUpdateInfo>? = null
    private lateinit var appUpdateManager: AppUpdateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initReviews()
        initUpdates()
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
            }.addOnCompleteListener { _ ->

                FirebaseCrashlytics.getInstance().setCustomKey("REVIEWS_API_DIALOG_SHOW", true)
                Prefs.putAny(COUNTER_FOR_REVIEW, 0)
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

                    checkForUpdates()
                }
                Activity.RESULT_OK -> {
                    Log.d(this::class.simpleName, "Update Success! Result code: $resultCode")
                    FirebaseCrashlytics.getInstance().setCustomKey(
                        "UPDATE_API_DOWNLOAD_SUCCESS",
                        "UPDATE_API_DOWNLOAD_SUCCESS"
                    )

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

                    checkForUpdates()
                }
            }
        }
    }


}