package br.com.painelb.ui.main.fragments.checklist.fragment.pending

import androidx.lifecycle.LiveData
import br.com.painelb.db.dao.checklist.CheckListDao
import br.com.painelb.model.checklist.Checklist
import javax.inject.Inject

class CheckListPendingRepository @Inject constructor(
    private val checkListDao: CheckListDao
) {

    fun checklist(): LiveData<List<Checklist>> {
        return checkListDao.getChecklistDataDistinctLiveData()
    }

    fun delete(id: Long) {
        checkListDao.deleteById(id)
    }
}
