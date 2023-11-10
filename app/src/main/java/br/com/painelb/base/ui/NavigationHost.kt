package br.com.painelb.base.ui

import androidx.appcompat.widget.Toolbar
import br.com.painelb.base.navigation.ActivityScreenSwitcher

interface NavigationHost {

    fun registerToolbarWithNavigation(toolbar: Toolbar)

    fun activityScreenSwitcher(): ActivityScreenSwitcher
}
