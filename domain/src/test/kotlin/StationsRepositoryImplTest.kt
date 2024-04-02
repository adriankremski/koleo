import app.cash.turbine.test
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.snuffix.domain.repository.CacheInfo
import com.github.snuffix.domain.repository.DistanceMatrixSource
import com.github.snuffix.domain.repository.LocalKeywordsSource
import com.github.snuffix.domain.repository.StationsRepositoryImpl
import com.github.snuffix.domain.repository.LocalStationsSource
import com.github.snuffix.domain.repository.RemoteKeywordsSource
import com.github.snuffix.domain.repository.RemoteStationsSource
import com.github.snuffix.domain.repository.Station
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito.anyList
import java.io.IOException
import java.util.UUID
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class StationsRepositoryImplTest {

    private val remoteStationsSource: RemoteStationsSource = mock()
    private val localStationsSource: LocalStationsSource = mock()
    private val remoteKeywordsSource: RemoteKeywordsSource = mock()
    private val localKeywordsSource: LocalKeywordsSource = mock()
    private val stationsCacheInfo: CacheInfo = mock()
    private val keywordsCacheInfo: CacheInfo = mock()
    private val distanceMatrixSource: DistanceMatrixSource = mock()

    private val repository: StationsRepositoryImpl =
        StationsRepositoryImpl(
            remoteStationsSource = remoteStationsSource,
            localStationsSource = localStationsSource,
            remoteKeywordsSource = remoteKeywordsSource,
            localKeywordsSource = localKeywordsSource,
            stationsCacheInfo = stationsCacheInfo,
            keywordsCacheInfo = keywordsCacheInfo,
            distanceMatrixSource = distanceMatrixSource
        )

    @Test
    fun `getItems should emit error if it occurs when fetching stations`() =
        runBlocking {
            val error = IOException("Network error")

            `when`(localStationsSource.getItems()).thenReturn(emptyList())
            `when`(localKeywordsSource.getItems()).thenReturn(emptyList())
            `when`(stationsCacheInfo.isExpired()).thenReturn(true)
            `when`(remoteStationsSource.getItems()).thenAnswer { throw error }

            val resultFlow = repository.searchForStation("")

            resultFlow.test {
                assertEquals(Err(com.github.snuffix.domain.repository.RepositoryError.GetStationsItemsError), awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun `getItems should emit error if it occurs when fetching keywords`() =
        runBlocking {
            val error = IOException("Network error")

            `when`(localStationsSource.getItems()).thenReturn(listOf(stationItem()))
            `when`(localKeywordsSource.getItems()).thenReturn(emptyList())
            `when`(stationsCacheInfo.isExpired()).thenReturn(false)
            `when`(keywordsCacheInfo.isExpired()).thenReturn(true)
            `when`(remoteKeywordsSource.getItems()).thenAnswer { throw error }

            val resultFlow = repository.searchForStation("")

            resultFlow.test {
                assertEquals(Err(com.github.snuffix.domain.repository.RepositoryError.GetStationsItemsError), awaitItem())
                awaitComplete()
            }
        }

    @Test
    fun `getItems should not start requests if cached data is valid`() =
        runBlocking {
            `when`(localStationsSource.getItems()).thenReturn(listOf(stationItem()))
            `when`(localKeywordsSource.getItems()).thenReturn(listOf(keywordItem()))

            `when`(stationsCacheInfo.isExpired()).thenReturn(false)
            `when`(keywordsCacheInfo.isExpired()).thenReturn(false)

            val resultFlow = repository.searchForStation("")

            resultFlow.test {
                assertEquals(Ok(emptyList<Station>()), awaitItem())
                awaitComplete()
                verify(remoteStationsSource, times(0)).getItems()
                verify(remoteKeywordsSource, times(0)).getItems()
            }
        }

    @Test
    fun `getItems should request new data if cached data is expired`() =
        runBlocking {
            `when`(localStationsSource.getItems()).thenReturn(listOf(stationItem()))
            `when`(localStationsSource.replaceItems(anyList())).thenReturn(listOf(stationItem()))
            `when`(localKeywordsSource.getItems()).thenReturn(listOf(keywordItem()))
            `when`(localKeywordsSource.replaceItems(anyList())).thenReturn(listOf(keywordItem()))

            `when`(remoteStationsSource.getItems()).thenReturn(listOf(stationItem()))
            `when`(remoteKeywordsSource.getItems()).thenReturn(listOf(keywordItem()))

            `when`(stationsCacheInfo.isExpired()).thenReturn(true)
            `when`(keywordsCacheInfo.isExpired()).thenReturn(true)

            val resultFlow = repository.searchForStation("")

            resultFlow.test {
                assertEquals(Ok(emptyList<Station>()), awaitItem())
                awaitComplete()
                verify(remoteStationsSource, times(1)).getItems()
                verify(remoteKeywordsSource, times(1)).getItems()
            }
        }
}

fun stationItem() = Station(
    id = randomId(),
    name = randomId(),
    latitude = 0.0,
    longitude = 0.0,
    hits = 0L,
    city = "",
    country = "",
    stationId = "",
    region = ""
)

fun keywordItem() = com.github.snuffix.domain.repository.StationKeyword(
    id = randomId(),
    keyword = randomId(),
    stationId = randomId()
)

private fun randomId() = UUID.randomUUID().toString()