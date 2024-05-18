package com.example.admin

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.Module
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class HiltModule {


    @Provides
    fun provideFirestore(): FirebaseDatabase = Firebase.database

    @Provides
    fun provideStorage(): FirebaseStorage = Firebase.storage
}