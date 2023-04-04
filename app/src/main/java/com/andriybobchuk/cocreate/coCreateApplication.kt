package com.andriybobchuk.cocreate

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * The Queen/King of the CoCreate app!
 *
 * This royal class extends the Android Application class and is responsible for initializing
 * the Dagger Hilt dependency injection framework for the entire app. It's like the ruler of
 * the app, ensuring that everything runs smoothly and that all dependencies are in order.
 *
 * The CoCreateApplication class also provides a base implementation for the application object
 * and can be used to configure other app-wide dependencies. It's like the foundation of the
 * app, providing a solid base for everything else to build upon.
 */
@HiltAndroidApp
class coCreateApplication : Application() {



}