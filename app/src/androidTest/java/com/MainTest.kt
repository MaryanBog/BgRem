package com

import com.bgrem.app.di.KoinModules
import com.bgrem.domain.localstorage.LocalDataStorage
import com.bgrem.domain.task.RemoveBackgroundInteractor
import com.bgrem.domain.task.RemoveBackgroundInteractorImpl
import com.bgrem.domain.task.TaskDataSource
import org.junit.Rule
import org.junit.Test
import org.koin.core.KoinApplication
import org.koin.dsl.koinApplication
import org.koin.dsl.module
//import org.koin.test.KoinTest
//import org.koin.test.KoinTestRule
//import org.koin.test.check.checkModules
//import org.koin.test.inject
//import org.koin.test.mock.MockProviderRule
//import org.mockito.Mockito

//class MainTest: KoinTest {
//
//    @get:Rule
//    val koinTestRule = KoinTestRule.create {
//        modules(KoinModules.all)
//    }
//
//    @Test
//    fun verifyKoinApp() {
//        koinApplication {
//            modules(KoinModules.all)
//            checkModules()
//        }
//    }

//    private val localDataStorage: LocalDataStorage by inject()
//    private val taskDataSource: TaskDataSource by inject()
//
//    @get:Rule
//    val koinTestRule = KoinTestRule.create {
//        modules(
//            module {
//                factory<RemoveBackgroundInteractor> {
//                    RemoveBackgroundInteractorImpl(
//                        localDataStorage, taskDataSource
//                    )
//                }
//            }
//        )
//    }
//
//    @get:Rule
//    val mockProvider = MockProviderRule.create { clazz ->
//        Mockito.mock(clazz.java)
//    }
//
//    @Test
//    fun checkingModule(){
//        checkModules {
//            modules(
//                module {
//                    factory<RemoveBackgroundInteractor> {
//                        RemoveBackgroundInteractorImpl(
//                            localDataStorage, taskDataSource
//                        )
//                    }
//                }
//            )
//        }
//    }

//}