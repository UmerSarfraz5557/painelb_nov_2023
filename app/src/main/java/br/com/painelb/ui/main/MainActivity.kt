package br.com.painelb.ui.main

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import br.com.painelb.R
import br.com.painelb.base.ui.BaseActivity
import br.com.painelb.databinding.ActivityMainBinding
import br.com.painelb.di.modules.viemodel.AppViewModelFactory
import br.com.painelb.domain.result.Event
import br.com.painelb.domain.result.EventObserver
import br.com.painelb.ui.main.fragments.occurrences.fragment.sent.OccurrencesSentViewModel
import br.com.painelb.util.positiveButton
import br.com.painelb.util.showDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.content.DialogInterface

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val viewModel by viewModels<MainViewModel> { viewModelFactory }


    companion object {
        private val TOP_LEVEL_DESTINATIONS = setOf(R.id.home_fragment)
    }

    private val mNavController by lazy { findNavController(R.id.main_nav_host_fragment) }

    override fun registerToolbarWithNavigation(toolbar: Toolbar) {
        val appBarConfiguration = AppBarConfiguration(TOP_LEVEL_DESTINATIONS)
        toolbar.setupWithNavController(mNavController, appBarConfiguration)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.updateEvent.observe(this@MainActivity,EventObserver {
            Log.d("Update","Not")
            if (it != null){
                if (it){

                    run{
                        Log.d("Update","Ok")
                        showDialog(R.string.update_your_app)
                    }

                }
            }
        })

    }

    private fun showDialog(title: String) {
        AlertDialog.Builder(this)
            .setTitle(R.string.update_available)
            .setMessage(title) // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
            .setPositiveButton(android.R.string.yes,
                DialogInterface.OnClickListener { dialog, which ->
                    // Continue with delete operation
                }) // A null listener allows the button to dismiss the dialog and take no further action.
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()

    }

}
