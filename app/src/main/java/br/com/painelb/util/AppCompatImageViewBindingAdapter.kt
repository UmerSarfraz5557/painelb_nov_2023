package br.com.painelb.util

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory

@BindingMethods(
    BindingMethod(
        type = AppCompatImageView::class,
        attribute = "app:tint",
        method = "setImageTintList"
    )
)
@Suppress("unused")
class AppCompatImageViewBindingAdapter

@BindingAdapter("imageUri")
fun AppCompatImageView.imageUri(imageUri: Uri?) {
    val glideRequests = Glide.with(this)
    val fullRequest = glideRequests.asDrawable().centerCrop()
        .placeholder(ColorDrawable(Color.GRAY))
        .error(ColorDrawable(Color.GRAY))
        .fallback(ColorDrawable(Color.GRAY))
    val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
    val thumbRequest = glideRequests
        .asDrawable()
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .transition(withCrossFade(factory))
    fullRequest.load(imageUri).thumbnail(thumbRequest.load(imageUri)).into(this)
}

@BindingAdapter("imageUrl")
fun AppCompatImageView.imageUrl(imageUrl: String?) {
    this.imageUri(imageUrl?.toUri())
}

@BindingAdapter("imageUri2")
fun imageUri2(imageView: ImageView, imageUri: String?) {
    when (imageUri.isNullOrEmpty()) {
        true -> {
            Glide.with(imageView)
                .load(ColorDrawable(Color.GRAY))
                .error(ColorDrawable(Color.GRAY))
                .fallback(ColorDrawable(Color.GRAY))
                .into(imageView)
        }
        else -> {
            //  val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
            Glide.with(imageView)
                .load(imageUri)
                .apply(RequestOptions().placeholder(ColorDrawable(Color.GRAY)))
                .error(ColorDrawable(Color.GRAY))
                .fallback(ColorDrawable(Color.GRAY))
                //   .transition(withCrossFade(factory))
                .into(imageView)
        }
    }
}
