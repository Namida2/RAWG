package com.example.featureGameDetails.presentation

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import com.example.core.presentaton.recyclerView.BaseRecyclerViewAdapter
import com.example.featureGameDetails.R
import com.example.featureGameDetails.databinding.FragmentGameDetailsBinding
import com.example.featureGameDetails.domain.entities.GameScreenshot
import com.example.featureGameDetails.presentation.viewPager.GamePageTransformer
import com.example.featureGameDetails.presentation.viewPager.adapterDelegates.GameScreenshotAdapterDelegate
import com.example.featureGameDetails.presentation.viewPager.itemDecorations.GameScreenshotsItemDecorations
import com.google.android.material.transition.platform.MaterialContainerTransform

import kotlin.properties.Delegates

class GameDetailsFragment : Fragment() {
    private var defaultScale by Delegates.notNull<Float>()
    private var currentPageMargin by Delegates.notNull<Int>()
    private var pageLargeMargin by Delegates.notNull<Int>()
    private var pageSmallMargin by Delegates.notNull<Int>()
    private var binding: FragmentGameDetailsBinding? = null
    private val adapter = BaseRecyclerViewAdapter(
        listOf(GameScreenshotAdapterDelegate())
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val outValue = TypedValue()
        resources.getValue(R.dimen.default_scale, outValue, true)
        defaultScale = outValue.float
        pageLargeMargin = resources.getDimensionPixelSize(R.dimen.page_large_margin)
        pageSmallMargin = resources.getDimensionPixelSize(R.dimen.page_small_margin)
        currentPageMargin = resources.getDimensionPixelSize(R.dimen.current_page_margin)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = resources.getInteger(com.example.core.R.integer.defaultAnimationDuration).toLong()
            scrimColor = Color.TRANSPARENT
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentGameDetailsBinding.inflate(inflater, container, false).let {
        binding = it; it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        binding?.screenshotViewPager?.adapter = adapter
        binding?.screenshotViewPager?.offscreenPageLimit = 1
        binding?.screenshotViewPager?.addItemDecoration(
            GameScreenshotsItemDecorations(currentPageMargin)
        )
        binding?.screenshotViewPager?.setPageTransformer(
            GamePageTransformer(defaultScale, currentPageMargin, pageLargeMargin)
        )
        adapter.submitList(getPostImagesList())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun getPostImagesList(): List<GameScreenshot> = listOf(
        GameScreenshot(
            BitmapDrawable(
                resources,
                BitmapFactory.decodeResource(resources, R.drawable.im_game_test)
            )
        ),
        GameScreenshot(
            BitmapDrawable(
                resources,
                BitmapFactory.decodeResource(resources, R.drawable.im_game_test)
            )
        ),
        GameScreenshot(
            BitmapDrawable(
                resources,
                BitmapFactory.decodeResource(resources, R.drawable.im_game_test)
            )
        ),
        GameScreenshot(
            BitmapDrawable(
                resources,
                BitmapFactory.decodeResource(resources, R.drawable.im_game_test)
            )
        ),
        GameScreenshot(
            BitmapDrawable(
                resources,
                BitmapFactory.decodeResource(resources, R.drawable.im_game_test)
            ),
        )
    )

}