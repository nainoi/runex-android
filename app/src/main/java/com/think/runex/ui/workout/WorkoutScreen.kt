package com.think.runex.ui.workout

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.SupportMapFragment
import com.google.android.libraries.maps.model.LatLng
import com.jozzee.android.core.permission.allPermissionsGranted
import com.jozzee.android.core.permission.havePermissions
import com.jozzee.android.core.permission.shouldShowPermissionRationale
import com.jozzee.android.core.util.Logger
import com.jozzee.android.core.util.simpleName
import com.jozzee.android.core.view.showToast
import com.think.runex.R
import com.think.runex.common.*
import com.think.runex.config.*
import com.think.runex.feature.location.LocationUtil
import com.think.runex.feature.workout.*
import com.think.runex.ui.base.BaseScreen
import com.think.runex.util.launch
import com.think.runex.util.runOnUiThread
import kotlinx.android.synthetic.main.screen_workout.*
import kotlinx.coroutines.delay
import java.lang.Exception

class WorkoutScreen : BaseScreen(), ActionControlsFragment.ActionControlsListener {

    private var googleMap: GoogleMap? = null
    private var workoutMessenger: Messenger? = null
    private var actionControlFragment: ActionControlsFragment? = null

    private var workoutStatus: Int = WorkoutStatus.UNKNOWN
        set(value) {
            field = value
            actionControlFragment?.status = field
        }

