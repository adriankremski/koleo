package com.github.snuffix.data.network.cache

import android.content.Context
import androidx.core.content.edit
import com.github.snuffix.domain.repository.CacheInfo
import java.time.Instant

const val KEY_CACHE_TIME = "cache_time"

abstract class BaseCacheInfo(context: Context, prefName: String) : CacheInfo {

    private val preferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

    override fun setCacheValid() {
        preferences.edit {
            putLong(KEY_CACHE_TIME, Instant.now().toEpochMilli())
        }
    }

    override fun isExpired(): Boolean {
        val storedTime = preferences.getLong(KEY_CACHE_TIME, 0)

        return if (storedTime == 0L) {
            true
        } else {
            val currentTime = Instant.now().toEpochMilli()
            currentTime - storedTime >= 24 * 60 * 60 * 1000 // 24 hours in milliseconds
        }
    }
}