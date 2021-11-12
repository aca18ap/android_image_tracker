package com.com4510.team01.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.com4510.team01.model.data.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TravelViewModel (application: Application) : AndroidViewModel(application) {
    private var mRepository: Repository = Repository(application)

    //Observable data (LiveData)
    //private var numberDataToDisplay: LiveData<NumberData?>?

    //Initialize observable data
    /*
    init {
        this.numberDataToDisplay = this.mRepository.getNumberData()
    }
    */


    /**
     * getter for the live data
     * @return
     */
    /*
    fun getNumberDataToDisplay(): LiveData<NumberData?>? {
        if (this.numberDataToDisplay == null) {
            this.numberDataToDisplay = MutableLiveData<NumberData>()
        }
        return this.numberDataToDisplay
    }
    */

    /**
     * Code that should be called by the ui that has some logic + references database. But the logic isn't necessarily
     * applied on a model object
     */
    /*
    fun generateNewNumber() {
        viewModelScope.launch(Dispatchers.IO) { mRepository.generateNewNumber() }
    }
     */
}