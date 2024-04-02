package com.github.snuffix.domain.repository

interface CacheInfo {
    fun isExpired(): Boolean
    fun setCacheValid()

}