package br.com.painelb.base.navigation

interface ScreenSwitcher<S : Screen> {
    fun open(mScreen: S)

    fun goBack()
}
