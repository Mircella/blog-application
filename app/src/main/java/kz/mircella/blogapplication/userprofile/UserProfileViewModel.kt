package kz.mircella.blogapplication.userprofile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kz.mircella.blogapplication.base.BaseViewModel
import kz.mircella.blogapplication.network.UserProfileApiService
import timber.log.Timber
import javax.inject.Inject

class UserProfileViewModel : BaseViewModel() {
    @Inject
    lateinit var userProfileApiService: UserProfileApiService

    private lateinit var userProfileDisposable: Disposable
    private lateinit var userAvatarDisposable: Disposable
    private var userProfileSubscription: CompositeDisposable

    val userProfileLogin: MutableLiveData<String> = MutableLiveData()
    val userProfileStatus: MutableLiveData<String> = MutableLiveData()
    val userAvatar: MutableLiveData<Bitmap> = MutableLiveData()

    init {
        val userId = "2d1af9a6-30f1-4b96-ba39-9a75b28d5174"
        userProfileSubscription = CompositeDisposable()
        loadUser(userId)
    }

    override fun onCleared() {
        super.onCleared()
        userProfileSubscription.clear()
    }

    private fun loadUser(userId: String) {
        userProfileDisposable = userProfileApiService.getUserProfile(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { DisplayableUserProfile(it.login, it.userStatus.toString()) }
                .doOnSubscribe { onRetrieveUserProfileStart() }
                .subscribe(
                        { onRetrieveUserProfileSuccess(it) },
                        { onRetrieveUserProfileError() }
                ).addTo(userProfileSubscription)
        userAvatarDisposable = userProfileApiService.getUserAvatar(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    val userAvatarByteStream = it.byteStream()
                    BitmapFactory.decodeStream(userAvatarByteStream)
                }.doOnSubscribe { onRetrieveUserAvatarStart() }
                .subscribe(
                        { onRetrieveUserAvatarSuccess(it) },
                        { onRetrieveUserAvatarError() }
                ).addTo(userProfileSubscription)
    }

    private fun onRetrieveUserProfileError() {
        Timber.d("RetrieveUserProfileError")
    }

    private fun onRetrieveUserProfileSuccess(displayableUserProfile: DisplayableUserProfile) {
        Timber.d( "RetrieveUserProfileSuccess")
        userProfileLogin.value = displayableUserProfile.login
        userProfileStatus.value = displayableUserProfile.userStatus
    }

    private fun onRetrieveUserAvatarError() {
        Timber.d("RetrieveUserAvatarError")
    }

    private fun onRetrieveUserAvatarSuccess(userAvatarBitMap: Bitmap) {
        Timber.d("RetrieveUserAvatarSuccess")
       userAvatar.value = userAvatarBitMap
    }

    private fun onRetrieveUserProfileStart() {
        Timber.d("RetrieveUserProfileStart")
    }

    private fun onRetrieveUserAvatarStart() {
        Timber.d("RetrieveUserAvatarStart")
    }
}