    //Monitors the state of the connection to the service.
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            workoutMessenger = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            workoutMessenger = Messenger(service)
        }
    }

    private val workoutReceiver: WorkoutReceiver by lazy {
        object : WorkoutReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (view == null || isAdded.not()) return
                if (intent?.action != WorkoutService.ACTION_BROADCAST) return
                runOnUiThread {
                    workoutStatus = intent.getIntExtra(KEY_STATUS, WorkoutStatus.UNKNOWN)
                    when (workoutStatus) {
                        WorkoutStatus.UNKNOWN -> {
                            //Nothing for now
                        }
                        WorkoutStatus.READY -> {
                            //Update location when have location data from intent
                            intent.getParcelableExtra<WorkingOutLocation>(KEY_LOCATION)?.also { location ->
                                moveMapCameraToLocation(location)
                            }
                        }
                        WorkoutStatus.WORKING_OUT -> {
                            intent.getParcelableExtra<WorkingOutDisplayData>(KEY_DATA)?.also { displayData ->
                                updateUi(displayData)
                            }

                            //TODO("For test show current location")
                            intent.getParcelableExtra<WorkingOutLocation>(KEY_LOCATION)?.also { location ->
                                moveMapCameraToLocation(location)
                            }
                        }
                        WorkoutStatus.PAUSE -> {
                            //Nothing for now
                        }
                        WorkoutStatus.STOP -> {
                            //Nothing for now
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.warning(simpleName(), "onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_workout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Logger.warning(simpleName(), "onHiddenChanged: $hidden")
        when (hidden) {
            true -> {
                unbindWorkoutServiceWhenNotForeGround()
            }
            false -> {
                setStatusBarColor(isLightStatusBar = true)
                bindWorkoutServiceIfNotRunning()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Logger.warning(simpleName(), "onStart")

        //Initial google map and bind workout service.
        initGoogleMap {
            bindWorkoutServiceIfNotRunning()
            LocalBroadcastManager.getInstance(requireContext())
                    .registerReceiver(workoutReceiver, IntentFilter(WorkoutService.ACTION_BROADCAST))
        }
    }

    override fun onResume() {
        super.onResume()
        Logger.warning(simpleName(), "onResume")
    }

    override fun onPause() {
        super.onPause()
        Logger.warning(simpleName(), "onPause")
    }

    override fun onStop() {
        super.onStop()
        Logger.warning(simpleName(), "onStop")
        try {
            unbindWorkoutServiceWhenNotForeGround()
            LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(workoutReceiver)

        } catch (error: Exception) {
            error.printStackTrace()
        }
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = true)
        actionControlFragment = childFragmentManager.findFragmentById(R.id.action_control) as ActionControlsFragment
    }

    private fun subscribeUi() {
    }

    //TODO("Have only workout type 'running' for now ")
    override fun onActionStart() {
        workoutMessenger?.run {
            send(Message.obtain(null, WorkoutAction.START).apply {
                data = Bundle().apply {
                    putString(KEY_TYPE, WorkoutType.RUNNING)
                }
            })
        }
    }

    override fun onActionPause() {
        workoutMessenger?.run {
            send(Message.obtain(null, WorkoutAction.PAUSE))
        }
    }

    override fun onActionResume() {
        workoutMessenger?.run {
            send(Message.obtain(null, WorkoutAction.RESUME))
        }
    }

    override fun onActionStop() {
        showAlertDialog(title = getString(R.string.confirm),
                message = getString(R.string.confirm_to_ending_run),
                positiveText = getString(R.string.confirm),
                negativeText = getString(R.string.cancel),
                onPositiveClick = {
                    workoutMessenger?.run {
                        send(Message.obtain(null, WorkoutAction.STOP))
                    }
                })

    }

    private fun updateUi(displayData: WorkingOutDisplayData) {
        time_duration_label?.text = displayData.duration
        distance_label?.text = displayData.distance
    }

    private fun initGoogleMap(callbacks: () -> Unit) {
        if (googleMap != null) {
            callbacks.invoke()
            return
        }
        (childFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment)?.also { mapFragment ->
            mapFragment.getMapAsync { googleMap: GoogleMap ->
                onMapReady(googleMap)
                callbacks.invoke()
            }
        }
    }

    private fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        this.googleMap?.uiSettings?.isMyLocationButtonEnabled = false
    }


    private fun bindWorkoutService() {
        if (checkLocationPermissionsAndOpenGps()) {
            val intent = Intent(requireContext(), WorkoutService::class.java)
            requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            Logger.info(simpleName(), "bindWorkoutService")
        }
    }

    /**
     * Check workout service is running, if not runnig
     * will be bind service
     */
    private fun bindWorkoutServiceIfNotRunning() {
        if (requireContext().serviceIsRunning(WorkoutService::class.java).not()) {
            Logger.warning(simpleName(), "Workout service not running")
            bindWorkoutService()
        } else {
            //Service is running will be enable my location on map
            checkLocationPermissionsAndOpenGps()
        }
    }

    private fun unbindWorkoutService() = try {
        requireContext().unbindService(serviceConnection)
        Logger.error(simpleName(), "unbindWorkoutService")
    } catch (error: Exception) {
        error.printStackTrace()
    }

    /**
     * Check workout service is running in foreground (On working out?)
     * if service is not foreground will be unbind service
     */
    private fun unbindWorkoutServiceWhenNotForeGround() {
        if (requireContext().serviceIsRunningInForeground(WorkoutService::class.java).not()) {
            Logger.warning(simpleName(), "Workout service not running in foreground")
            unbindWorkoutService()
        }
    }

    private fun moveMapCameraToLocation(location: WorkingOutLocation) {
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), GOOGLE_MAP_DEFAULT_ZOOM))
    }


    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        //Check location permissions before get last location
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (havePermissions(permissions, RC_PERMISSION_LOCATION)) {
            googleMap?.isMyLocationEnabled = true
            when (LocationUtil.isGpsEnabled(requireContext())) {
                true -> {
                    //TODO("May be update ui to detecting location")
                    LocationUtil.getLastLocation(requireContext()) { location ->
                        if (location != null) {
                            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), GOOGLE_MAP_DEFAULT_ZOOM))
                        }
                    }
                }
                false -> LocationUtil.openGps(requireActivity())
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun checkLocationPermissionsAndOpenGps(): Boolean {
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (havePermissions(permissions, RC_PERMISSION_LOCATION)) {
            googleMap?.isMyLocationEnabled = true
            when (LocationUtil.isGpsEnabled(requireContext())) {
                true -> return true
                false -> LocationUtil.openGps(requireActivity())
            }
        }
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            RC_PERMISSION_LOCATION -> when {
                //User allow to access location.
                allPermissionsGranted(grantResults) -> bindWorkoutService()
                //User denied access to location.
                shouldShowPermissionRationale(permissions) -> showToast(R.string.location_permission_denied)
                //User tick Don't ask again.
                else -> showSettingPermissionInSettingDialog()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RC_OPEN_GPS -> if (resultCode == Activity.RESULT_OK) {
                launch {
                    delay(100)
                    bindWorkoutService()
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun showSettingPermissionInSettingDialog() = showAlertDialog(
            message = R.string.allow_permission_in_setting,
            positiveText = R.string.setting) {

        //On positive click, go to permission setting screen.
        val uri = Uri.parse("package:${requireContext().packageName}")
        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Logger.warning(simpleName(), "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.error(simpleName(), "onDestroy")
    }
}