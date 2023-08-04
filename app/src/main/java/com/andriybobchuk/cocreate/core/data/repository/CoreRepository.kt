package com.andriybobchuk.cocreate.core.data.repository

import com.google.firebase.auth.FirebaseAuth

interface CoreRepository {

    fun getCurrentUserID(): String

}