package com.bgrem.domain.launch

import com.bgrem.domain.localstorage.LocalDataStorage
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock


class GetIsFirstLaunchUseCaseImplTest{

    val localDataStorage = mock<LocalDataStorage>()

    @Test
    suspend fun shouldReturnLocalDataStorage() {

        Mockito.`when`(localDataStorage.getIsFirstLaunch()).thenReturn(true)

        val useCase = GetIsFirstLaunchUseCaseImpl(localDataStorage)
        val actual = useCase.execute()
        val expected = true

        Assertions.assertEquals(expected, actual)
    }
}