package com.andriybobchuk.cocreate.di

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.andriybobchuk.cocreate.core.data.repository.CoreRepository
import com.andriybobchuk.cocreate.core.data.repository.CoreRepositoryImpl
import com.andriybobchuk.cocreate.feature.auth.data.repository.AuthRepository
import com.andriybobchuk.cocreate.feature.auth.data.repository.AuthRepositoryImpl
import com.andriybobchuk.cocreate.feature.profile.data.repository.ProfileRepository
import com.andriybobchuk.cocreate.feature.profile.data.repository.ProfileRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/**
 * The heart of the CoCreate app!
 *
 * This magical object initializes Dagger Hilt for the app module, providing it with all the
 * dependencies it needs to run smoothly. It's like a spark that ignites a chain reaction of
 * awesomeness throughout the app.
 *
 * The AppModule object is the coolest kid on the block, providing all sorts of goodies for
 * the app to use. It's like a treasure chest of app-level dependencies that are accessible
 * from anywhere in the app.
 */
@Module
@InstallIn(SingletonComponent::class) // this object lives as long as Application does
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuthObject() = FirebaseAuth.getInstance() // We can now use this inside of our repository

    @Provides
    @Singleton
    fun provideRepositoryImpl(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore,
        coreRepository: CoreRepository
    ):AuthRepository {
        return AuthRepositoryImpl(
            firebaseAuth = firebaseAuth,
            firebaseFirestore = firebaseFirestore,
            coreRepository = coreRepository
        )
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestoreObject() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideProfileRepositoryImpl(
        firebaseFirestore: FirebaseFirestore,
        coreRepository: CoreRepository
    ):ProfileRepository {
        return ProfileRepositoryImpl(
            firebaseFirestore = firebaseFirestore,
            coreRepository = coreRepository
        )
    }

    @Provides
    @Singleton
    fun provideCoreRepositoryImpl(
        firebaseAuth: FirebaseAuth
    ): CoreRepository {
        return CoreRepositoryImpl(
            firebaseAuth = firebaseAuth
        )
    }


}