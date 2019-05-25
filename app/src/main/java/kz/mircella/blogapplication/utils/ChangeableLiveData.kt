package kz.mircella.blogapplication.utils

import androidx.lifecycle.MutableLiveData
import androidx.databinding.BaseObservable
import androidx.databinding.Observable


class ChangeableLiveData<T : BaseObservable> : MutableLiveData<T>() {

    internal var callback: Observable.OnPropertyChangedCallback = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable, propertyId: Int) {

            //Trigger LiveData observer on change of any property in object
            value = value

        }
    }


    override fun setValue(value: T?) {
        super.setValue(value)

        //listen to property changes
        value!!.addOnPropertyChangedCallback(callback)
    }


}