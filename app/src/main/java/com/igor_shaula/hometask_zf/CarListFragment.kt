package com.igor_shaula.hometask_zf

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import timber.log.Timber

class CarListFragment : Fragment() {

    companion object {
        fun newInstance() = CarListFragment()
    }

    private lateinit var viewModel: CarListViewModel

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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.v("onCreate - 4")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.v("onCreateView - 5")
        return inflater.inflate(R.layout.fragment_car_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.v("onViewCreated - 6")
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.v("onActivityCreated - 7 deprecated")
        viewModel = ViewModelProvider(this).get(CarListViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Timber.v("onViewStateRestored - 8")
    }

    override fun onStart() {
        super.onStart()
        Timber.v("onStart - 9")
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

}