package com

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Utility functions/function extensions used in testing
 */


/**
 * This function attaches an observer to a LiveData object and blocks the thread waiting for an update from the LiveData for at most 2
 * seconds. It either returns the data it got from the LiveData object or throws an exception if it takes longer than 2 seconds to get.
 */
fun<T> LiveData<T>.getOrAwaitValue() : T
{
    var data: T? = null
    val latch = CountDownLatch(1)

    val observer = object : Observer<T>
    {
        override fun onChanged(t: T) {
            data = t
            this@getOrAwaitValue.removeObserver(this)
            latch.countDown()
        }
    }
    this.observeForever(observer)

    try {
        //Wait at most 2 seconds for livedata to change
        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw TimeoutException("Live Data doesn't get updated")
        }
    } finally{
        this.removeObserver(observer)
    }

    return data as T
}