package br.com.painelb.ui.main.fragments.occurrences.create.fragments.addvictim

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.painelb.R
import br.com.painelb.base.ui.BaseFragment
import br.com.painelb.databinding.FragmentAddVictimBinding
import br.com.painelb.di.modules.viemodel.AppViewModelFactory
import br.com.painelb.domain.result.EventObserver
import br.com.painelb.util.navigateUp
import br.com.painelb.util.viewModelProvider
import java.util.*
import javax.inject.Inject

class AddVictimFragment : BaseFragment<FragmentAddVictimBinding>(R.layout.fragment_add_victim) {
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    lateinit var viewModel: AddVictimViewModel

    private val args: AddVictimFragmentArgs by navArgs()

    override fun haveToolbar() = true
    override fun resToolbarId() = R.id.toolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = viewModelProvider(viewModelFactory)
        viewModel.occurrenceId = args.occurrenceId
        viewModel.setOccurrenceVictim(args.occurrenceVictim)
        mBinding.viewModel = viewModel
        val savedStateHandle = findNavController().previousBackStackEntry?.savedStateHandle
        viewModel.navigateToCreateVictim.observe(viewLifecycleOwner, EventObserver {
            savedStateHandle?.set(EXTRA_ARGUMENT_ADD_VICTIM, it)
            navigateUp()
        })
        viewModel.navigateToUpdateVictim.observe(viewLifecycleOwner, EventObserver {
            savedStateHandle?.set(EXTRA_ARGUMENT_UPDATE_VICTIM, it)
            navigateUp()
        })


        addVictimDateMask(mBinding.victiDateEditText)




    }

    companion object {
        const val EXTRA_ARGUMENT_ADD_VICTIM = "EXTRA_ARGUMENT_ADD_VICTIM"
        const val EXTRA_ARGUMENT_UPDATE_VICTIM = "EXTRA_ARGUMENT_UPDATE_VICTIM"
    }



    fun addVictimDateMask(input: EditText){

        val mDateEntryWatcher = object : TextWatcher {

            var edited = false
            val dividerCharacter = "/"

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (edited) {
                    edited = false
                    return
                }

                var working = getEditText()

                working = manageDateDivider(working, 2, start, before)
                working = manageDateDivider(working, 5, start, before)

                edited = true
                input.setText(working)
                input.setSelection(input.text.length)
            }

            private fun manageDateDivider(working: String, position : Int, start: Int, before: Int) : String{
                if (working.length == position) {
                    return if (before <= position && start < position)
                        working + dividerCharacter
                    else
                        working.dropLast(1)
                }
                return working
            }

            private fun getEditText() : String {
                return if (input.text.length >= 10)
                    input.text.toString().substring(0,10)
                else
                    input.text.toString()
            }

            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        }

        input.addTextChangedListener(mDateEntryWatcher)

    }

}