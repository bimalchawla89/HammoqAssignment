package com.example.hammoqassignment.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import com.example.hammoqassignment.R
import com.example.hammoqassignment.adapter.ImageAdapter
import com.example.hammoqassignment.base.BaseActivity
import com.example.hammoqassignment.databinding.ActivityMainBinding
import com.example.hammoqassignment.databinding.setImage
import com.example.hammoqassignment.extensions.gone
import com.example.hammoqassignment.extensions.hide
import com.example.hammoqassignment.extensions.visible
import com.example.hammoqassignment.listener.ImageClickListener
import com.example.hammoqassignment.models.Photo
import com.example.hammoqassignment.viewbinding.viewBinding
import com.example.hammoqassignment.viewmodel.ImageViewModel
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation


@AndroidEntryPoint
class MainActivity : BaseActivity(), ImageClickListener {

    override val binding: ActivityMainBinding by viewBinding()
    private val _imageViewModel: ImageViewModel by viewModels()
    private val _imageAdapter by lazy { ImageAdapter(this) }
    private var _imageList = listOf<Photo>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.rvImages.adapter = _imageAdapter

        binding.etSearch.setOnTouchListener(OnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP && event.rawX >= binding.etSearch.right - binding.etSearch.compoundDrawables[2].bounds.width()
            ) {
                binding.progressBar.visible()
                binding.ivFullImage.gone()
                binding.ivClose.gone()
                binding.divider.gone()
                binding.tvNext.gone()
                binding.tvPrevious.gone()
                _imageViewModel.fetchImages(binding.etSearch.text.toString())
                return@OnTouchListener true
            }
            false
        })

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.progressBar.visible()
                binding.ivFullImage.gone()
                binding.ivClose.gone()
                binding.divider.gone()
                binding.tvPrevious.gone()
                _imageViewModel.fetchImages(binding.etSearch.text.toString())
                true
            } else false
        }

        binding.tvNext.setOnClickListener {
            binding.progressBar.visible()
            _imageViewModel.fetchNextImages(binding.etSearch.text.toString())
            binding.tvPrevious.visible()
            binding.tvNext.hide()
        }

        binding.tvPrevious.setOnClickListener {
            binding.progressBar.visible()
            _imageViewModel.fetchImages(binding.etSearch.text.toString())
            binding.tvNext.hide()
            binding.tvPrevious.hide()
        }

        binding.ivClose.setOnClickListener {
            binding.ivFullImage.gone()
            binding.ivClose.gone()
            binding.divider.gone()
        }

        _imageViewModel.imageListData.observe(this, {
            binding.progressBar.gone()
            it?.let {
                _imageList = it.photos
                _imageAdapter.submitList(ArrayList(it.photos))
                if (it.photos.size == 9) {
                    binding.tvNext.visible()
                }
            } ?: run {
                Snackbar.make(
                    binding.clMainLayout,
                    getString(R.string.no_data_found),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })

        _imageViewModel.nextImageListData.observe(this, {
            binding.progressBar.gone()
            it?.let {
                _imageList = it.photos
                _imageAdapter.submitList(ArrayList(it.photos))
            } ?: run {
                Snackbar.make(
                    binding.clMainLayout,
                    getString(R.string.no_data_found),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun imageClicked(position: Int) {
        binding.ivFullImage.visible()
        binding.ivClose.visible()
        binding.divider.visible()
        Picasso.get().load(_imageList[position].source.large)
            .transform(RoundedCornersTransformation(12, 12)).into(binding.ivFullImage)

    }

}