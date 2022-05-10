package com.example.featureGameDetails.presentation

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.core.presentaton.recyclerView.BaseRecyclerViewAdapter
import com.example.featureGameDetails.R
import com.example.featureGameDetails.databinding.FragmentGameDetailsBinding
import com.example.featureGameDetails.domain.entities.GameScreenshot
import com.example.featureGameDetails.presentation.viewPager.GameScreenshotAdapterDelegate

class GameDetailsFragment : Fragment() {
    private var binding: FragmentGameDetailsBinding? = null
//    private val adapter = BaseRecyclerViewAdapter(
//        listOf(GameScreenshotAdapterDelegate())
//    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentGameDetailsBinding.inflate(inflater, container, true).let {
        binding = it; it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        binding?.screenshotViewPager?.adapter = adapter
//        adapter.submitList(getPostImagesList())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun getPostImagesList(): List<GameScreenshot> = listOf(
        GameScreenshot(BitmapDrawable(resources, BitmapFactory.decodeResource(resources, R.drawable.im_game))),
        GameScreenshot(BitmapDrawable(resources, BitmapFactory.decodeResource(resources, R.drawable.im_game))),
        GameScreenshot(BitmapDrawable(resources, BitmapFactory.decodeResource(resources, R.drawable.im_game))),
        GameScreenshot(BitmapDrawable(resources, BitmapFactory.decodeResource(resources, R.drawable.im_game))),
        GameScreenshot(BitmapDrawable(resources, BitmapFactory.decodeResource(resources, R.drawable.im_game))),
    )
}