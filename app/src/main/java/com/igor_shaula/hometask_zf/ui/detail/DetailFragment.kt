package com.igor_shaula.hometask_zf.ui.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.igor_shaula.hometask_zf.databinding.FragmentDetailBinding

class DetailFragment : DialogFragment() {

    private lateinit var binding: FragmentDetailBinding

    private lateinit var viewModel: DetailViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {

        private const val DETAILS_FRAGMENT_TAG = "DetailFragment"

        fun show(fragmentManager: FragmentManager) {
            if (fragmentManager.findFragmentByTag(DETAILS_FRAGMENT_TAG) == null) {
                DetailFragment().show(fragmentManager, DETAILS_FRAGMENT_TAG)
            }
        }
    }
}