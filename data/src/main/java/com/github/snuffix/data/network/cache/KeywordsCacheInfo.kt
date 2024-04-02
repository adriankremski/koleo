package com.github.snuffix.data.network.cache

import android.content.Context
import com.github.snuffix.domain.repository.Constants
import javax.inject.Inject

class KeywordsCacheInfo @Inject constructor(context: Context): BaseCacheInfo(context, Constants.KEY_KEYWORDS_CACHE)