package com.mohsen.apk.wetterkleidung.network.di

import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.inject.Qualifier
import javax.inject.Scope

@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
annotation class HeaderParametersQualifier