package com.mohsen.apk.wetterkleidung.model

data class City(
    var name: String = "",
    var temp: String = "",
    var tempDesc: String = "",
    var tempIconId: String = "",
    var isDefault: Boolean = false
)