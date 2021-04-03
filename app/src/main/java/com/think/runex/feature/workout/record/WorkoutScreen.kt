package com.think.runex.feature.workout.record

import android.Manifest
import android.app.Activity
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.jozzee.android.core.permission.shouldShowPermissionRationale
import com.jozzee.android.core.resource.getColor
import com.jozzee.android.core.resource.getDimension
import com.jozzee.android.core.util.Logger
import com.jozzee.android.core.util.simpleName
import com.jozzee.android.core.view.showToast
import com.think.runex.R
import com.think.runex.util.extension.*
import com.think.runex.config.*
import com.think.runex.feature.location.LocationUtil
import com.think.runex.feature.workout.*
import com.think.runex.feature.workout.data.*
import com.think.runex.base.PermissionsLauncherScreen
import com.think.runex.feature.workout.MapPresenter
import com.think.runex.feature.workout.summary.WorkoutSummaryScreen
import com.think.runex.util.launch
import com.think.runex.util.runOnUiThread
import kotlinx.android.synthetic.main.screen_workout.*
import kotlinx.coroutines.delay
import java.lang.Exception

class WorkoutScreen : PermissionsLauncherScreen(), ActionControlsFragment.ActionControlsListener {

    private var mapPresenter: MapPresenter? = null
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
                                mapPresenter?.animateCamera(location.toLatLng())
                            }
                        }
                        WorkoutStatus.WORKING_OUT -> {
                            intent.getParcelableExtra<WorkingOutDisplayData>(KEY_DATA)?.also { displayData ->
                                updateUi(displayData)
                            }

                            intent.getParcelableExtra<WorkingOutLocation>(KEY_LOCATION)?.also { location ->
                                mapPresenter?.addPolyline(location)
                                mapPresenter?.animateCamera(location.toLatLng(), GOOGLE_MAP_WORKING_OUT_ZOOM)
                            }
                        }
                        WorkoutStatus.PAUSE -> {
                            //Nothing for now
                        }
                        WorkoutStatus.STOP -> {
                            mapPresenter?.zoomToFitWorkoutLine()
                            intent.getParcelableExtra<WorkingOutRecord>(KEY_DATA)?.also { record ->
                                launch {
                                    addFragment(WorkoutSummaryScreen.newInstance(record))
                                    resetUi()
                                    delay(1000)
                                    workoutMessenger?.run {
                                        send(Message.obtain(null, WorkoutAction.CLEAR))
                                    }
                                }
                            }
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

        //Initial google map and bind workout service.
        initMaps {
            bindWorkoutService()
            LocalBroadcastManager.getInstance(requireContext())
                    .registerReceiver(workoutReceiver, IntentFilter(WorkoutService.ACTION_BROADCAST))
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Logger.warning(simpleName(), "onHiddenChanged: $hidden")
        when (hidden) {
            true -> unbindWorkoutService()
            false -> {
                setStatusBarColor(isLightStatusBar = true)
                bindWorkoutService()
            }
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

        //TODO("Force test workout summary screen")
        //addFragment(WorkoutSummaryScreen.newInstance(WorkoutRecord(WorkoutType.RUNNING)))
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
        workoutMessenger?.run {
            send(Message.obtain(null, WorkoutAction.STOP))
        }
    }

    private fun updateUi(displayData: WorkingOutDisplayData) {
        distance_label?.text = displayData.distances
        duration_label?.text = displayData.duration
        duration_per_kilometer_label?.text = displayData.durationPerKilometer
        duration_per_kilometer_placeholder?.text = ("Pace${displayData.durationPerKilometerUnit}")
        calorie_label?.text = displayData.calories
    }

    private fun resetUi() {
        distance_label?.text = ("0.00")
        duration_label?.text = getString(R.string.hint_time)
        duration_per_kilometer_label?.text = getString(R.string.hint_time_minute)
        duration_per_kilometer_placeholder?.text = getString(R.string.minutes_per_kilometer_placeholder)
        calorie_label?.text = "0"
        mapPresenter?.clearPolyline()
    }

    private fun initMaps(callbacks: () -> Unit) {
        if (mapPresenter != null) {
            callbacks.invoke()
            return
        }
        Logger.warning(simpleName(), "initialMaps...")
        (childFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment)?.also { mapFragment ->
            mapFragment.getMapAsync { googleMap: GoogleMap ->
                googleMap.uiSettings?.isMyLocationButtonEnabled = false
                mapPresenter = MapPresenter(googleMap, getColor(R.color.colorAccent), getDimension(R.dimen.space_8dp).toFloat())
                Logger.warning(simpleName(), "Setup mapPresenter")
                callbacks.invoke()
            }
        }
    }

    private fun bindWorkoutService() {
        checkLocationPermissionsAndOpenGps {
            val intent = Intent(requireContext(), WorkoutService::class.java)
            requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            Logger.info(simpleName(), "bindWorkoutService")
        }
    }

    private fun unbindWorkoutService() = try {
        requireContext().unbindService(serviceConnection)
        Logger.error(simpleName(), "unbindWorkoutService")
    } catch (error: Exception) {
        error.printStackTrace()
    }

    private fun checkLocationPermissionsAndOpenGps(callbacks: () -> Unit) {
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        requestPermissions(permissions) { results ->
            when {
                //User allow to access location.
                allPermissionsGranted(results) -> {
                    when (LocationUtil.isGpsEnabled(requireContext())) {
                        true -> {
                            mapPresenter?.setMyLocationEnabled(true)
                            callbacks.invoke()
                        }
                        false -> LocationUtil.openGps(requireActivity())
                    }
                }
                //User denied access to location.
                shouldShowPermissionRationale(permissions) -> showToast(R.string.location_permission_denied)
                //User tick Don't ask again.
                else -> requireContext().showSettingPermissionInSettingDialog()
            }
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

    override fun onDestroyView() {
        Logger.warning(simpleName(), "onDestroyView")
        super.onDestroyView()
        try {
            unbindWorkoutService()
            LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(workoutReceiver)

        } catch (error: Exception) {
            error.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mapPresenter?.clear()
        Logger.error(simpleName(), "onDestroy")
    }
}