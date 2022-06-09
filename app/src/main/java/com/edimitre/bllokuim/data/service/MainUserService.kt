package com.edimitre.bllokuim.data.service


import androidx.lifecycle.LiveData
import com.edimitre.bllokuim.data.dao.MainUserDao
import com.edimitre.bllokuim.data.model.MainUser
import javax.inject.Inject

class MainUserService @Inject constructor(private val userDao: MainUserDao) {


    suspend fun saveUser(user: MainUser) {

        userDao.save(user)

    }

    fun getUser(): LiveData<MainUser?> {

        return userDao.getMainUser()
    }

}