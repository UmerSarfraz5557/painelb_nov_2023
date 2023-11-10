package br.com.painelb.ui.main.fragments.occurrences.create

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import bd.gov.epwdict.util.onPageSelected
import br.com.painelb.R
import br.com.painelb.base.ui.BaseFragment
import br.com.painelb.databinding.FragmentCreateOccurrencesBinding
import br.com.painelb.di.modules.viemodel.AppViewModelFactory
import br.com.painelb.domain.result.EventObserver
import br.com.painelb.model.ItemType
import br.com.painelb.model.occurrences.OccurreceWitnes
import br.com.painelb.model.occurrences.OccurrenceVictim
import br.com.painelb.model.occurrences.VehicleConductor
import br.com.painelb.ui.main.fragments.occurrences.create.adapter.CreateOccurrencesAdapter
import br.com.painelb.ui.main.fragments.occurrences.create.fragments.addvehicle.AddVehicleConductorFragment
import br.com.painelb.ui.main.fragments.occurrences.create.fragments.addvictim.AddVictimFragment
import br.com.painelb.ui.main.fragments.occurrences.create.fragments.adwitness.AddWitnessFragment
import br.com.painelb.ui.main.fragments.occurrences.fragment.sent.OccurrenceSentFragment
import br.com.painelb.util.navigate
import br.com.painelb.util.navigateUp
import br.com.painelb.util.positiveButton
import br.com.painelb.util.showDialog
import timber.log.Timber
import javax.inject.Inject

class CreateOccurrencesFragment :
    BaseFragment<FragmentCreateOccurrencesBinding>(R.layout.fragment_create_occurrences) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val viewModel by viewModels<CreateOccurrenceViewModel> { viewModelFactory }


    private val args: CreateOccurrencesFragmentArgs by navArgs()
    var id: Long = -1L
    lateinit var itemType: ItemType

    override fun haveToolbar() = true
    override fun resToolbarId() = R.id.toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = args.Id
        itemType = args.itemType
        viewModel.activityContext = activity as Context
        if (id != -1L) viewModel.updateFormId(id, itemType)
        Timber.d("onCreate : $id")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = viewModel
        if (id != -1L) getToolbar()?.setTitle(R.string.title_fragment_update_occurrences)

        mBinding.pager.apply {
            val checkListAdapter = CreateOccurrencesAdapter(this@CreateOccurrencesFragment)
            adapter = checkListAdapter
            isUserInputEnabled = false
            viewModel.totalPageCount.value = checkListAdapter.itemCount
            onPageSelected { position -> viewModel.currentQuizPosition.value = position }
        }
        viewModel.messageEvent.observe(viewLifecycleOwner, EventObserver {
            run {
                showDialog(cancelable = false, cancelableTouchOutside = false) {
                    setMessage(it)
                    positiveButton(context.getString(R.string.ok))
                }
            }
        })

        val savedStateHandle = findNavController().previousBackStackEntry?.savedStateHandle
        viewModel.successMessageEvent.observe(viewLifecycleOwner, EventObserver {
            run {
                showDialog(cancelable = false, cancelableTouchOutside = false) {
                    setMessage(it)
                    OccurrenceSentFragment.toReloadData = true
                    positiveButton(context.getString(R.string.ok), handleClick = { navigateUp() })
                }
            }
        })

        viewModel.occurrenceSaveMessage.observe(viewLifecycleOwner, EventObserver {
            run {
                showDialog(cancelable = false, cancelableTouchOutside = false) {
                    setMessage(it)
                    OccurrenceSentFragment.toReloadData = true
                    positiveButton(context.getString(R.string.ok), handleClick = { navigateUp() })
                }
            }
        })

        viewModel.pagePositionChangeListener.observe(viewLifecycleOwner, EventObserver {
            mBinding.pager.currentItem = it
        })

        viewModel.navigateToAddVehicleAndConductor.observe(viewLifecycleOwner, EventObserver {
            navigate(CreateOccurrencesFragmentDirections.navigateToAddVehicleConductorFragment(it))
        })

        val stateHandle = findNavController().currentBackStackEntry?.savedStateHandle
        stateHandle?.getLiveData<VehicleConductor>(AddVehicleConductorFragment.EXTRA_ARGUMENT_ADD_VEHICLE_CONDUCTOR)
            ?.observe(viewLifecycleOwner, Observer {
                viewModel.onAddVehicleConductor(it)
                stateHandle.remove<VehicleConductor>(AddVehicleConductorFragment.EXTRA_ARGUMENT_ADD_VEHICLE_CONDUCTOR)
            })

        stateHandle?.getLiveData<VehicleConductor>(AddVehicleConductorFragment.EXTRA_ARGUMENT_UPDATE_VEHICLE_CONDUCTOR)
            ?.observe(viewLifecycleOwner, Observer {
                viewModel.onUpdateVehicleConductor(it)
                stateHandle.remove<VehicleConductor>(AddVehicleConductorFragment.EXTRA_ARGUMENT_UPDATE_VEHICLE_CONDUCTOR)
            })

        viewModel.navigateToAddVictim.observe(viewLifecycleOwner, EventObserver {
            navigate(CreateOccurrencesFragmentDirections.navigateToAddVictimFragment(it))
        })

        stateHandle?.getLiveData<OccurrenceVictim>(AddVictimFragment.EXTRA_ARGUMENT_ADD_VICTIM)
            ?.observe(viewLifecycleOwner, Observer {
                viewModel.onAddVictim(it)
                stateHandle.remove<OccurrenceVictim>(AddVictimFragment.EXTRA_ARGUMENT_ADD_VICTIM)
            })


        stateHandle?.getLiveData<OccurrenceVictim>(AddVictimFragment.EXTRA_ARGUMENT_UPDATE_VICTIM)
            ?.observe(viewLifecycleOwner, Observer {
                viewModel.onUpdateVictim(it)
                stateHandle.remove<OccurrenceVictim>(AddVictimFragment.EXTRA_ARGUMENT_UPDATE_VICTIM)
            })

        viewModel.navigateToAddWitness.observe(viewLifecycleOwner, EventObserver {
            navigate(CreateOccurrencesFragmentDirections.navigateToAddWitnessFragment(it))
        })

        stateHandle?.getLiveData<OccurreceWitnes>(AddWitnessFragment.EXTRA_ARGUMENT_ADD_WITNESS)
            ?.observe(viewLifecycleOwner, Observer {
                viewModel.onAddWitness(it)
                stateHandle.remove<OccurreceWitnes>(AddWitnessFragment.EXTRA_ARGUMENT_ADD_WITNESS)
            })

        stateHandle?.getLiveData<OccurreceWitnes>(AddWitnessFragment.EXTRA_ARGUMENT_UPDATE_WITNESS)
            ?.observe(viewLifecycleOwner, Observer {
                viewModel.onUpdateWitness(it)
                stateHandle.remove<OccurreceWitnes>(AddWitnessFragment.EXTRA_ARGUMENT_UPDATE_WITNESS)
            })
    }
}
