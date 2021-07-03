package com.example.mymovies.utils

import com.example.mymovies.util.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher

class TestDispatcherProvider:DispatcherProvider {
    override val main: CoroutineDispatcher
        get() = TestCoroutineDispatcher()
    override val io: CoroutineDispatcher
        get() = TestCoroutineDispatcher()
    override val default: CoroutineDispatcher
        get() = TestCoroutineDispatcher()
    override val unconfined: CoroutineDispatcher
        get() = TestCoroutineDispatcher()
}