package com.billioapp.util

import platform.posix.time
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
actual fun currentEpochMillis(): Long = (time(null).toLong() * 1000L)
