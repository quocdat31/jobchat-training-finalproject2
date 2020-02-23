package com.example.finalproject2.di

import com.example.finalproject2.ui.signup.SignUpPresenter
import com.example.finalproject2.ui.signup.SignUpContract
import com.example.finalproject2.firebase.authentication.FirebaseAuthInterface
import com.example.finalproject2.firebase.database.FirebaseDatabaseInterface
import com.example.finalproject2.ui.signin.SignInContract
import com.example.finalproject2.ui.signin.SignInPresenter
import dagger.Module
import dagger.Provides

@Module(includes = [InteractionModule::class])
class PresenterModule {

    @Provides
    fun providePresenter(
        firebaseAuthInterface: FirebaseAuthInterface,
        firebaseDatabaseInterface: FirebaseDatabaseInterface
    ): SignUpContract.Presenter {
        return SignUpPresenter(firebaseAuthInterface, firebaseDatabaseInterface)
    }

    @Provides
    fun provideLoginPresenter(firebaseAuthInterface: FirebaseAuthInterface): SignInContract.Presenter {
        return SignInPresenter(firebaseAuthInterface)
    }

    @Provides
    fun provideHomePresenter(): HomeContract.Presenter {
        return MainPresenter()
    }

    @Provides
    fun providerContactPresenter(
        firebaseAuthInterface: FirebaseAuthInterface,
        firebaseDatabaseInterface: FirebaseDatabaseInterface
    ): ContactContract.Presenter {
        return ContactPresenter(firebaseDatabaseInterface, firebaseAuthInterface)
    }

    @Provides
    fun providerPersonalPresenter(firebaseAuthInterface: FirebaseAuthInterface): PersonalContract.Presenter {
        return PersonalPresenter(firebaseAuthInterface)
    }

}
