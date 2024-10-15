package com.eakurnikov.trustore.di

import com.eakurnikov.trustore.domain.backup.BackupManager
import com.eakurnikov.trustore.domain.backup.BackupManagerImpl
import com.eakurnikov.trustore.domain.backup.BackupStorage
import com.eakurnikov.trustore.domain.backup.BackupStorageImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface BackupModule {

    @Singleton
    @Binds
    fun bindBackupManager(impl: BackupManagerImpl): BackupManager

    @Singleton
    @Binds
    fun bindBackupStorage(impl: BackupStorageImpl): BackupStorage
}
