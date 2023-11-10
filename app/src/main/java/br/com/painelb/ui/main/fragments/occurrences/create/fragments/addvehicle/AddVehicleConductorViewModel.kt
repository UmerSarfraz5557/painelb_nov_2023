package br.com.painelb.ui.main.fragments.occurrences.create.fragments.addvehicle

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import br.com.painelb.R
import br.com.painelb.domain.result.Event
import br.com.painelb.model.occurrences.VehicleConductor
import br.com.painelb.ui.main.fragments.occurrences.create.CreateOccurrencesRepository
import br.com.painelb.util.EMPTY
import javax.inject.Inject
import kotlin.properties.Delegates


class AddVehicleConductorViewModel @Inject constructor(
    private val context: Context,
    private val repository: CreateOccurrencesRepository
) : ViewModel() {

    var vehicleId = System.currentTimeMillis()


    val navigateToCreateOccurrence = MutableLiveData<Event<VehicleConductor>>()
    val navigateToUpdateOccurrence = MutableLiveData<Event<VehicleConductor>>()
    var isUpdate = false

    var occurrenceId by Delegates.notNull<Long>()

    var vehiclePlate = MutableLiveData<String>()
    var errorVehiclePlate = MutableLiveData<String>()

    var vehicleDocumentType = MutableLiveData<String>()
    var errorVehicleDocumentType = MutableLiveData<String>()

    var vehicleDocumentNumber = MutableLiveData<String>()
    var errorVehicleDocumentNumber = MutableLiveData<String>()

    var vehicleType = MutableLiveData<String>()
    var errorVehicleType = MutableLiveData<String>()

    private val occurrenceType = repository.vehicleType()
    val occurrenceTypeData = occurrenceType.map {
        if (it.data != null) it.data!!.map { item ->  item.name }.toTypedArray()
        else arrayOf()
    }


    var damageCategory = MutableLiveData<String>()
    var errorDamageCategory = MutableLiveData<String>()

    var vehicleProcedure = MutableLiveData<String>()
    var errorVehicleProcedure = MutableLiveData<String>()

    var driverName = MutableLiveData<String>()
    var errorDriverName = MutableLiveData<String>()

    var driverDocumentType = MutableLiveData<String>()
    var errorDriverDocumentType = MutableLiveData<String>()

    var driverDocumentNumber = MutableLiveData<String>()
    var errorDriverDocumentNumber = MutableLiveData<String>()

    var driverProcedure = MutableLiveData<String>()
    var errorDriverProcedure = MutableLiveData<String>()

    init {
        resetValue()
        vehiclePlate.observeForever {
            if (!errorVehiclePlate.value.isNullOrBlank()) errorVehiclePlate.value =
                String.EMPTY
        }

        vehicleDocumentType.observeForever {
            if (!errorVehicleDocumentType.value.isNullOrBlank()) errorVehicleDocumentType.value =
                String.EMPTY
        }
        vehicleDocumentNumber.observeForever {
            if (!errorVehicleDocumentNumber.value.isNullOrBlank()) errorVehicleDocumentNumber.value =
                String.EMPTY
        }
        vehicleType.observeForever {
            if (!errorVehicleType.value.isNullOrBlank()) errorVehicleType.value =
                String.EMPTY
        }
        damageCategory.observeForever {
            if (!errorDamageCategory.value.isNullOrBlank()) errorDamageCategory.value =
                String.EMPTY
        }

        vehicleProcedure.observeForever {
            if (!errorVehicleProcedure.value.isNullOrBlank()) errorVehicleProcedure.value =
                String.EMPTY
        }
        driverName.observeForever {
            if (!errorDriverName.value.isNullOrBlank()) errorDriverName.value =
                String.EMPTY
        }

        driverDocumentType.observeForever {
            if (!errorDriverDocumentType.value.isNullOrBlank()) errorDriverDocumentType.value =
                String.EMPTY
        }
        driverDocumentNumber.observeForever {
            if (!errorDriverDocumentNumber.value.isNullOrBlank()) errorDriverDocumentNumber.value =
                String.EMPTY
        }
        driverProcedure.observeForever {
            if (!errorDriverProcedure.value.isNullOrBlank()) errorDriverProcedure.value =
                String.EMPTY
        }
    }

    fun setVehicleConductor(vehicleConductor: VehicleConductor?) {
        if (vehicleConductor != null) {
            isUpdate = true
            vehicleId = vehicleConductor.vehicleId
            vehiclePlate.value = vehicleConductor.plateVehicle
            vehicleDocumentType.value = vehicleConductor.docVehicleType
            vehicleDocumentNumber.value = vehicleConductor.docVehicleNumber
            vehicleType.value = vehicleConductor.vehicleType
            damageCategory.value = vehicleConductor.damageCategory
            vehicleProcedure.value = vehicleConductor.vehicleProcedure
            driverName.value = vehicleConductor.driverName
            driverDocumentType.value = vehicleConductor.driverDocumentType
            driverDocumentNumber.value = vehicleConductor.driverDocumentNumber
            driverProcedure.value = vehicleConductor.driverProcedure
        }
    }

    private fun resetValue() {
        vehiclePlate.value = String.EMPTY
        errorVehiclePlate.value = String.EMPTY
        vehicleDocumentType.value = String.EMPTY
        errorVehicleDocumentType.value = String.EMPTY
        vehicleDocumentNumber.value = String.EMPTY
        errorVehicleDocumentNumber.value = String.EMPTY
        vehicleType.value = String.EMPTY
        errorVehicleType.value = String.EMPTY
        damageCategory.value = String.EMPTY
        errorDamageCategory.value = String.EMPTY
        vehicleProcedure.value = String.EMPTY
        errorVehicleProcedure.value = String.EMPTY
        driverName.value = String.EMPTY
        errorDriverName.value = String.EMPTY
        driverDocumentType.value = String.EMPTY
        errorDriverDocumentType.value = String.EMPTY
        driverDocumentNumber.value = String.EMPTY
        errorDriverDocumentNumber.value = String.EMPTY
        driverProcedure.value = String.EMPTY
        errorDriverProcedure.value = String.EMPTY
        vehicleId = System.currentTimeMillis()
    }

    fun vehicleDocumentTypeItemSelected(item: Any) {
        (item as String).apply { vehicleDocumentType.value = item }
    }

    fun vehicleTypeTypeItemSelected(item: Any) {
        (item as String).apply { vehicleType.value = item }
    }

    fun damageCategoryItemSelected(item: Any) {
        (item as String).apply { damageCategory.value = item }
    }

    fun vehicleProcedureItemSelected(item: Any) {
        (item as String).apply { vehicleProcedure.value = item }
    }

    fun driverDocumentTypeItemSelected(item: Any) {
        (item as String).apply { driverDocumentType.value = item }
    }

    fun driverProcedureItemSelected(item: Any) {
        (item as String).apply { driverProcedure.value = item }
    }

    fun validateData() {
        when {
            //vehiclePlate.value.isNullOrEmpty() -> errorVehiclePlate.value = context.getString(R.string.enter_vehicle_plate)
            vehicleDocumentType.value.isNullOrEmpty() -> errorVehicleDocumentType.value =
                context.getString(R.string.enter_vehicle_document_type)
            vehicleDocumentNumber.value.isNullOrEmpty() -> errorVehicleDocumentNumber.value =
                context.getString(R.string.enter_vehicle_document_number)
            vehicleType.value.isNullOrEmpty() -> errorVehicleType.value =
                context.getString(R.string.enter_vehicle_type)
            damageCategory.value.isNullOrEmpty() -> errorDamageCategory.value =
                context.getString(R.string.enter_damage_category)
            vehicleProcedure.value.isNullOrEmpty() -> errorVehicleProcedure.value =
                context.getString(R.string.enter_vehicle_procedure)
            driverName.value.isNullOrEmpty() -> errorDriverName.value =
                context.getString(R.string.enter_driver_name)
            driverDocumentType.value.isNullOrEmpty() -> errorDriverDocumentType.value =
                context.getString(R.string.enter_driver_document_type)
            //driverDocumentNumber.value.isNullOrEmpty() -> errorDriverDocumentNumber.value = context.getString(R.string.enter_driver_document_number)
            driverProcedure.value.isNullOrEmpty() -> errorDriverProcedure.value =
                context.getString(R.string.enter_driver_procedure)
            else -> onSubmitClick()
        }
    }

    private fun onSubmitClick() {
        val vehicleConductor = VehicleConductor(
            vehicleId = vehicleId,
            occurrenceId = occurrenceId,
            plateVehicle = vehiclePlate.value!!,
            docVehicleType = vehicleDocumentType.value!!,
            docVehicleNumber = vehicleDocumentNumber.value!!,
            vehicleType = vehicleType.value!!,
            damageCategory = damageCategory.value!!,
            vehicleProcedure = vehicleProcedure.value!!,
            driverName = driverName.value!!,
            driverDocumentType = driverDocumentType.value!!,
            driverDocumentNumber = driverDocumentNumber.value!!,
            driverProcedure = driverProcedure.value!!
        )

        val event = Event(vehicleConductor)
        if (isUpdate) navigateToUpdateOccurrence.value = event
        else
            navigateToCreateOccurrence.value = event
    }
}
