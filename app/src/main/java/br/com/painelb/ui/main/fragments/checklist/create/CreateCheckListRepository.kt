package br.com.painelb.ui.main.fragments.checklist.create

import android.util.Patterns
import androidx.lifecycle.LiveData
import br.com.painelb.api.ApiResponse
import br.com.painelb.api.ApiService
import br.com.painelb.db.dao.checklist.CheckListDao
import br.com.painelb.db.dao.checklist.CheckListPhotoDao
import br.com.painelb.db.dao.relational.CheckListRelation
import br.com.painelb.db.table.checklist.ChecklistEntity
import br.com.painelb.db.table.checklist.ChecklistPhotos
import br.com.painelb.model.ItemType
import br.com.painelb.model.Response
import br.com.painelb.model.checklist.CreateCheckList
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
import javax.inject.Singleton

@Singleton
class CreateCheckListRepository @Inject constructor(
    val appExecutors: AppExecutors,
    val apiService: ApiService,
    private val checkListDao: CheckListDao,
    private val checkListPhotoDao: CheckListPhotoDao,
    private val moshi: Moshi
) {
    fun createCheckList(data: CreateCheckList, itemType: ItemType): LiveData<Resource<Response>> {
        return object :
            NetworkBoundResourceOnlyNetwork<Response, Any?>(appExecutors) {
            override fun save(response: Resource<Response>) {
                if (itemType == ItemType.LOCAL) {
                    val id = data.checklist.checklistId
                    checkListDao.deleteById(id)
                    checkListPhotoDao.deleteByChecklistId(id)
                }
            }

            override fun createCall(): LiveData<ApiResponse<Response>> {
                val adapter = moshi.adapter(CreateCheckList::class.java)
                val type = MultipartBody.FORM
                val checklistId = data.checklist.checklistId
                val id = checklistId.toString()
                val idBody = RequestBody.create(type, id)
                val checkList = adapter.toJson(data)
                val checkListBody = RequestBody.create(type, checkList)
                val files = prepareFilePart(data.checklistPhotos)
                return if (itemType == ItemType.REMOTE) {
                    apiService.updateChecklist(checklistId,idBody, checkListBody, files)
                } else {
                    apiService.createCheckList(idBody, checkListBody, files)
                }
            }

        }.asLiveData()
    }

    fun save(createChecklist: CreateCheckList, itemType: ItemType) {
        val checklist = createChecklist.checklist
        val checklistEntity = ChecklistEntity(
            id = checklist.checklistId,
            checklist = checklist
        )
        val value = createChecklist.checklistPhotos
        if (itemType == ItemType.LOCAL) {
            checkListDao.update(checklistEntity)
            if (!value.isNullOrEmpty()) {
                checkListPhotoDao.deleteByChecklistId(value[0].checklistId)
                checkListPhotoDao.insert(value.toList())
            }
        } else {
            checkListDao.insert(checklistEntity)
            if (!value.isNullOrEmpty()) checkListPhotoDao.insert(value.toList())
        }
    }

    fun getChecklistFromDatabase(id: Long): LiveData<Resource<CheckListRelation>> {
        return object :
            NetworkBoundResourceOnlyDatabase<CheckListRelation>(appExecutors) {
            override fun loadFromDb(): LiveData<CheckListRelation> {
                return checkListDao.getChecklistRelationDistinctLiveData(id)
            }
        }.asLiveData()
    }

    fun getChecklistById(id: Long): LiveData<Resource<CreateCheckList>> {
        return object :
            NetworkBoundResourceOnlyNetwork<CreateCheckList, CreateCheckList>(appExecutors) {
            override fun createCall(): LiveData<ApiResponse<CreateCheckList>> =
                apiService.getChecklistById(id)
        }.asLiveData()
    }


    fun prepareFilePart(occurrencePhotos: List<ChecklistPhotos>): List<MultipartBody.Part> {
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
