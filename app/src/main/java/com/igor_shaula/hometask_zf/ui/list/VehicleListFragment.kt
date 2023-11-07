package com.igor_shaula.hometask_zf.ui.list

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.igor_shaula.hometask_zf.data.VehicleStatus
import com.igor_shaula.hometask_zf.data.toVehicleRecordList
import com.igor_shaula.hometask_zf.databinding.FragmentVehiclesListBinding
import timber.log.Timber

class VehicleListFragment : Fragment() {

    companion object {
        fun newInstance() = VehicleListFragment()
    }

    private lateinit var binding: FragmentVehiclesListBinding

    private lateinit var viewModel: VehicleListViewModel

    private lateinit var rvAdapter: VehicleListAdapter

    // region standard lifecycle androidx.fragment.app.Fragment callbacks

    @Deprecated("Deprecated in Java")
    override fun onInflate(activity: Activity, attrs: AttributeSet, savedInstanceState: Bundle?) {
        super.onInflate(activity, attrs, savedInstanceState)
        Timber.v("onInflate deprecated")
    }

    override fun onInflate(context: Context, attrs: AttributeSet, savedInstanceState: Bundle?) {
        super.onInflate(context, attrs, savedInstanceState)
        Timber.v("onInflate - 1")
    }

    @Deprecated("Deprecated in Java")
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        Timber.v("onAttach - 2 deprecated")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.v("onAttach - 3")
        // java.lang.IllegalStateException: Can't access ViewModels from detached fragment
        viewModel = ViewModelProvider(this)[VehicleListViewModel::class.java]
        // TODO: Use the ViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.v("onCreate - 4")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Timber.v("onCreateView - 5")
        binding = FragmentVehiclesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.v("onViewCreated - 6")
        binding.actvHeader.text = "the text"
        prepareVehiclesListUI()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.v("onActivityCreated - 7 deprecated")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Timber.v("onViewStateRestored - 8")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStart() {
        super.onStart()
        Timber.v("onStart - 9")
        viewModel.vehiclesMapMLD.observe(viewLifecycleOwner) { thisMap ->
            showDataInTheList(thisMap.toList())
            thisMap.forEach { (key, _) ->
                getDataForEveryVehicle(key)
            }
        }
        viewModel.timeToUpdateVehicleStatus.observe(viewLifecycleOwner) {
            viewModel.vehiclesMapMLD.value?.toList()?.let { showDataInTheList(it) }
        }
    }

    override fun onResume() {
        super.onResume()
        Timber.v("onResume - 10")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Timber.v("onSaveInstanceState - 11")
    }

    // endregion standard androidx.fragment.app.Fragment callbacks

    // region other standard androidx.fragment.app.Fragment callbacks

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Timber.v("onConfigurationChanged")
    }

    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater {
        Timber.v("onGetLayoutInflater")
        return super.onGetLayoutInflater(savedInstanceState)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Timber.v("onHiddenChanged")
    }

    override fun onPrimaryNavigationFragmentChanged(isPrimaryNavigationFragment: Boolean) {
        super.onPrimaryNavigationFragmentChanged(isPrimaryNavigationFragment)
        Timber.v("onPrimaryNavigationFragmentChanged")
    }

    // endregion other standard androidx.fragment.app.Fragment callbacks

    // region work with the List

    private fun prepareVehiclesListUI() {
        rvAdapter = VehicleListAdapter { vehicleItemRecord, position ->
            Timber.d("click function: vehicleItemRecord = $vehicleItemRecord, position = $position")
        }
        binding.rvVehiclesList.adapter = rvAdapter
        binding.rvVehiclesList.layoutManager = LinearLayoutManager(context)
    }

    private fun showDataInTheList(pairs: List<Pair<String, VehicleStatus>>) {
        rvAdapter.setItems(pairs.toVehicleRecordList())
    }

    private fun getDataForEveryVehicle(vehicleId: String) {
        viewModel.timeToGetAllDetails(vehicleId)
    }

    // endregion work with the List
}
