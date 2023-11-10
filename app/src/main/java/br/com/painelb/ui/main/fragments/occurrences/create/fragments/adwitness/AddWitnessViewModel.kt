package br.com.painelb.ui.main.fragments.occurrences.create.fragments.adwitness

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.painelb.R
import br.com.painelb.domain.result.Event
import br.com.painelb.model.occurrences.OccurreceWitnes
import br.com.painelb.util.EMPTY
import javax.inject.Inject
import kotlin.properties.Delegates


class AddWitnessViewModel @Inject constructor(
    private val context: Context
) : ViewModel() {

    var isUpdate = false
    var witnessId = System.currentTimeMillis()

    val navigateToCreateWitness = MutableLiveData<Event<OccurreceWitnes>>()
    val navigateToUpdateWitness = MutableLiveData<Event<OccurreceWitnes>>()

    var occurrenceId by Delegates.notNull<Long>()

    var witnessName = MutableLiveData<String>()
    var errorwitnessName = MutableLiveData<String>()

    var witnessDocumentType = MutableLiveData<String>()
    var errorWitnessDocumentType = MutableLiveData<String>()


    var witnessDocumentNumber = MutableLiveData<String>()
    var errorWitnessDocumentNumber = MutableLiveData<String>()

    var witnessAddress = MutableLiveData<String>()
    var errorWitnessAddress = MutableLiveData<String>()

    init {
        resetValue()
        witnessName.observeForever {
            if (!errorwitnessName.value.isNullOrBlank()) errorwitnessName.value =
                String.EMPTY
        }

        witnessDocumentType.observeForever {
            if (!errorWitnessDocumentType.value.isNullOrBlank()) errorWitnessDocumentType.value =
                String.EMPTY
        }

        witnessDocumentNumber.observeForever {
            if (!errorWitnessDocumentNumber.value.isNullOrBlank()) errorWitnessDocumentNumber.value =
                String.EMPTY
        }
        witnessAddress.observeForever {
            if (!errorWitnessAddress.value.isNullOrBlank()) errorWitnessAddress.value =
                String.EMPTY
        }
    }

    fun setOccurreceWitnes(occurreceWitnes: OccurreceWitnes?) {
        if (occurreceWitnes != null) {
            isUpdate = true
            witnessId = occurreceWitnes.witnessId
            witnessName.value = occurreceWitnes.name
            witnessDocumentType.value = occurreceWitnes.documentType
            witnessDocumentNumber.value = occurreceWitnes.documentNumber
            witnessAddress.value = occurreceWitnes.address
        }
    }

    private fun resetValue() {
        witnessName.value = String.EMPTY
        errorwitnessName.value = String.EMPTY
        witnessDocumentType.value = String.EMPTY
        errorWitnessDocumentType.value = String.EMPTY
        witnessDocumentNumber.value = String.EMPTY
        errorWitnessDocumentNumber.value = String.EMPTY
        witnessAddress.value = String.EMPTY
        errorWitnessAddress.value = String.EMPTY
        witnessId = System.currentTimeMillis()
    }

    fun witnessDocumentTypeItemSelected(item: Any) {
        (item as String).apply { witnessDocumentType.value = item }
    }

    fun validateData() {
        when {
            witnessName.value.isNullOrEmpty() -> errorwitnessName.value =
                context.getString(R.string.enter_witness_name)
            witnessDocumentType.value.isNullOrEmpty() -> errorWitnessDocumentType.value =
                context.getString(R.string.enter_witness_document_type)
            witnessDocumentNumber.value.isNullOrEmpty() -> errorWitnessDocumentNumber.value =
                context.getString(R.string.enter_witness_document_number)
            //witnessAddress.value.isNullOrEmpty() -> errorWitnessAddress.value = context.getString(R.string.enter_witness_address)
            else -> onSubmitClick()
        }
    }

    private fun onSubmitClick() {
        val occurrencesWitness = OccurreceWitnes(
            witnessId = witnessId,
            occurrenceId = occurrenceId,
            address = witnessAddress.value!!,
            documentNumber = witnessDocumentNumber.value!!,
            name = witnessName.value!!,
            documentType = witnessDocumentType.value!!
        )
        val event = Event(occurrencesWitness)
        if (isUpdate) navigateToUpdateWitness.value = event
        else navigateToCreateWitness.value = event
    }
}
