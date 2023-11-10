package br.com.painelb.ui.main.fragments.occurrences.create

import android.Manifest
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.*
import androidx.lifecycle.map
import br.com.painelb.R
import br.com.painelb.db.dao.relational.OccurrenceRelation
import br.com.painelb.db.table.occurrence.OccurrencePhoto
import br.com.painelb.domain.result.Event
import br.com.painelb.model.ItemType
import br.com.painelb.model.Response
import br.com.painelb.model.occurrences.*
import br.com.painelb.network.Resource
import br.com.painelb.network.Status
import br.com.painelb.prefs.PreferenceStorage
import br.com.painelb.util.*
import timber.log.Timber
import java.text.SimpleDateFormat
import javax.inject.Inject
import kotlin.collections.map


class CreateOccurrenceViewModel @Inject constructor(
    private val context: Context,
    private val preferenceStorage: PreferenceStorage,
    private val repository: CreateOccurrencesRepository,
    private val appExecutors: AppExecutors
) : ViewModel() {

    val itemType = MutableLiveData(ItemType.NEW)
    private val occurrenceType = repository.occurrenceType()
     var activityContext : Context? = null

    val occurrenceTypeData = occurrenceType.map {
        if (it.data != null) it.data!!.map { item -> item.name }.toTypedArray()
        else arrayOf()
    }

    val successMessageEvent = MutableLiveData<Event<String>>()
    val messageEvent = MutableLiveData<Event<String>>()
    val navigateToAddVehicleAndConductor = MutableLiveData<Event<Long>>()
    val navigateToAddVictim = MutableLiveData<Event<Long>>()
    val navigateToAddWitness = MutableLiveData<Event<Long>>()
    val navigateFileScreen = MutableLiveData<Event<Unit>>()
    val storageReadPermissionEvent = MutableLiveData<Event<Unit>>()

    val currentQuizPosition = MutableLiveData<Int>()
    val pagePositionChangeListener = MutableLiveData<Event<Int>>()
    val totalPageCount = MutableLiveData<Int>()

    var occurrenceId = System.currentTimeMillis()

    var typeOfOccurrence = MutableLiveData<String>()
    var errorTypeOfOccurrence = MutableLiveData<String>()

    var address = MutableLiveData<String>()
    var errorAddress = MutableLiveData<String>()

    var perimeter = MutableLiveData<String>()
    var errorPerimeter = MutableLiveData<String>()

    var date = MutableLiveData<String>()
    var errorDate = MutableLiveData<String>()

    var time = MutableLiveData<String>()
    var errorTime = MutableLiveData<String>()

    var descriptions = MutableLiveData<String>()
    var errorDescriptions = MutableLiveData<String>()

    val vehicleConductors = MutableLiveData<ArrayList<VehicleConductor>>(ArrayList())
    val editVehicleConductors = MutableLiveData<Event<Pair<Long, VehicleConductor>>>()

    private var tempVehicleConductors : ArrayList<OccurrenceVictim>? = ArrayList()


    val hasVictim = MutableLiveData(false)
    private fun hasVictimObserver(): Observer<Boolean> {
        return Observer {
            Timber.d("The check box is %s", it)
            if (!it) {
                if(victims.value != null && victims.value!!.size > 0){
                      hasVictim.value = true
                      run{
                          activityContext?.showDialog(cancelable = false, cancelableTouchOutside = false) {
                              setMessage(context.getString(R.string.victim_remove_message))
                              positiveButton(context.getString(R.string.ok))
                          }
                      }

                }
            }
        }
    }

    val victims = MutableLiveData<ArrayList<OccurrenceVictim>>(ArrayList())
    val editVictim = MutableLiveData<Event<Pair<Long, OccurrenceVictim>>>()

    val hasWitness = MutableLiveData(false)
    private fun hasWitnessObserver(): Observer<Boolean> {
        return Observer {
            if (!it) witness.value?.clear()
        }
    }

    val witness = MutableLiveData<ArrayList<OccurreceWitnes>>(ArrayList())
    val editWitness = MutableLiveData<Event<Pair<Long, OccurreceWitnes>>>()

    val photos = MutableLiveData<ArrayList<OccurrencePhoto>>(ArrayList())

    val photoLoadingStatus = MutableLiveData<Resource<String>>()

    val loadingStatus = MutableLiveData<Resource<String>>()

    private val occurrenceData = MutableLiveData<CreateOccurrence>()

    val occurrenceSaveStatus = MutableLiveData<Resource<String>>()
    val occurrenceSaveMessage = MutableLiveData<Event<String>>()

    private val occurrenceDataResponse = occurrenceData.switchMap {

        for ((index,victim) in it.occurrenceVictim.withIndex()){
            it.occurrenceVictim[index].birthDate = dbFormat(victim.birthDate)
        }

        repository.createOccurrence(it, itemType.value!!)
    }

    private fun occurrenceResponseObserver(): Observer<Resource<Response>> {
        return Observer {
            Log.d("Remote"," ${it.status}  ${it.message}")
            loadingStatus.value = Resource(it.status, null, null)
            when {
                Status.SUCCESS == it.status -> successMessageEvent.value =
                    Event(it.data?.message ?: "")
                Status.ERROR == it.status -> messageEvent.value = Event(it.message ?: "")
            }
        }
    }

    private val occurrenceDatabaseResult = MediatorLiveData<Resource<OccurrenceRelation>>()

    private fun occurrenceDatabaseResultObserver(): Observer<Resource<OccurrenceRelation>> {
        return Observer {
            loadingStatus.value = Resource(it.status, null, null)
            if (Status.SUCCESS == it.status) {
                val data = it.data
                if (data != null) {
                    val occurrence = data.occurrence.occurrence
                    occurrenceId = occurrence.occurrenceId
                    address.value = occurrence.address
                    date.value = occurrence.date
                    descriptions.value = occurrence.description
                    hasVictim.value = occurrence.hasVictim
                    hasWitness.value = occurrence.hasWitness
                    typeOfOccurrence.value = occurrence.occurrenceType
                    perimeter.value = occurrence.perimeter
                    time.value = occurrence.time
                    vehicleConductors.value = ArrayList(data.occurrence.vehicleConductor)
                    victims.value = ArrayList(data.occurrence.occurrenceVictim)
                    updateDriverCheckBoxes()
                    witness.value = ArrayList(data.occurrence.occurreceWitness)
                    photos.value = ArrayList(data.photos)
                }
            } else if (Status.ERROR == it.status) {
                successMessageEvent.value =
                    Event(it?.message ?: context.getString(R.string.error_message_unknown))
            }
        }
    }

    private fun updateDriverCheckBoxes(){
        Timber.d("Updating CheckBoxes")
        if(vehicleConductors.value != null && victims.value != null){
            val value = vehicleConductors.value
            value?.forEach { data ->
                data.isCheck = false
                for(victim in victims.value!!){
                if(victim.name == data.driverName && victim.documentNumber == data.driverDocumentNumber){
                    data.isCheck = true
                    break;
                }
            }
            }
            vehicleConductors.value = value
            if(victims.value!!.size ==0){
                hasVictim.value = false

            }
        }

    }

    private val occurrenceRemoteResult = MediatorLiveData<Resource<CreateOccurrence>>()

    private fun occurrenceRemoteResultObserver(): Observer<Resource<CreateOccurrence>> {
        return Observer {

            loadingStatus.value = Resource(it.status, null, null)
            if (Status.SUCCESS == it.status) {
                Log.d("Datata","loaded")
                val data = it.data
                if (data != null) {
                    val occurrence = data.occurrence
                    occurrenceId = occurrence.occurrenceId
                    address.value = occurrence.address
                    date.value = occurrence.date
                    descriptions.value = occurrence.description
                    hasVictim.value = occurrence.hasVictim
                    hasWitness.value = occurrence.hasWitness
                    typeOfOccurrence.value = occurrence.occurrenceType
                    perimeter.value = occurrence.perimeter
                    time.value = occurrence.time
                    vehicleConductors.value = ArrayList(data.vehicleConductor)
                    victims.value = ArrayList(data.occurrenceVictim)
                    updateDriverCheckBoxes()
                    witness.value = ArrayList(data.occurreceWitness)
                    photos.value = ArrayList(data.occurrencePhotos)
                }
            } else if (Status.ERROR == it.status) {
                successMessageEvent.value =
                    Event(it?.message ?: context.getString(R.string.error_message_unknown))
            }
        }
    }

    init {
        resetValue()
        typeOfOccurrence.observeForever {
            if (!errorTypeOfOccurrence.value.isNullOrBlank()) errorTypeOfOccurrence.value =
                String.EMPTY
        }
        address.observeForever {
            if (!errorAddress.value.isNullOrBlank()) errorAddress.value = String.EMPTY
        }

        perimeter.observeForever {
            if (!errorPerimeter.value.isNullOrBlank()) errorPerimeter.value =
                String.EMPTY
        }

        date.observeForever {
            if (!errorDate.value.isNullOrBlank()) errorDate.value = String.EMPTY
        }
        time.observeForever {
            if (!errorTime.value.isNullOrBlank()) errorTime.value = String.EMPTY
        }

        descriptions.observeForever {
            if (!errorDescriptions.value.isNullOrBlank()) errorDescriptions.value = String.EMPTY
        }

        hasVictim.observeForever(hasVictimObserver())
        hasWitness.observeForever(hasWitnessObserver())
        occurrenceDataResponse.observeForever(occurrenceResponseObserver())
        occurrenceDatabaseResult.observeForever(occurrenceDatabaseResultObserver())
        occurrenceRemoteResult.observeForever(occurrenceRemoteResultObserver())
    }


    fun updateFormId(id: Long, itemType: ItemType) {
        Timber.d("updateFormId : $id   $itemType")
        this.itemType.value = itemType
        if (itemType == ItemType.LOCAL) setDataFromLocal(id)
        else if (itemType == ItemType.REMOTE) setDataFromServer(id)
    }

    private fun resetValue() {
        typeOfOccurrence.value = String.EMPTY
        errorTypeOfOccurrence.value = String.EMPTY
        address.value = String.EMPTY
        errorAddress.value = String.EMPTY
        perimeter.value = String.EMPTY
        errorPerimeter.value = String.EMPTY
        date.value = String.EMPTY
        errorDate.value = String.EMPTY
        time.value = String.EMPTY
        errorTime.value = String.EMPTY
        descriptions.value = String.EMPTY
        errorDescriptions.value = String.EMPTY
        photos.value = arrayListOf()
        vehicleConductors.value?.clear()
        victims.value?.clear()
        witness.value?.clear()
        photos.value?.clear()
    }

    private fun setDataFromLocal(id: Long) {
        val source = repository.getOccurrenceFromDatabase(id)
        occurrenceDatabaseResult.removeSource(source)
        occurrenceDatabaseResult.addSource(source) {
            occurrenceDatabaseResult.value = it
            if (it.status == Status.ERROR || it.status == Status.SUCCESS)
                occurrenceDatabaseResult.removeSource(source)
        }
    }

    private fun setDataFromServer(id: Long) {
        val source = repository.getOccurrenceById(id)
        occurrenceRemoteResult.removeSource(source)
        occurrenceRemoteResult.addSource(source) {
            occurrenceRemoteResult.value = it
            if (it.status == Status.ERROR || it.status == Status.SUCCESS)
                occurrenceRemoteResult.removeSource(source)
        }
    }

    fun onAddVehicleConductor(data: VehicleConductor) {
        val value = vehicleConductors.value!!
        value.add(data)
        vehicleConductors.value = value
    }

    fun onUpdateVehicleConductor(data: VehicleConductor) {
        val value = vehicleConductors.value
        val index = value?.indexOfFirst { it.vehicleId == data.vehicleId }
        if (index != null && index >= 0) value[index] = data
        vehicleConductors.value = value
    }

    fun onRemoveVehicleConductor(position: Int) {
        val value = vehicleConductors.value!!
        value.removeAt(position)
        vehicleConductors.value = value
    }

    fun onEditVehicleConductor(position: Int) {
        val value = vehicleConductors.value!!
        editVehicleConductors.value = Event(Pair(occurrenceId, value[position]))
    }

    fun onAddVictim(data: OccurrenceVictim) {
        val value = victims.value!!
        value.add(data)
        victims.value = value
    }

     fun showDeleteConfirmationDialog(position: Int) {
         if(activityContext != null){
             val builder = AlertDialog.Builder(activityContext!!).apply {
                 setMessage(R.string.delete_vehicle)
                 setPositiveButton(R.string.yes) { _, _ -> this@CreateOccurrenceViewModel.onRemoveVehicleConductor(position) }
                 setNegativeButton(R.string.no) { _, _ -> }
             }
             builder.create().show()
         }

    }

    fun onUpdateVictim(data: OccurrenceVictim) {
        val value = victims.value
        val index = value?.indexOfFirst { it.victimId == data.victimId }
        if (index != null && index >= 0) value[index] = data
        else value?.add(data)
        victims.value = value
    }

    fun onRemoveVictim(position: Int) {
        val value = victims.value!!
        value.removeAt(position)
        victims.value = value
        updateDriverCheckBoxes()
    }

    fun onEditVictim(position: Int) {
        val value = victims.value!!
        editVictim.value = Event(Pair(occurrenceId, value[position]))
    }

    fun onVictimDriverSelected(isChecked: Boolean, position: Int, item: VehicleConductor,onComplete : ((shouldBeChecked : Boolean)-> Unit)) {
        if (checkIfThisDriverIsRegisteredAsVictim(item)){
            onComplete(true)
            run{
                activityContext?.showDialog(cancelable = false, cancelableTouchOutside = false) {
                    setMessage(context.getString(R.string.victim_remove_message))
                    positiveButton(context.getString(R.string.ok))
                }
            }
        }else{
            item.isCheck = isChecked
            val items = vehicleConductors!!.value!!
            items[position] = item
            vehicleConductors.value = items
            onComplete(false)

            if (isChecked) {
                val occurrenceVictim = OccurrenceVictim(
                    victimId = System.currentTimeMillis(),
                    occurrenceId = occurrenceId,
                    address = "",
                    documentNumber = item.driverDocumentNumber,
                    documentType = "",
                    genre = "",
                    name = item.driverName,
                    statusVictim = "",
                    birthDate = ""
                )
                editVictim.value = Event(Pair(occurrenceId, occurrenceVictim))
            }
        }

    }


    fun checkIfThisDriverIsRegisteredAsVictim(data: VehicleConductor): Boolean{
        if(victims.value != null && victims.value!!.size > 0){
            for(victim in victims.value!!){
                if(victim.name == data.driverName && victim.documentNumber == data.driverDocumentNumber){
                   return true;
                }
            }
        }

        return false;
    }


    fun onAddWitness(data: OccurreceWitnes) {
        val value = witness.value!!
        value.add(data)
        witness.value = value
    }

    fun onUpdateWitness(data: OccurreceWitnes) {
        val value = witness.value
        val index = value?.indexOfFirst { it.witnessId == data.witnessId }
        if (index != null && index >= 0) value[index] = data
        witness.value = value
    }

    fun onRemoveWitness(position: Int) {
        val value = witness.value!!
        value.removeAt(position)
        witness.value = value
    }

    fun onEditWitness(position: Int) {
        val value = witness.value!!
        editWitness.value = Event(Pair(occurrenceId, value[position]))
    }


    fun typeOfOccurrenceItemSelected(item: Any) {
        (item as String).apply { typeOfOccurrence.value = item }
    }

    fun perimeterItemSelected(item: Any) {
        (item as String).apply { perimeter.value = item }
    }

    fun validateOccurrenceInformationData() {
        when {
            typeOfOccurrence.value.isNullOrEmpty() -> errorTypeOfOccurrence.value =
                context.getString(R.string.enter_type_of_occurrence)
            address.value.isNullOrEmpty() -> errorAddress.value =
                context.getString(R.string.enter_address)
            perimeter.value.isNullOrEmpty() -> errorPerimeter.value =
                context.getString(R.string.enter_perimeter)
            date.value.isNullOrEmpty() -> errorDate.value =
                context.getString(R.string.enter_date)
            time.value.isNullOrEmpty() -> errorTime.value =
                context.getString(R.string.enter_time)
            descriptions.value.isNullOrEmpty() -> errorDescriptions.value =
                context.getString(R.string.enter_descriptions)

            else -> onNextClick()
        }
    }

    fun validateImageData() {
        when {
            photos.value.isNullOrEmpty() -> messageEvent.value =
                Event(context.getString(R.string.please_choose_image))
            else -> onNextClick()
        }
    }

    fun onPreviousClick() {
        if (currentQuizPosition.value!! > 0 && totalPageCount.value!! > 1) {
            pagePositionChangeListener.postValue(Event(currentQuizPosition.value!! - 1))
        }
    }

    fun onNextClick() {
        if (currentQuizPosition.value!! < (totalPageCount.value!! - 1)) {
            pagePositionChangeListener.postValue(Event(currentQuizPosition.value!! + 1))
        }
    }

    fun navigateToAddVehicleAndConductor() {
        navigateToAddVehicleAndConductor.value = Event(occurrenceId)
    }

    fun navigateToAddVictim() {
        navigateToAddVictim.value = Event(occurrenceId)
    }

    fun navigateToAddWitness() {
        navigateToAddWitness.value = Event(occurrenceId)
    }

    fun onNavigateFileScreen() {
        when (context.hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            true -> navigateFileScreen.postValue(Event(Unit))
            false -> storageReadPermissionEvent.value = Event(Unit)
        }
    }

     fun showVictimDeleteConfirmation(pos: Int) {

        val builder = AlertDialog.Builder(activityContext!!).apply {
            setMessage(R.string.delete_victim)
            setPositiveButton(R.string.yes) { _, _ -> onRemoveVictim(pos)}
            setNegativeButton(R.string.no) { _, _ -> }
        }
        builder.create().show()
    }

    fun showWitnessDeleteConfirmation(pos: Int) {

        val builder = AlertDialog.Builder(activityContext!!).apply {
            setMessage(R.string.delete_witness)
            setPositiveButton(R.string.yes) { _, _ -> onRemoveWitness(pos)}
            setNegativeButton(R.string.no) { _, _ -> }
        }
        builder.create().show()
    }


    fun uploadFile(url: String) {
        val value = photos.value!!
//        val name = "photo${value.size + 1}.jpg"
//        photoLoadingStatus.value = Resource.loading()
//        appExecutors.diskIO().execute {
//            var bm = BitmapFactory.decodeFile(url)
//            Timber.d("orginal : " + Formatter.formatFileSize(context, bm.byteCount.toLong()))
//            val options = BitmapCompressOptions()
//            options.height = 480
//            options.width = 480
//            options.config = Bitmap.Config.RGB_565
//
//            bm =Tiny.getInstance().source(url).asBitmap().withOptions(options).compressSync().bitmap
//
//            Timber.d("compress : " + Formatter.formatFileSize(context, bm.byteCount.toLong()))
//            val byteArrayOutputStream = ByteArrayOutputStream()
//            bm.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream)
//
//            Timber.d("bitma : " + Formatter.formatFileSize(context, bm.byteCount.toLong()))
//            val byteArray = byteArrayOutputStream.toByteArray()
//            val photo = Base64.encodeToString(byteArray, Base64.DEFAULT)
//            bm.recycle()
        val photoId = System.currentTimeMillis()
        val name = "${photoId}_${Uri.parse(url).lastPathSegment}"
        val occurrencePhoto =
            OccurrencePhoto(
                name = name,
                occurrenceId = occurrenceId,
                photo = url,
                photoId = photoId
            )
//            appExecutors.mainThread().execute {
        value.add(occurrencePhoto)
        photos.value = value
        //  photoLoadingStatus.value = Resource.success("")
        //  }
        // }
    }

    fun onRemovePhoto(position: Int) {
        val value = photos.value!!
        value.removeAt(position)
        photos.value = value
    }


    fun onSubmitClick() {
        val createOccurrence = createOccurrence()
        occurrenceData.value = createOccurrence
    }

    fun save() {
        occurrenceSaveStatus.value = Resource.loading()
        appExecutors.diskIO().execute {
            val createOccurrence = createOccurrence()
            repository.occurrenceSave(createOccurrence, itemType.value!!)
            appExecutors.mainThread().execute {
                occurrenceSaveStatus.value = Resource.success("Save successfully")
                occurrenceSaveMessage.value = Event("Save successfully")
            }
        }
    }

    private fun createOccurrence(): CreateOccurrence {
        val occurrence = Occurrence(
            usersId = preferenceStorage.userId,
            occurrenceId = occurrenceId,
            address = address.value!!,
            date = date.value!!,
            description = descriptions.value!!,
            hasVictim = hasVictim.value!!,
            hasWitness = hasWitness.value!!,
            occurrenceType = typeOfOccurrence.value!!,
            perimeter = perimeter.value!!,
            time = time.value!!
        )
        return CreateOccurrence(
            occurrence = occurrence,
            vehicleConductor = vehicleConductors.value!!,
            occurrenceVictim = victims.value!!,
            occurreceWitness = witness.value!!,
            occurrencePhotos = photos.value!!
        )
    }

    override fun onCleared() {
        hasVictim.removeObserver(hasVictimObserver())
        hasWitness.removeObserver(hasWitnessObserver())
        occurrenceDataResponse.removeObserver(occurrenceResponseObserver())
        occurrenceDatabaseResult.removeObserver(occurrenceDatabaseResultObserver())
        occurrenceRemoteResult.removeObserver(occurrenceRemoteResultObserver())
        super.onCleared()
    }


    fun dbFormat(date: String): String {
    /* commented in 28/12/2021
    val parser = SimpleDateFormat("dd-MM-yyyy") or
    val parser = SimpleDateFormat("dd/MM/yyyy")
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    return formatter.format(parser.parse(date))
    */
    return date
}



}
