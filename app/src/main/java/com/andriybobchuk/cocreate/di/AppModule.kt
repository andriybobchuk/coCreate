package com.andriybobchuk.cocreate.di

import com.andriybobchuk.cocreate.feature.auth.data.repository.AuthRepository
import com.andriybobchuk.cocreate.feature.auth.data.repository.AuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
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
    fun provideRepositoryImpl(firebaseAuth: FirebaseAuth):AuthRepository {
        return AuthRepositoryImpl(firebaseAuth = firebaseAuth)
    }
}