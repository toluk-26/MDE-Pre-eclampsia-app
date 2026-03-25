@file:OptIn(ExperimentalUuidApi::class)

package com.example.pre_eclampsiascreener.ble.managers

import android.util.Log
import com.example.pre_eclampsiascreener.ble.Profile
import com.example.pre_eclampsiascreener.ble.ServiceManager
import com.example.pre_eclampsiascreener.ble.parsers.TimezoneParser
import com.example.pre_eclampsiascreener.ble.parsers.UnixTimeParser
import com.example.pre_eclampsiascreener.ble.repo.TimeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import no.nordicsemi.kotlin.ble.client.RemoteCharacteristic
import no.nordicsemi.kotlin.ble.client.RemoteService
import no.nordicsemi.kotlin.ble.core.WriteType
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

private const val TAG = "TimeManager"

internal class TimeManager : ServiceManager {
    private val UNIX_TIME_CHARACTERISTIC_UUID: Uuid =
        Uuid.parse("043f0001-0ff5-45d1-9502-db9d40757da2")
    private val TIMEZONE_CHARACTERISTIC_UUID: Uuid =
        Uuid.parse("043f0002-0ff5-45d1-9502-db9d40757da2")

    override val profile: Profile = Profile.TIME

    override suspend fun observeServiceInteractions(
        remoteService: RemoteService,
        scope: CoroutineScope
    ) {

        unixTimeCharacteristic = remoteService.characteristics.firstOrNull {
            it.uuid == UNIX_TIME_CHARACTERISTIC_UUID
        } ?: throw IllegalStateException("Time characteristic not found")
        try {
            unixTimeCharacteristic.subscribe()
                .mapNotNull { UnixTimeParser.parse(it) }
                .onEach { TimeRepository.updateTime(it) }
                .onCompletion { TimeRepository.clear() }
                .catch { e ->
                    Log.e(TAG, "Subscribe error: ${e.message}")
                }
                .launchIn(scope)

        } catch (e: Exception) {
            Log.e(TAG, "Subscribe error null: ${e.message}")
        }
        try {
            unixTimeCharacteristic
                .read()
                .let { Log.d(TAG, "read timeb: ${it.contentToString()}")
                    UnixTimeParser.parse(it) }
                ?.also {
                    Log.d(TAG, "read time: $it")
                    TimeRepository.updateTime(it) }
        } catch (e: Exception) {
            Log.e(TAG, "Read error: ${e.message}")
        }

        timezoneCharacteristic = remoteService.characteristics.firstOrNull {
            it.uuid == TIMEZONE_CHARACTERISTIC_UUID
        } ?: throw IllegalStateException("Timezone characteristic not found")
        try {
            timezoneCharacteristic
                .subscribe()
                .mapNotNull { TimezoneParser.parse(it) }
                .onEach { TimeRepository.updateTimezone(it) }
                .onCompletion { TimeRepository.clear() }
                .catch { e ->
                    Log.e(TAG, "Subscribe error: ${e.message}")
                }
                .launchIn(scope)
        } catch (e: Exception) {
            Log.e(TAG, "Subscribe error null: ${e.message}")
        }
        try {
            timezoneCharacteristic
                .read()
                .let { TimezoneParser.parse(it) }
                ?.also { Log.d(TAG, "read tz: $it")
                    TimeRepository.updateTimezone(it) }
        } catch (e: Exception) {
            Log.e(TAG, "Read error: ${e.message}")
        }
    }

    companion object {
        private lateinit var unixTimeCharacteristic: RemoteCharacteristic
        private lateinit var timezoneCharacteristic: RemoteCharacteristic

        suspend fun writeTime(value: Long) {
            val dataBytes = value.toString().toByteArray()
            try {
                if (::unixTimeCharacteristic.isInitialized) {
                    // Write WITHOUT response (fire-and-forget)
                    unixTimeCharacteristic.write(dataBytes, WriteType.WITHOUT_RESPONSE)

                    // — OR — Write WITH response (waits for ack, can throw on failure)
                    // myWriteCharacteristic.write(data, WriteType.WITH_RESPONSE)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Write error: ${e.message}")
            }
//            finally {
//                MyRepository.update(deviceId, value)
//            }
        }
    }
}