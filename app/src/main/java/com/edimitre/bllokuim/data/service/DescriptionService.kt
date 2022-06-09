package com.edimitre.bllokuim.data.service


import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.edimitre.bllokuim.data.dao.DescriptionDao
import com.edimitre.bllokuim.data.model.Description
import javax.inject.Inject

class DescriptionService @Inject constructor(private val descriptionDao: DescriptionDao) {


    var allDescriptionsList = descriptionDao.getAll().asLiveData()

    suspend fun saveDescription(description: Description) {

        descriptionDao.save(description)

    }

    suspend fun deleteDescription(description: Description) {

        descriptionDao.delete(description)

    }

    suspend fun getAllDescriptionsList(): List<Description> {

        return descriptionDao.getAllList()
    }


    fun getAllDescriptions(): LiveData<List<Description>> {


        return allDescriptionsList


    }

}