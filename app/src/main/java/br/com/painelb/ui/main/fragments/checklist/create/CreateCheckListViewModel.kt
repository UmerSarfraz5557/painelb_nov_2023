package br.com.painelb.ui.main.fragments.checklist.create

import android.Manifest
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import androidx.lifecycle.*
import br.com.painelb.R
import br.com.painelb.db.dao.relational.CheckListRelation
import br.com.painelb.db.table.checklist.ChecklistPhotos
import br.com.painelb.domain.result.Event
import br.com.painelb.model.ItemType
import br.com.painelb.model.Response
import br.com.painelb.model.checklist.Checklist
import br.com.painelb.model.checklist.CreateCheckList
import br.com.painelb.network.Resource
import br.com.painelb.network.Status
import br.com.painelb.prefs.PreferenceStorage
import br.com.painelb.util.AppExecutors
import br.com.painelb.util.EMPTY
import br.com.painelb.util.hasPermission
import timber.log.Timber
import javax.inject.Inject

class CreateCheckListViewModel @Inject constructor(
    private val context: Context,
    private val preferenceStorage: PreferenceStorage,
    private val repository: CreateCheckListRepository,
    private val appExecutors: AppExecutors
) : ViewModel() {
    val itemType = MutableLiveData(ItemType.NEW)

    val messageEvent = MutableLiveData<Event<String>>()
    val successMessageEvent = MutableLiveData<Event<String>>()
    val navigateFileScreen = MutableLiveData<Event<Unit>>()
    val storageReadPermissionEvent = MutableLiveData<Event<Unit>>()

    val currentQuizPosition = MutableLiveData<Int>()
    val pagePositionChangeListener = MutableLiveData<Event<Int>>()
    val totalPageCount = MutableLiveData<Int>()

    private var checklistId = System.currentTimeMillis()

    var date = MutableLiveData<String>()
    var errorDate = MutableLiveData<String>()

    var teamService = MutableLiveData<String>()
    var errorTeamService = MutableLiveData<String>()

    var conductor = MutableLiveData<String>()
    var errorConductor = MutableLiveData<String>()

    var plate = MutableLiveData<String>()
    var errorPlate = MutableLiveData<String>()

    var prisma = MutableLiveData<String>()
    var errorPrisma = MutableLiveData<String>()

    var departureTime = MutableLiveData<String>()
    var errorDepartureTime = MutableLiveData<String>()

    var returnTime = MutableLiveData<String>()
    var errorReturnTime = MutableLiveData<String>()

    var kmDeparture = MutableLiveData<String>()
    var errorKmDeparture = MutableLiveData<String>()

    var kmReturn = MutableLiveData<String>()
    var errorKmReturn = MutableLiveData<String>()

    var outputFuelQuantity = MutableLiveData<String>()
    var errorOutputFuelQuantity = MutableLiveData<String>()

    var returnFuelQuantity = MutableLiveData<String>()
    var errorReturnFuelQuantity = MutableLiveData<String>()

    var carpet = MutableLiveData<Boolean>()
    val spinnerCarpetVisible = MutableLiveData<Int>()

    var tireIron = MutableLiveData<Boolean>()
    var triangue = MutableLiveData<Boolean>()
    var monkey = MutableLiveData<Boolean>()
    var frontLightingSystem = MutableLiveData<Boolean>()
    var reaLightingSystem = MutableLiveData<Boolean>()
    var sirene = MutableLiveData<Boolean>()
    var flashing = MutableLiveData<Boolean>()
    var supplyCard = MutableLiveData<Boolean>()
    var crlv = MutableLiveData<Boolean>()
    var glacier = MutableLiveData<Boolean>()
    var etilometer = MutableLiveData<Boolean>()
    var pneus = MutableLiveData<Boolean>()
    var stereo = MutableLiveData<Boolean>()
    var cones = MutableLiveData<Boolean>()
    var conesQuantities = MutableLiveData<String>()
    var superCones = MutableLiveData<Boolean>()
    var supperConesQuantities = MutableLiveData<String>()
    var newJersey = MutableLiveData<Boolean>()
    var newJerseyQuantities = MutableLiveData<String>()
    var handle = MutableLiveData<Boolean>()
    var handleQuantities = MutableLiveData<String>()
    var observationsQuantities = MutableLiveData<String>()

    private val checkListData = MutableLiveData<CreateCheckList>()
    val resetValue = MutableLiveData<Event<Unit>>()

    private val checkListDataResponse = checkListData.switchMap {
        repository.createCheckList(it, itemType.value!!)
    }

    fun createSpinnerAdapter(context: Context): ArrayAdapter<CharSequence> {
        return ArrayAdapter.createFromResource(
            context,
            R.array.carpet,
            android.R.layout.simple_spinner_item
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

    private fun checkListResponseObserver(): Observer<Resource<Response>> {
        return Observer {
            loadingStatus.value = it
            when {
                Status.SUCCESS == it.status -> successMessageEvent.value =
                    Event(it.data?.message ?: "")
                Status.ERROR == it.status -> messageEvent.value = Event(it.message ?: "")
            }
        }
    }

    val photos = MutableLiveData<ArrayList<ChecklistPhotos>>(ArrayList())
    val photoLoadingStatus = MutableLiveData<Resource<String>>()

    val loadingStatus = MutableLiveData<Resource<Response>>()

    val checklistSaveStatus = MutableLiveData<Resource<String>>()
    val checklistSaveMessage = MutableLiveData<Event<String>>()

    private val checkListDatabaseResult = MediatorLiveData<Resource<CheckListRelation>>()
    private fun checkListDatabaseResultObserver(): Observer<Resource<CheckListRelation>> {
        return Observer {
            loadingStatus.value = Resource(it.status, null, null)
            if (Status.SUCCESS == it.status) {
                val data = it.data
                Timber.d("The Data is ${data?.checklist?.checklist?.handleQuantities}")
                if (data != null) {
                    Timber.d("The Data is ${data.checklist.checklist.handleQuantities}")
                    val checklist = data.checklist
                    checklistId = checklist.id
                    conductor.value = checklist.checklist.conductor
                    teamService.value = checklist.checklist.teamService
                    date.value = checklist.checklist.checkDate
                    plate.value = checklist.checklist.plate
                    prisma.value = checklist.checklist.prisma
                    departureTime.value = checklist.checklist.departureTime
                    returnTime.value = checklist.checklist.returnTime
                    kmDeparture.value = checklist.checklist.kmDeparture
                    kmReturn.value = checklist.checklist.kmReturn
                    outputFuelQuantity.value = checklist.checklist.outputFuelQuantity
                    returnFuelQuantity.value = checklist.checklist.returnFuelQuantity
                    carpet.value = checklist.checklist.carpet
                    tireIron.value = checklist.checklist.tireIron
                    triangue.value = checklist.checklist.triangue
                    monkey.value = checklist.checklist.monkey
                    frontLightingSystem.value = checklist.checklist.frontLightingSystem
                    reaLightingSystem.value = checklist.checklist.backLightingSystem
                    sirene.value = checklist.checklist.sirene
                    flashing.value = checklist.checklist.flashing
                    supplyCard.value = checklist.checklist.supplyCard
                    crlv.value = checklist.checklist.crlv
                    glacier.value = checklist.checklist.glacier
                    etilometer.value = checklist.checklist.etilometer
                    pneus.value = checklist.checklist.pneus
                    stereo.value = checklist.checklist.stereo
                    cones.value = checklist.checklist.cones
                    conesQuantities.value = checklist.checklist.conesQuantities
                    superCones.value = checklist.checklist.superCones
                    supperConesQuantities.value = checklist.checklist.superConesQuantities
                    newJersey.value = checklist.checklist.newJersey
                    newJerseyQuantities.value = checklist.checklist.newJerseyQuantities
                    handle.value = checklist.checklist.handle
                    handleQuantities.value = checklist.checklist.handleQuantities
                    observationsQuantities.value = checklist.checklist.observation_quantities
                    photos.value = ArrayList(data.photos)
                }
            } else if (Status.ERROR == it.status) {
                successMessageEvent.value =
                    Event(it?.message ?: context.getString(R.string.error_message_unknown))
            }
        }
    }

    private val checkListRemoteResult = MediatorLiveData<Resource<CreateCheckList>>()

    private fun checkListRemoteResultObserver(): Observer<Resource<CreateCheckList>> {
        return Observer {
            loadingStatus.value = Resource(it.status, null, null)
            if (Status.SUCCESS == it.status) {
                val data = it.data
                if (data != null) {
                    val checklist = data.checklist
                    checklistId = checklist.checklistId
                    conductor.value = checklist.conductor
                    teamService.value = checklist.teamService
                    date.value = checklist.checkDate
                    plate.value = checklist.plate
                    prisma.value = checklist.prisma
                    departureTime.value = checklist.departureTime
                    returnTime.value = checklist.returnTime
                    kmDeparture.value = checklist.kmDeparture
                    kmReturn.value = checklist.kmReturn
                    outputFuelQuantity.value = checklist.outputFuelQuantity
                    returnFuelQuantity.value = checklist.returnFuelQuantity
                    carpet.value = checklist.carpet
                    tireIron.value = checklist.tireIron
                    triangue.value = checklist.triangue
                    monkey.value = checklist.monkey
                    frontLightingSystem.value = checklist.frontLightingSystem
                    reaLightingSystem.value = checklist.backLightingSystem
                    sirene.value = checklist.sirene
                    flashing.value = checklist.flashing
                    supplyCard.value = checklist.supplyCard
                    crlv.value = checklist.crlv
                    glacier.value = checklist.glacier
                    etilometer.value = checklist.etilometer
                    pneus.value = checklist.pneus
                    stereo.value = checklist.stereo
                    cones.value = checklist.cones
                    conesQuantities.value = checklist.conesQuantities
                    superCones.value = checklist.superCones
                    supperConesQuantities.value = checklist.superConesQuantities
                    newJersey.value = checklist.newJersey
                    newJerseyQuantities.value = checklist.newJerseyQuantities
                    handle.value = checklist.handle
                    handleQuantities.value = checklist.handleQuantities
                    observationsQuantities.value = checklist.observation_quantities
                    photos.value = ArrayList(data.checklistPhotos)
                }
            } else if (Status.ERROR == it.status) {
                successMessageEvent.value =
                    Event(it?.message ?: context.getString(R.string.error_message_unknown))
            }
        }
    }

    init {
        resetValue()

        spinnerCarpetVisible.value = if (carpet.value == true) View.VISIBLE else View.GONE

        date.observeForever {
            if (!errorDate.value.isNullOrBlank()) errorDate.value = String.EMPTY
        }

        teamService.observeForever {
            if (!errorTeamService.value.isNullOrBlank()) errorTeamService.value = String.EMPTY
        }

        conductor.observeForever {
            if (!errorConductor.value.isNullOrBlank()) errorConductor.value = String.EMPTY
        }

        plate.observeForever {
            if (!errorPlate.value.isNullOrBlank()) errorPlate.value = String.EMPTY
        }

        prisma.observeForever {
            if (!errorPrisma.value.isNullOrBlank()) errorPrisma.value = String.EMPTY
        }

        departureTime.observeForever {
            if (!errorDepartureTime.value.isNullOrBlank()) errorDepartureTime.value = String.EMPTY
        }

        returnTime.observeForever {
            if (!errorReturnTime.value.isNullOrBlank()) errorReturnTime.value = String.EMPTY
        }

        kmDeparture.observeForever {
            if (!errorKmDeparture.value.isNullOrBlank()) errorKmDeparture.value = String.EMPTY
        }

        kmReturn.observeForever {
            if (!errorKmReturn.value.isNullOrBlank()) errorKmReturn.value = String.EMPTY
        }

        outputFuelQuantity.observeForever {
            if (!errorOutputFuelQuantity.value.isNullOrBlank()) errorOutputFuelQuantity.value =
                String.EMPTY
        }

        returnFuelQuantity.observeForever {
            //if (!errorReturnFuelQuantity.value.isNullOrBlank()) errorReturnFuelQuantity.value = String.EMPTY
        }

        checkListDataResponse.observeForever(checkListResponseObserver())
        checkListDatabaseResult.observeForever(checkListDatabaseResultObserver())
        checkListRemoteResult.observeForever(checkListRemoteResultObserver())
    }

    fun onCarpetCheckedChanged(isChecked: Boolean) {
        carpet.value = isChecked
        spinnerCarpetVisible.value = if (isChecked) View.VISIBLE else View.GONE
    }

    private fun resetValue() {
        date.value = String.EMPTY
        errorDate.value = String.EMPTY
        teamService.value = String.EMPTY
        errorTeamService.value = String.EMPTY
        conductor.value = String.EMPTY
        errorConductor.value = String.EMPTY
        plate.value = String.EMPTY
        errorPlate.value = String.EMPTY
        prisma.value = String.EMPTY
        errorPrisma.value = String.EMPTY
        departureTime.value = String.EMPTY
        errorDepartureTime.value = String.EMPTY
        returnTime.value = String.EMPTY
        errorReturnTime.value = String.EMPTY
        kmDeparture.value = String.EMPTY
        errorKmDeparture.value = String.EMPTY
        kmReturn.value = String.EMPTY
        errorKmReturn.value = String.EMPTY
        outputFuelQuantity.value = String.EMPTY
        errorOutputFuelQuantity.value = String.EMPTY
        returnFuelQuantity.value = String.EMPTY
        errorReturnFuelQuantity.value = String.EMPTY

        carpet.value = false
        tireIron.value = false
        triangue.value = false
        monkey.value = false
        frontLightingSystem.value = false
        reaLightingSystem.value = false
        sirene.value = false
        flashing.value = false
        supplyCard.value = false
        crlv.value = false
        glacier.value = false
        etilometer.value = false
        pneus.value = false
        stereo.value = false
        cones.value = false
        conesQuantities.value = String.EMPTY
        superCones.value = false
        supperConesQuantities.value = String.EMPTY
        newJersey.value = false
        newJerseyQuantities.value = String.EMPTY
        handle.value = false
        handleQuantities.value = String.EMPTY
        observationsQuantities.value = String.EMPTY
        photos.value?.clear()
        resetValue.value = Event(Unit)
    }

    fun teamServiceItemSelected(item: Any) {
        (item as String).apply { teamService.value = item }
    }

    fun outputFuelQuantityItemSelected(item: Any) {
        (item as String).apply { outputFuelQuantity.value = item }
    }

    fun returnFuelQuantityItemSelected(item: Any) {
        (item as String).apply { returnFuelQuantity.value = item }
    }

    fun validateVehicleData() {
        when {
            date.value.isNullOrEmpty() -> errorDate.value = context.getString(R.string.enter_date)
            teamService.value.isNullOrEmpty() -> errorTeamService.value =
                context.getString(R.string.enter_team_service)
            conductor.value.isNullOrEmpty() -> errorConductor.value =
                context.getString(R.string.enter_conductor)
            plate.value.isNullOrEmpty() -> errorPlate.value =
                context.getString(R.string.enter_plate)
            prisma.value.isNullOrEmpty() -> errorPrisma.value =
                context.getString(R.string.enter_prisma)
            departureTime.value.isNullOrEmpty() -> errorDepartureTime.value =
                context.getString(R.string.enter_departure_time)
//            returnTime.value.isNullOrEmpty() -> errorReturnTime.value =
//                context.getString(R.string.enter_return_time)
            kmDeparture.value.isNullOrEmpty() -> errorKmDeparture.value =
                context.getString(R.string.enter_km_departure)
            //kmReturn.value.isNullOrEmpty() -> errorKmReturn.value = context.getString(R.string.enter_km_return)
            outputFuelQuantity.value.isNullOrEmpty() -> errorOutputFuelQuantity.value = context.getString(R.string.enter_output_fuel_quantity)
            //returnFuelQuantity.value.isNullOrEmpty() -> errorReturnFuelQuantity.value = context.getString(R.string.enter_return_fuel_quantity)
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

    fun onSubmitClick() {
        val createCheckList = createCheckList()
        checkListData.postValue(createCheckList)
    }

    private fun createCheckList(): CreateCheckList {
        val checklist = Checklist(
            checklistId = checklistId,
            usersId = preferenceStorage.userId,
            teamService = teamService.value!!,
            conductor = conductor.value!!,
            checkDate = date.value!!,
            plate = plate.value!!,
            prisma = prisma.value!!,
            departureTime = departureTime.value!!,
            returnTime = returnTime.value?: String.EMPTY,
            kmDeparture = kmDeparture.value!!,
            kmReturn = kmReturn.value!!,
            outputFuelQuantity = outputFuelQuantity.value!!,
            returnFuelQuantity = returnFuelQuantity.value!!,
            carpet = carpet.value!!,
            tireIron = tireIron.value!!,
            triangue = triangue.value!!,
            monkey = monkey.value!!,
            frontLightingSystem = frontLightingSystem.value!!,
            backLightingSystem = reaLightingSystem.value!!,
            sirene = sirene.value!!,
            flashing = flashing.value!!,
            supplyCard = supplyCard.value!!,
            crlv = crlv.value!!,
            glacier = glacier.value!!,
            etilometer = etilometer.value!!,
            pneus = pneus.value!!,
            stereo = stereo.value!!,
            cones = cones.value!!,
            conesQuantities = conesQuantities.value!!,
            superCones = superCones.value!!,
            superConesQuantities = supperConesQuantities.value!!,
            newJersey = newJersey.value!!,
            newJerseyQuantities = newJerseyQuantities.value!!,
            handle = handle.value!!,
            handleQuantities = handleQuantities.value!!,
            //observations = observations.value!!,
            observation_quantities = observationsQuantities.value!!
        )
        return CreateCheckList(checklist, photos.value!!)
    }

    fun updateFormId(id: Long, itemType: ItemType) {
        Timber.d("updateFormId : $id")
        this.itemType.value = itemType
        if (itemType == ItemType.LOCAL) setDataFromLocal(id)
        else if (itemType == ItemType.REMOTE) setDataFromServer(id)
    }

    private fun setDataFromLocal(id: Long) {
        val source = repository.getChecklistFromDatabase(id)
        checkListDatabaseResult.removeSource(source)
        checkListDatabaseResult.addSource(source) {
            checkListDatabaseResult.value = it
            if (it.status == Status.ERROR || it.status == Status.SUCCESS)
                checkListDatabaseResult.removeSource(source)
        }
    }

    private fun setDataFromServer(id: Long) {
        val source = repository.getChecklistById(id)
        checkListRemoteResult.removeSource(source)
        checkListRemoteResult.addSource(source) {
            checkListRemoteResult.value = it
            if (it.status == Status.ERROR || it.status == Status.SUCCESS)
                checkListRemoteResult.removeSource(source)
        }

    }

    fun save() {
        checklistSaveStatus.value = Resource.loading()
        appExecutors.diskIO().execute {
            val checklist = createCheckList()
            repository.save(checklist, itemType.value!!)
            appExecutors.mainThread().execute {
                checklistSaveStatus.value = Resource.success("Save successfully")
                checklistSaveMessage.value = Event("Save successfully")
            }
        }
    }

    fun onNavigateFileScreen() {
        when (context.hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            true -> navigateFileScreen.postValue(Event(Unit))
            false -> storageReadPermissionEvent.value = Event(Unit)
        }
    }

    fun uploadFile(url: String) {
        val value = photos.value!!
        val photoId = System.currentTimeMillis()
        val name = "${photoId}_${Uri.parse(url).lastPathSegment}"
        val checklistPhoto =
            ChecklistPhotos(
                photoId = photoId,
                name = name,
                photo = url,
                checklistId = checklistId
            )
        value.add(checklistPhoto)
        photos.value = value
    }

    fun onRemovePhoto(position: Int) {
        val value = photos.value!!
        value.removeAt(position)
        photos.value = value
    }

    override fun onCleared() {
        checkListDataResponse.removeObserver(checkListResponseObserver())
        checkListDatabaseResult.removeObserver(checkListDatabaseResultObserver())
        checkListRemoteResult.removeObserver(checkListRemoteResultObserver())
        super.onCleared()
    }
}
