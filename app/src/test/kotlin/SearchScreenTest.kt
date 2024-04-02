import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.github.snuffix.koleo.KoleoTheme
import com.github.snuffix.koleo.search.SearchScreenContentPreview
import org.junit.Rule
import org.junit.Test

class SearchScreenTest {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        renderingMode = SessionParams.RenderingMode.NORMAL,
        showSystemUi = false,
        maxPercentDifference = 1.0,
    )

    @Test
    fun launch_search_screen() {
        paparazzi.snapshot {
            KoleoTheme {
                SearchScreenContentPreview(isLoading = false, displaySearchError = false)
            }
        }
    }

    @Test
    fun launch_search_screen_loading_state() {
        paparazzi.snapshot {
            KoleoTheme {
                SearchScreenContentPreview(isLoading = true, displaySearchError = false)
            }
        }
    }

    @Test
    fun launch_search_screen_error_state() {
        paparazzi.snapshot {
            KoleoTheme {
                SearchScreenContentPreview(isLoading = false, displaySearchError = true)
            }
        }
    }
}