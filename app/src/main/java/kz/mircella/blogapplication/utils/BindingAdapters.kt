package kz.mircella.blogapplication.utils

import android.net.Uri
import android.text.SpannableString
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.marcinmoskala.videoplayview.VideoPlayView
import com.ramotion.garlandview.TailAdapter
import com.ramotion.garlandview.TailItem
import com.ramotion.garlandview.TailLayoutManager
import com.ramotion.garlandview.TailRecyclerView
import com.ramotion.garlandview.TailSnapHelper
import com.ramotion.garlandview.header.HeaderTransformer
import com.squareup.picasso.Picasso
import kz.mircella.blogapplication.R
import kz.mircella.blogapplication.ui.forum.ForumTopicAdapter
import kz.mircella.blogapplication.utils.extensions.getParentActivity

@BindingAdapter("mutableVisibility")
fun setMutableVisibility(view: View, visibility: MutableLiveData<Int>?) {
    val parentActivity = view.getParentActivity()
    if (parentActivity != null && visibility != null) {
        visibility.observe(parentActivity, Observer { value -> view.visibility = value ?: View.VISIBLE })
    }
}

@BindingAdapter("mutableText")
fun setMutableText(view: TextView, text: MutableLiveData<String>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value -> view.text = value ?: "" })
    }
}

@BindingAdapter("mutableSpannableText")
fun setMutableSpannableText(view: TextView, text: MutableLiveData<SpannableString>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value -> view.text = value ?: "" })
    }
}

@BindingAdapter("recyclerViewAdapter")
fun setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter
}

@BindingAdapter("imageUrl")
fun setImageUri(view: ImageView, imageUri: String?) {
    val options = RequestOptions
            .placeholderOf(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .centerCrop()
            .fitCenter()
    val fullUrl = imageUri?.let{
        Uri.parse(it)
            .buildUpon()
            .build()
            .toString()
    }
    fullUrl?.let { Glide
            .with(view.context)
            .applyDefaultRequestOptions(options)
            .load(it)
            .into(view) }
}

@BindingAdapter("viewPagerAdapter")
fun setAdapter(pager: ViewPager, adapter: PagerAdapter) {
    pager.adapter = adapter
}

@BindingAdapter("videoImageUrl")
fun setVideoImageUrl(view: VideoPlayView, imageUri: MutableLiveData<String>?){
    val parentActivity = view.getParentActivity()
    if (parentActivity != null && imageUri != null) {
        imageUri.observe(parentActivity, Observer { value ->
            val fullUrl =
                Uri.parse(value)
                        .buildUpon()
                        .build()
                        .toString()
            Picasso.with(view.context).load(fullUrl).into(view.imageView)
        })
    }

}

@BindingAdapter("videoUri")
fun setVideoUri(view: VideoPlayView, videoUri: MutableLiveData<String>?){
    val parentActivity = view.getParentActivity()
    if (parentActivity != null && videoUri != null) {
        videoUri.observe(parentActivity, Observer { value ->
            val a = parentActivity.resources.getIdentifier(value.substring(0, value.length - 4), "raw", parentActivity.packageName)
            view.videoUrl = "android.resource://${parentActivity.packageName}/$a"
        })
    }
}

@BindingAdapter("tailRecyclerViewAdapter")
fun <T : TailItem<*>?> setAdapter(tailRecyclerView: TailRecyclerView, adapter: TailAdapter<T>) {
    (tailRecyclerView.layoutManager as TailLayoutManager).setPageTransformer(HeaderTransformer())
    tailRecyclerView.adapter = ForumTopicAdapter()
    TailSnapHelper().attachToRecyclerView(tailRecyclerView)
    tailRecyclerView.adapter = adapter
}