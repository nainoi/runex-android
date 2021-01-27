package com.think.runex.ui.workout

import android.os.Bundle
import android.view.*
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.SupportMapFragment
import com.jozzee.android.core.resource.getColor
import com.jozzee.android.core.resource.getDimension
import com.jozzee.android.core.util.Logger
import com.jozzee.android.core.util.simpleName
import com.think.runex.R
import com.think.runex.common.setStatusBarColor
import com.think.runex.common.setupToolbar
import com.think.runex.config.KEY_DATA
import com.think.runex.feature.workout.WorkoutRecord
import com.think.runex.ui.base.BaseScreen
import com.think.runex.util.NightMode
import kotlinx.android.synthetic.main.screen_workout_summary.*
import kotlinx.android.synthetic.main.toolbar.*

class WorkoutSummaryScreen : BaseScreen() {

    companion object {
        @JvmStatic
        fun newInstance(record: WorkoutRecord) = WorkoutSummaryScreen().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_DATA, record)
            }
        }
    }

    private var mapPresenter: MapPresenter? = null

    private var record: WorkoutRecord? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        record = arguments?.getParcelable(KEY_DATA)

        //TODO("Force set record fro test")
        //{"distances":168.84119,"durationMillis":42364,"startMillis":1611761954202,"stopMillis":0,"type":"Running"}​
        //record = WorkoutRecord(WorkoutType.RUNNING).apply {
        //    distances = 210.57405f
        //    durationMillis = 25148
        //    startMillis = 1611762302729
        //    stopMillis = 1611762327895
        //}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_workout_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()

        //Initial google map and update views.
        initMaps {
            mapPresenter?.drawPolylineFromDatabase()
            mapPresenter?.zoomToFitWorkoutLine()
            record?.getDisplayData()?.also { displayDate ->
                distance_in_map_label?.text = displayDate.distances
                duration_in_map_label?.text = displayDate.duration
                distance_label?.text = displayDate.distances
                duration_label?.text = displayDate.duration
                duration_per_kilometer_label?.text = displayDate.durationPerKilometer
                duration_per_kilometer_placeholder?.text = displayDate.durationPerKilometerUnit
                calorie_label?.text = displayDate.calories
            }
        }
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
        setupToolbar(toolbar, R.string.summary, R.drawable.ic_navigation_back)
    }

    private fun subscribeUi() {
        submit_button?.setOnClickListener {

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
                mapPresenter = MapPresenter(googleMap, getColor(R.color.colorAccent), getDimension(R.dimen.space_8dp).toFloat())
                Logger.warning(simpleName(), "Setup mapPresenter")
                callbacks.invoke()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_workout_summary, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_share -> {
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}