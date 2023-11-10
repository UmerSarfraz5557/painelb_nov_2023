package br.com.painelb.ui.main.fragments.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import br.com.painelb.db.dao.checklist.CheckListDao
import br.com.painelb.db.dao.occurrence.OccurrenceDao
import br.com.painelb.domain.result.Event
import br.com.painelb.network.Status
import br.com.painelb.prefs.PreferenceStorage
import br.com.painelb.util.LogoutHelper
import br.com.painelb.util.map
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    storage: PreferenceStorage,
    private val logoutHelper: LogoutHelper,
    occurrenceDao: OccurrenceDao,
    checkListDao: CheckListDao
) : ViewModel() {
    val name = storage.name
    val email = storage.email
    val navigateToLogoutDialog = MutableLiveData<Event<Unit>>()
    val navigateToOccurrence = MutableLiveData<Event<Unit>>()
    val navigateToCheckList = MutableLiveData<Event<Unit>>()

    private val navigateToLogoutConfirm = MutableLiveData<Event<Unit>>()

    private val logoutStatus = navigateToLogoutConfirm.switchMap { logoutHelper.logout() }
    val navigateToAuthScreen = MutableLiveData<Event<Unit>>()

    val occurrenceCount = occurrenceDao.getOccurrenceDistinctLiveData().map {
        it.count().toString()
    }
    val checkListCount = checkListDao.getChecklistDataDistinctLiveData().map {
        it.count().toString()
    }

    init {
        logoutStatus.observeForever {
            if (it.status == Status.SUCCESS) navigateToAuthScreen.value = Event(Unit)
        }
    }

    fun logout() = navigateToLogoutDialog.postValue(Event(Unit))
    fun onConfirmLogout() = navigateToLogoutConfirm.postValue(Event(Unit))
    fun onOccurrenceClick() = navigateToOccurrence.postValue(Event(Unit))
    fun onCheckListClick() = navigateToCheckList.postValue(Event(Unit))
}
