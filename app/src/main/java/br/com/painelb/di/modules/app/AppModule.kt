package br.com.painelb.di.modules.app

import android.content.Context
import androidx.room.Room
import br.com.painelb.PainelbApplication
import br.com.painelb.base.navigation.ActivityScreenSwitcher
import br.com.painelb.db.PainelbDatabase
import br.com.painelb.db.dao.checklist.CheckListDao
import br.com.painelb.db.dao.checklist.CheckListPhotoDao
import br.com.painelb.db.dao.occurrence.OccurrenceDao
import br.com.painelb.db.dao.occurrence.OccurrencePhotoDao
import br.com.painelb.db.dao.occurrence.OccurrenceTypeDao
import br.com.painelb.db.dao.occurrence.VehicleTypeDao
import br.com.painelb.prefs.PreferenceStorage
import br.com.painelb.prefs.SharedPreferenceStorage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideApplicationContext(mApplication: PainelbApplication): Context =
        mApplication.applicationContext

    @Singleton
    @Provides
    fun provideActivityScreenSwitcher() = ActivityScreenSwitcher()

    @Singleton
    @Provides
    fun providesPreferenceStorage(context: Context): PreferenceStorage =
        SharedPreferenceStorage(context)

    @Singleton
    @Provides
    fun provideDatabase(app: PainelbApplication): PainelbDatabase {
        return Room
            .databaseBuilder(app, PainelbDatabase::class.java, "painelb_database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideOccurrenceTypeDao(db: PainelbDatabase): OccurrenceTypeDao = db.occurrenceTypeDao()

    @Singleton
    @Provides
    fun provideVehicleTypeDao(db: PainelbDatabase): VehicleTypeDao = db.vehicleTypeDao()

    @Singleton
    @Provides
    fun provideOccurrenceDao(db: PainelbDatabase): OccurrenceDao = db.occurrenceDao()

    @Singleton
    @Provides
    fun provideOccurrencePhotoDao(db: PainelbDatabase): OccurrencePhotoDao = db.occurrencePhotoDao()

    @Singleton
    @Provides
    fun provideCheckListDao(db: PainelbDatabase): CheckListDao = db.checkListDao()

    @Singleton
    @Provides
    fun provideCheckListPhotoDao(db: PainelbDatabase): CheckListPhotoDao = db.checkListPhotoDao()
}
