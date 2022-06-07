package com.edimitre.bllokuim.data.service


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.edimitre.bllokuim.data.dao.DescriptionDao
import com.edimitre.bllokuim.data.model.Description
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DescriptionService @Inject constructor(private val descriptionDao: DescriptionDao) {

    val TAG = "BllokuIm => "

    var allDescriptionsList = descriptionDao.getAll().asLiveData()

    suspend fun saveDescription(description: Description) {

        descriptionDao.save(description)

        Log.e(TAG, "description => " + description.name + " u ruajt me sukses")
    }

    suspend fun getAllDescriptionsList():List<Description>{

        return descriptionDao.getAllList()
    }


    fun getAllDescriptions(): LiveData<List<Description>> {


        return allDescriptionsList


    }

}