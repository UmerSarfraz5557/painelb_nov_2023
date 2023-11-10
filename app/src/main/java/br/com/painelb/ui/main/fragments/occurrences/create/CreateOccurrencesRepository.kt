package br.com.painelb.ui.main.fragments.occurrences.create

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import br.com.painelb.api.ApiResponse
import br.com.painelb.api.ApiService
import br.com.painelb.db.dao.occurrence.OccurrenceDao
import br.com.painelb.db.dao.occurrence.OccurrencePhotoDao
import br.com.painelb.db.dao.occurrence.OccurrenceTypeDao
import br.com.painelb.db.dao.occurrence.VehicleTypeDao
import br.com.painelb.db.dao.relational.OccurrenceRelation
import br.com.painelb.db.table.occurrence.OccurrenceEntity
import br.com.painelb.db.table.occurrence.OccurrencePhoto
import br.com.painelb.db.table.occurrence.OccurrenceType
import br.com.painelb.db.table.occurrence.VehicleType
import br.com.painelb.model.ItemType
import br.com.painelb.model.Response
import br.com.painelb.model.occurrences.CreateOccurrence
import br.com.painelb.network.NetworkBoundResource
import br.com.painelb.network.NetworkBoundResourceOnlyDatabase
import br.com.painelb.network.NetworkBoundResourceOnlyNetwork
import br.com.painelb.network.Resource
import br.com.painelb.util.AppExecutors
import com.squareup.moshi.Moshi
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*
import javax.inject.Inject

class CreateOccurrencesRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val apiService: ApiService,
    private val occurrenceTypeDao: OccurrenceTypeDao,
    private val vehicleTypeDao: VehicleTypeDao,
    private val occurrenceDao: OccurrenceDao,
    private val occurrencePhotoDao: OccurrencePhotoDao,
    private val moshi: Moshi
) {

    fun createOccurrence(
        data: CreateOccurrence, itemType: ItemType
    ): LiveData<Resource<Response>> {
        return object :
            NetworkBoundResourceOnlyNetwork<Response, Response>(
                appExecutors
            ) {

            override fun save(response: Resource<Response>) {
                if (itemType == ItemType.LOCAL) {
                    val id = data.occurrence.occurrenceId
                    occurrenceDao.deleteById(id)
                    occurrencePhotoDao.deleteByOccurrenceId(id)
                }
            }

            override fun createCall(): LiveData<ApiResponse<Response>> {
                val adapter = moshi.adapter(CreateOccurrence::class.java)
                val type = MultipartBody.FORM
                val occurrenceId = data.occurrence.occurrenceId
                val idString = occurrenceId.toString()
                val idBody = RequestBody.create(type, idString)
                val occurrence = adapter.toJson(data)
                val occurrenceBody = RequestBody.create(type, occurrence)
                val files = prepareFilePart(data.occurrencePhotos)
                return if (itemType == ItemType.REMOTE) {
                    apiService.updateOccurrence(occurrenceId,idBody, occurrenceBody, files)
                } else {
                    apiService.createOccurrence(idBody, occurrenceBody, files)
                }
            }
        }.asLiveData()
    }

    fun occurrenceType(): LiveData<Resource<List<OccurrenceType>>> {
        return object :
            NetworkBoundResource<List<OccurrenceType>, List<OccurrenceType>>(appExecutors) {
            override fun saveCallResult(item: List<OccurrenceType>) {
                occurrenceTypeDao.insert(item)
            }

            override fun shouldFetch(data: List<OccurrenceType>?) = true

            override fun loadFromDb() = occurrenceTypeDao.getOccurrenceTypeLiveData()

            override fun createCall() = apiService.typeOccurrence()
        }.asLiveData()
    }


    fun vehicleType(): LiveData<Resource<List<VehicleType>>> {
        return object :
            NetworkBoundResource<List<VehicleType>, List<VehicleType>>(appExecutors) {
            override fun saveCallResult(item: List<VehicleType>) {
                Log.d("Vehicle","Success "+item.size)
                vehicleTypeDao.insert(item)
            }

            override fun shouldFetch(data: List<VehicleType>?) = true

            override fun loadFromDb() = vehicleTypeDao.getVehicleTypeLiveData()

            override fun createCall() = apiService.typeVehicle()
        }.asLiveData()
    }

    fun occurrenceSave(createOccurrence: CreateOccurrence, itemType: ItemType) {
        val occurrenceEntity = OccurrenceEntity(
            id = createOccurrence.occurrence.occurrenceId,
            occurrence = createOccurrence.occurrence,
            occurreceWitness = createOccurrence.occurreceWitness,
            occurrenceVictim = createOccurrence.occurrenceVictim,
            vehicleConductor = createOccurrence.vehicleConductor
        )
        val value = createOccurrence.occurrencePhotos
        if (itemType == ItemType.LOCAL) {
            occurrenceDao.update(occurrenceEntity)
            if (!value.isNullOrEmpty()) {
                occurrencePhotoDao.deleteByOccurrenceId(value[0].occurrenceId)
                occurrencePhotoDao.insert(value.toList())
            }
        } else {
            occurrenceDao.insert(occurrenceEntity)
            if (!value.isNullOrEmpty()) occurrencePhotoDao.insert(value.toList())
        }
    }

    fun getOccurrenceFromDatabase(id: Long): LiveData<Resource<OccurrenceRelation>> {
        return object :
            NetworkBoundResourceOnlyDatabase<OccurrenceRelation>(appExecutors) {
            override fun loadFromDb(): LiveData<OccurrenceRelation> {
                return occurrenceDao.getOccurrenceRelationDistinctLiveData(id)
            }
        }.asLiveData()
    }

    fun getOccurrenceById(id: Long): LiveData<Resource<CreateOccurrence>> {
        return object :
            NetworkBoundResourceOnlyNetwork<CreateOccurrence, CreateOccurrence>(appExecutors) {
            override fun createCall(): LiveData<ApiResponse<CreateOccurrence>> =
                apiService.getOccurrenceById(id)
        }.asLiveData()
    }


    fun prepareFilePart(occurrencePhotos: List<OccurrencePhoto>): List<MultipartBody.Part> {
        val parts: ArrayList<MultipartBody.Part> = ArrayList()
        occurrencePhotos.forEach {
            if (!Patterns.WEB_URL.matcher(it.photo).matches()) {
                val file = File(it.photo)
                val parse = "multipart/form-data".toMediaTypeOrNull()
                val requestFile = RequestBody.create(parse, file)
                parts.add(MultipartBody.Part.createFormData("photos", it.name, requestFile))
            }
        }
        return parts
    }
}
