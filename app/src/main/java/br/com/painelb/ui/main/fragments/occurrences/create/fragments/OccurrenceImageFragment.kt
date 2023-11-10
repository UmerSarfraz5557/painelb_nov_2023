package br.com.painelb.ui.main.fragments.occurrences.create.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import br.com.painelb.R
import br.com.painelb.base.ui.BaseFragment
import br.com.painelb.databinding.FragmentOccurrenceImageBinding
import br.com.painelb.databinding.ListItemImageBinding
import br.com.painelb.db.table.occurrence.OccurrencePhoto
import br.com.painelb.di.modules.viemodel.AppViewModelFactory
import br.com.painelb.domain.result.Event
import br.com.painelb.domain.result.EventObserver
import br.com.painelb.ui.main.fragments.occurrences.create.CreateOccurrenceViewModel
import br.com.painelb.util.layoutInflater
import br.com.painelb.util.parentViewModelProvider
import br.com.painelb.util.requestPermissions
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import droidninja.filepicker.utils.GridSpacingItemDecoration
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*
import javax.inject.Inject


class OccurrenceImageFragment :
    BaseFragment<FragmentOccurrenceImageBinding>(R.layout.fragment_occurrence_image),
    EasyPermissions.PermissionCallbacks {

    companion object {
        private const val PERMISSIONS_REQUEST_STORAGE_READ = 881
        private const val REQUEST_CODE_PICK_FILE = 882
    }

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    lateinit var viewModel: CreateOccurrenceViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = parentViewModelProvider(viewModelFactory)
        mBinding.viewModel = viewModel

        viewModel.storageReadPermissionEvent.observe(viewLifecycleOwner, EventObserver {
            requestPermissions(
                PERMISSIONS_REQUEST_STORAGE_READ,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        })
        viewModel.navigateFileScreen.observe(viewLifecycleOwner, EventObserver {
            FilePickerBuilder.instance
                .setMaxCount(1)
                .setActivityTheme(R.style.FilePickerTheme)
                .setActivityTitle(getString(R.string.add_images))
                .enableVideoPicker(false)
                .enableCameraSupport(true)
                .showGifs(false)
                .showFolderView(true)
                .enableSelectAll(false)
                .enableImagePicker(true)
                .withOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .pickPhoto(this, REQUEST_CODE_PICK_FILE)
        })
        val spanCunt = 3
        val imageAdapter = ImageListAdapter(requireContext(), viewModel, true, spanCunt)
        val manager = StaggeredGridLayoutManager(spanCunt, StaggeredGridLayoutManager.VERTICAL)
        val spacing = requireContext().resources.getDimensionPixelSize(R.dimen.spacing_normal)
        val decoration = GridSpacingItemDecoration(spanCunt, spacing, false)
        mBinding.imageRecyclerView.apply {
            layoutManager = manager
            adapter = imageAdapter
            addItemDecoration(decoration)
        }
        viewModel.photos.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            imageAdapter.submitList(it)
            imageAdapter.notifyDataSetChanged()
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        when (requestCode) {
            PERMISSIONS_REQUEST_STORAGE_READ -> viewModel.onNavigateFileScreen()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (REQUEST_CODE_PICK_FILE == requestCode && resultCode == Activity.RESULT_OK && data != null) {
            val files: ArrayList<String>? =
                data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA)
            processFile(files)
        } else super.onActivityResult(requestCode, resultCode, data)
    }

    private fun processFile(files: ArrayList<String>?) {
        when {
            files != null && files.size > 0 -> {
                val filePath = files[0]
                viewModel.uploadFile(filePath)
            }
            else -> viewModel.messageEvent.postValue(Event(getString(R.string.error_invalid_file)))
        }
    }
}

class ImageListAdapter(
    context: Context,
    private val viewModel: CreateOccurrenceViewModel?,
    private val deleteEnable: Boolean,
    spanCunt: Int
) :
    ListAdapter<OccurrencePhoto, ImageListAdapter.ViewHolder>(DistrictDiff) {
    private var imageSize: Int = 0

    init {
        setColumnNumber(context, spanCunt)
    }

    private fun setColumnNumber(context: Context, columnNum: Int) {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(metrics)
        val widthPixels = metrics.widthPixels
        imageSize = widthPixels / columnNum
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemImageBinding.inflate(parent.layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel, deleteEnable, imageSize)
    }

    class ViewHolder(private val binding: ListItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            photo: OccurrencePhoto,
            viewModel: CreateOccurrenceViewModel?,
            deleteEnable: Boolean,
            imageSize: Int
        ) {
            binding.deleteImageView.visibility = if (deleteEnable) View.VISIBLE else View.GONE
            binding.deleteImageView.setOnClickListener { viewModel?.onRemovePhoto(adapterPosition) }

            val requestOptions = RequestOptions
                .centerCropTransform()
                .override(imageSize, imageSize)
                .placeholder(R.drawable.image_placeholder)

            Glide.with(binding.selectImageView).load(photo.photo)
                .apply(requestOptions)
                .thumbnail(0.5f)
                .into(binding.selectImageView)
            binding.executePendingBindings()
        }
    }

    object DistrictDiff : DiffUtil.ItemCallback<OccurrencePhoto>() {
        override fun areItemsTheSame(oldItem: OccurrencePhoto, newItem: OccurrencePhoto) =
            oldItem.photoId == newItem.photoId

        override fun areContentsTheSame(oldItem: OccurrencePhoto, newItem: OccurrencePhoto) =
            oldItem == newItem
    }
}
