package br.com.painelb.di.modules.main

import androidx.lifecycle.ViewModel
import br.com.painelb.di.modules.viemodel.ViewModelKey
import br.com.painelb.di.scope.ChildFragmentScoped
import br.com.painelb.di.scope.FragmentScoped
import br.com.painelb.ui.main.MainViewModel
import br.com.painelb.ui.main.fragments.checklist.CheckListFragment
import br.com.painelb.ui.main.fragments.checklist.create.CreateCheckListContainerFragment
import br.com.painelb.ui.main.fragments.checklist.create.CreateCheckListViewModel
import br.com.painelb.ui.main.fragments.checklist.create.fragment.CreateCheckListFragment
import br.com.painelb.ui.main.fragments.checklist.create.fragment.ImageFragment
import br.com.painelb.ui.main.fragments.checklist.create.fragment.PreviewFragment
import br.com.painelb.ui.main.fragments.checklist.create.fragment.VehicleFragment
import br.com.painelb.ui.main.fragments.checklist.fragment.pending.CheckListPendingFragment
import br.com.painelb.ui.main.fragments.checklist.fragment.pending.CheckListPendingViewModel
import br.com.painelb.ui.main.fragments.checklist.fragment.sent.CheckListSentFragment
import br.com.painelb.ui.main.fragments.checklist.fragment.sent.CheckListSentViewModel
import br.com.painelb.ui.main.fragments.home.HomeFragment
import br.com.painelb.ui.main.fragments.home.HomeViewModel
import br.com.painelb.ui.main.fragments.occurrences.OccurrencesFragment
import br.com.painelb.ui.main.fragments.occurrences.create.CreateOccurrenceViewModel
import br.com.painelb.ui.main.fragments.occurrences.create.CreateOccurrencesFragment
import br.com.painelb.ui.main.fragments.occurrences.create.fragments.*
import br.com.painelb.ui.main.fragments.occurrences.create.fragments.addvehicle.AddVehicleConductorFragment
import br.com.painelb.ui.main.fragments.occurrences.create.fragments.addvehicle.AddVehicleConductorViewModel
import br.com.painelb.ui.main.fragments.occurrences.create.fragments.addvictim.AddVictimFragment
import br.com.painelb.ui.main.fragments.occurrences.create.fragments.addvictim.AddVictimViewModel
import br.com.painelb.ui.main.fragments.occurrences.create.fragments.adwitness.AddWitnessFragment
import br.com.painelb.ui.main.fragments.occurrences.create.fragments.adwitness.AddWitnessViewModel
import br.com.painelb.ui.main.fragments.occurrences.fragment.pending.OccurrencePendingFragment
import br.com.painelb.ui.main.fragments.occurrences.fragment.pending.OccurrencesPendingViewModel
import br.com.painelb.ui.main.fragments.occurrences.fragment.sent.OccurrenceSentFragment
import br.com.painelb.ui.main.fragments.occurrences.fragment.sent.OccurrencesSentViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class MainModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeHomeFragment(): HomeFragment

    @Binds
    @IntoMap
    @ViewModelKey(OccurrencesSentViewModel::class)
    abstract fun bindOccurrencesSentViewModel(sentViewModel: OccurrencesSentViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindOccurrencesMainViewModel(mainViewModel: MainViewModel): ViewModel

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeOccurrencesFragment(): OccurrencesFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeOccurrenceSentFragment(): OccurrenceSentFragment

    @Binds
    @IntoMap
    @ViewModelKey(OccurrencesPendingViewModel::class)
    abstract fun bindOccurrencesPendingViewModel(viewModelCreate: OccurrencesPendingViewModel): ViewModel

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeOccurrencePendingFragment(): OccurrencePendingFragment

    @Binds
    @IntoMap
    @ViewModelKey(CreateCheckListViewModel::class)
    abstract fun bindCreateCheckListViewModel(viewModelCreate: CreateCheckListViewModel): ViewModel

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeCheckListFragment(): CheckListFragment

    @Binds
    @IntoMap
    @ViewModelKey(CheckListSentViewModel::class)
    abstract fun bindCheckListSentViewModel(viewModel: CheckListSentViewModel): ViewModel

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeCheckListSentFragment(): CheckListSentFragment

    @Binds
    @IntoMap
    @ViewModelKey(CheckListPendingViewModel::class)
    abstract fun bindCheckListPendingViewModel(viewModel: CheckListPendingViewModel): ViewModel

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeCheckListPendingFragment(): CheckListPendingFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeCheckFragment(): CreateCheckListContainerFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeVehicleFragment(): VehicleFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeCreateCheckListFragment(): CreateCheckListFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeImageFragment(): ImageFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributePreviewFragment(): PreviewFragment

    @Binds
    @IntoMap
    @ViewModelKey(CreateOccurrenceViewModel::class)
    abstract fun bindCreateOccurrenceViewModel(viewModel: CreateOccurrenceViewModel): ViewModel

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeCreateOccurrencesFragment(): CreateOccurrencesFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeOccurrenceInformationFragment(): OccurrenceInformationFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeDriverFragment(): VehicleConductorFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeVictimFragment(): VictimFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeWitnessFragment(): WitnessFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeOccurrenceImageFragment(): OccurrenceImageFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeOccurrencePreviewFragment(): OccurrencePreviewFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeAddVehicleConductorFragment(): AddVehicleConductorFragment

    @Binds
    @IntoMap
    @ViewModelKey(AddVehicleConductorViewModel::class)
    abstract fun bindAddVehicleConductorViewModel(viewModel: AddVehicleConductorViewModel): ViewModel

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeAddVictimFragment(): AddVictimFragment

    @Binds
    @IntoMap
    @ViewModelKey(AddVictimViewModel::class)
    abstract fun bindAddVictimViewModel(viewModel: AddVictimViewModel): ViewModel

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeAddWitnessFragment(): AddWitnessFragment

    @Binds
    @IntoMap
    @ViewModelKey(AddWitnessViewModel::class)
    abstract fun bindAddWitnessViewModel(viewModel: AddWitnessViewModel): ViewModel

}
