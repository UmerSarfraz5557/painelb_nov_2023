package br.com.painelb.ui.authentication.fragments.forgetpassword

import br.com.painelb.R
import br.com.painelb.base.ui.BaseFragment
import br.com.painelb.databinding.FragmentForgetPasswordBinding


class ForgetPasswordFragment :
    BaseFragment<FragmentForgetPasswordBinding>(R.layout.fragment_forget_password) {

    override fun haveToolbar() = true
    override fun resToolbarId() = R.id.toolbar
}