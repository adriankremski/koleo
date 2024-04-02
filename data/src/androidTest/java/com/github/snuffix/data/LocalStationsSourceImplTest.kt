package com.github.snuffix.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.snuffix.data.local.source.LocalStationsSourceImpl
import com.github.snuffix.data.local.StationsDatabase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class LocalStationsSourceImplTest {

    private lateinit var database: StationsDatabase
    private lateinit var localSource: LocalStationsSourceImpl

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            StationsDatabase::class.java
        ).allowMainThreadQueries().build()

        localSource = LocalStationsSourceImpl(database.stationsDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun getItemsShouldReturnNoItemsInitially() = runBlocking {
        val items = localSource.getItems()

        assertEquals(0, items.size)
    }

    @Test
    fun replaceItemsShouldRemoveOldItems() = runBlocking {
        val itemsToInsert = listOf(stationItem(), stationItem(), stationItem())

        localSource.replaceItems(List(10) { stationItem() })
        localSource.replaceItems(itemsToInsert)
        val retrievedItems = localSource.getItems()

        assertEquals(itemsToInsert.size, retrievedItems.size)
        assertEquals(itemsToInsert, retrievedItems)
    }
}

fun stationItem() = com.github.snuffix.domain.repository.Station(
    id = randomId(),
    stationId = randomId(),
    name = randomId(),
    latitude = 0.0,
    longitude = 0.0,
    hits = 0,
    city = "",
    country = "",
    region = "",
)

private fun randomId() = UUID.randomUUID().toString()
