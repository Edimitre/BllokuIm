package com.edimitre.bllokuim.data.service


import android.util.Log
import androidx.lifecycle.LiveData
import com.edimitre.bllokuim.data.dao.MainUserDao
import com.edimitre.bllokuim.data.model.MainUser
import javax.inject.Inject

class MainUserService @Inject constructor(private val userDao: MainUserDao) {

    val TAG = "BllokuIm => "


    suspend fun saveUser(user: MainUser) {

        userDao.save(user)

        Log.e(TAG, "user => " + user.name + " u ruajt me sukses")
    }

    fun getUser(): LiveData<MainUser?> {

        return userDao.getMainUser()
    }

}