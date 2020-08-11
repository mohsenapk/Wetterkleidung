package com.mohsen.apk.wetterkleidung.ui.dialog

interface FragmentDialogManager {
    fun openCityManagerDialog()
}

class FragmentDialogManagerImpl: FragmentDialogManager{
    override fun openCityManagerDialog() {
        CityManagerDialog().showsDialog
    }
}