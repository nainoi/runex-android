package com.think.runex.feature.workout.summary

import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.jozzee.android.core.resource.getColor
import com.jozzee.android.core.resource.getDimension
import com.jozzee.android.core.util.Logger
import com.jozzee.android.core.util.simpleName
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.showToast
import com.jozzee.android.core.view.visible
import com.think.runex.R
import com.think.runex.common.*
import com.think.runex.config.KEY_DATA
import com.think.runex.config.KEY_ID
import com.think.runex.feature.workout.data.WorkingOutRecord
import com.think.runex.feature.workout.WorkoutViewModel
import com.think.runex.feature.workout.data.WorkingOutLocation
import com.think.runex.feature.workout.data.WorkoutInfo
import com.think.runex.base.BaseScreen
import com.think.runex.feature.event.data.EventRegisteredForSubmitResult
import com.think.runex.feature.event.registered.SelectEventsBottomSheet
import com.think.runex.feature.workout.MapPresenter
import com.think.runex.util.NightMode
import com.think.runex.util.launch
import io.realm.Realm
import kotlinx.android.synthetic.main.layout_workout_summary_on_map.*
import kotlinx.android.synthetic.main.screen_workout_summary.*
import kotlinx.android.synthetic.main.toolbar.*

class WorkoutSummaryScreen : BaseScreen(), SelectEventsBottomSheet.OnConfirmSelectEventToSubmitListener {

    companion object {
        @JvmStatic
        fun newInstance(record: WorkingOutRecord) = WorkoutSummaryScreen().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_DATA, record)
            }
        }

        @JvmStatic
        fun newInstance(workoutId: String) = WorkoutSummaryScreen().apply {
            arguments = Bundle().apply {
                putString(KEY_ID, workoutId)
            }
        }
    }

    private lateinit var viewModel: WorkoutViewModel
    private var mapPresenter: MapPresenter? = null

    private var record: WorkingOutRecord? = null
    private var workoutId: String? = null
    private var workoutInfo: WorkoutInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = getViewModel(WorkoutViewModel.Factory(requireContext()))

        record = arguments?.getParcelable(KEY_DATA)
        workoutId = arguments?.getString(KEY_ID)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_workout_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()

        when (workoutId.isNullOrBlank() && record != null) {
            true -> performAddWorkout()
            false -> performGetWorkoutInfo()
        }
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
        setupToolbar(toolbar, R.string.summary, R.drawable.ic_navigation_back)
    }

    private fun subscribeUi() {
        submit_button?.setOnClickListener {
            showBottomSheet(SelectEventsBottomSheet())
        }

        viewModel.setOnHandleError(::errorHandler)
    }

    private fun performAddWorkout() = launch {
        progress_layout?.visible()

        val locations = Realm.getDefaultInstance().run {
            copyFromRealm(where(WorkingOutLocation::class.java).findAllAsync()) ?: emptyList()
        }
        val workout = WorkoutInfo(record, locations)
        //Logger.warning(simpleName(), "Workout: ${workout.toJson()}")
        workoutInfo = viewModel.addWorkout(workout)
        workoutId = workoutInfo?.id
        if (workoutInfo != null) {
            //Clear temp locations
            Realm.getDefaultInstance().run {
                beginTransaction()
                delete(WorkingOutLocation::class.java)
                commitTransaction()
            }
        }

        progress_layout?.gone()

        updateUi()
    }

    private fun performGetWorkoutInfo() = launch {
        progress_layout?.visible()

        workoutInfo = viewModel.getWorkoutInfo(workoutId ?: "")
        //workoutId = workoutInfo?.id

        progress_layout?.gone()

        updateUi()
    }

    private fun performSubmitWorkoutToEvents(events: List<EventRegisteredForSubmitResult>, workoutImage: Bitmap?) = launch {

        showProgressDialog(R.string.submit_result)

        val isSuccess = viewModel.submitWorkoutToEvents(events, workoutInfo, workoutImage)

        hideProgressDialog()
        workoutImage?.recycle()

        if (isSuccess) {

            showAlertDialog(R.string.success)

            //Update workout info
            workoutInfo = viewModel.getWorkoutInfo(workoutId ?: "")
            updateUi()
        }
    }

    private fun updateUi() {
        //Update record data to views.
        workoutInfo?.getDisplayData()?.also { displayDate ->
            distance_on_map_label?.text = displayDate.distances
            duration_on_map_label?.text = displayDate.duration
            distance_label?.text = displayDate.distances
            duration_label?.text = displayDate.duration
            duration_per_kilometer_label?.text = displayDate.durationPerKilometer
            duration_per_kilometer_placeholder?.text = displayDate.durationPerKilometerUnit
            calorie_label?.text = displayDate.calories
        }
        submit_button?.isEnabled = workoutInfo?.isSync != true

        initMaps {
            workoutInfo?.locations?.also {
                mapPresenter?.drawPolyline(it)
                mapPresenter?.zoomToFitWorkoutLine()
            }
        }
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
                googleMap.uiSettings.setAllGesturesEnabled(false)
                mapPresenter = MapPresenter(googleMap, getColor(R.color.colorAccent), getDimension(R.dimen.space_8dp).toFloat())
                Logger.warning(simpleName(), "Setup mapPresenter")
                map_layout?.visible()
                callbacks.invoke()
            }
        }
    }

    override fun onConfirmSelectEventToSubmit(events: List<EventRegisteredForSubmitResult>) {
        mapPresenter?.snapshot {
            performSubmitWorkoutToEvents(events, it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_workout_summary, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_share -> {
            showBottomSheet(ShareWorkoutBottomSheet.newInstance(workoutInfo))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun errorHandler(statusCode: Int, message: String, tag: String?) {
        when (tag) {
            "submit_workout" -> {
                showToast(message)
            }
            else -> super.errorHandler(statusCode, message, tag)
        }
    }

    override fun onDestroy() {
        mapPresenter?.clear()
        super.onDestroy()
    }
}