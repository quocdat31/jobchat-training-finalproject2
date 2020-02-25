package com.example.finalproject2.di

import com.example.finalproject2.firebase.authentication.FirebaseAuthImpl
import com.example.finalproject2.firebase.authentication.FirebaseAuthInterface
import com.example.finalproject2.firebase.database.FirebaseDatabaseImp
import com.example.finalproject2.firebase.database.FirebaseDatabaseInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [FirebaseModule::class])
class InteractionModule {
    @Provides
    @Singleton
    fun authentication(firebaseAuth: FirebaseAuth): FirebaseAuthInterface {
        return FirebaseAuthImpl(firebaseAuth)
    }
    @Provides
    @Singleton
    fun database(firebaseDatabase: FirebaseDatabase): FirebaseDatabaseInterface {
        return FirebaseDatabaseImp(firebaseDatabase)
    }
}
