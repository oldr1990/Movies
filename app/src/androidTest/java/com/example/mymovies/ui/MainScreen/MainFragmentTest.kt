package com.example.mymovies.ui.MainScreen

import androidx.test.espresso.Espresso.pressBack
import androidx.test.filters.MediumTest
import com.example.mymovies.launchFragmentHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class MainFragmentTest{
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp(){
        hiltRule.inject()
    }

    @After
    fun cleanUp(){}

    @Test
    fun clickSearchButtonWithCorrectInput(){
    launchFragmentHiltContainer<MainFragment> {
    }
        pressBack()
    }
}