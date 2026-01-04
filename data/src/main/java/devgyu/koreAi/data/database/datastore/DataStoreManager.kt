package devgyu.koreAi.data.database.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import devgyu.koreAi.domain.util.JsonUtil.fromJson
import devgyu.koreAi.domain.util.JsonUtil.toJson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class DataStoreManager(myDataStore: DataStore<Preferences>) {
    val dataStore = myDataStore

    /**
     * @param key [DataStoreKeys] 에서 정의해서 쓰기
     * @param data Edit 할 데이터를 넣어주기
     * */
    suspend inline fun <reified T> getValue(key: String): T? {
        return when (T::class.simpleName) {
            String::class.simpleName -> {
                val prefKey = stringPreferencesKey(key)
                dataStore.data.firstOrNull()?.get(prefKey) as T?
            }

            Int::class.simpleName -> {
                val prefKey = intPreferencesKey(key)
                dataStore.data.firstOrNull()?.get(prefKey) as T?
            }

            Long::class.simpleName -> {
                val prefKey = longPreferencesKey(key)
                dataStore.data.firstOrNull()?.get(prefKey) as T?
            }

            Double::class.simpleName -> {
                val prefKey = doublePreferencesKey(key)
                dataStore.data.firstOrNull()?.get(prefKey) as T?
            }

            Float::class.simpleName -> {
                val prefKey = floatPreferencesKey(key)
                dataStore.data.firstOrNull()?.get(prefKey) as T?
            }

            Boolean::class.simpleName -> {
                val prefKey = booleanPreferencesKey(key)
                dataStore.data.firstOrNull()?.get(prefKey) as T?
            }

            else -> {
                val prefKey = stringPreferencesKey(key)
                val value = dataStore.data.firstOrNull()?.get(prefKey) ?: return null
                value.fromJson<T>()
            }
        }
    }

    /**
     * 값을 가져온 뒤 삭제
     * */
    suspend inline fun <reified T> getValueAndDelete(key: String): T? {
        val value = getValue<T>(key)
        clear<T>(key)
        return value
    }

    suspend inline fun <reified T> edit(key: String, data: T) {
        dataStore.edit { preferences ->
            when (T::class.simpleName) {
                String::class.simpleName -> {
                    val prefKey = stringPreferencesKey(key)
                    preferences[prefKey] = data as String
                }

                Int::class.simpleName -> {
                    val prefKey = intPreferencesKey(key)
                    preferences[prefKey] = data as Int
                }

                Long::class.simpleName -> {
                    val prefKey = longPreferencesKey(key)
                    preferences[prefKey] = data as Long
                }

                Double::class.simpleName -> {
                    val prefKey = doublePreferencesKey(key)
                    preferences[prefKey] = data as Double
                }

                Float::class.simpleName -> {
                    val prefKey = floatPreferencesKey(key)
                    preferences[prefKey] = data as Float
                }

                Boolean::class.simpleName -> {
                    val prefKey = booleanPreferencesKey(key)
                    preferences[prefKey] = data as Boolean
                }

                else -> {
                    val prefKey = stringPreferencesKey(key)
                    preferences[prefKey] = data.toJson()
                }
            }
        }
    }

    inline fun <reified T> getFlow(key: String, defaultValue: T?): Flow<T?> {
        return when (T::class.simpleName) {
            String::class.simpleName -> {
                val prefKey = stringPreferencesKey(key)
                dataStore.data.map {
                    it[prefKey] ?: defaultValue
                } as Flow<T?>
            }

            Int::class.simpleName -> {
                val prefKey = intPreferencesKey(key)
                dataStore.data.map {
                    it[prefKey] ?: defaultValue
                } as Flow<T?>
            }

            Long::class.simpleName -> {
                val prefKey = longPreferencesKey(key)
                dataStore.data.map {
                    it[prefKey] ?: defaultValue
                } as Flow<T?>
            }

            Double::class.simpleName -> {
                val prefKey = doublePreferencesKey(key)
                dataStore.data.map {
                    it[prefKey] ?: defaultValue
                } as Flow<T?>
            }

            Float::class.simpleName -> {
                val prefKey = floatPreferencesKey(key)
                dataStore.data.map {
                    it[prefKey] ?: defaultValue
                } as Flow<T?>
            }

            Boolean::class.simpleName -> {
                val prefKey = booleanPreferencesKey(key)
                dataStore.data.map {
                    it[prefKey] ?: defaultValue
                } as Flow<T?>
            }

            else -> {
                val prefKey = stringPreferencesKey(key)
                dataStore.data.map {
                    when {
                        (it[prefKey] ?: defaultValue) == null -> null
                        it[prefKey] != null -> it[prefKey]!!.fromJson<T>()
                        else -> defaultValue.toJson().fromJson()
                    }
                }
            }
        }
    }

    suspend fun clearAll() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend inline fun <reified T> clear(key: String) {
        dataStore.edit { preferences ->
            when (T::class.simpleName) {
                String::class.simpleName -> {
                    val prefKey = stringPreferencesKey(key)
                    preferences.remove(prefKey)
                }

                Int::class.simpleName -> {
                    val prefKey = intPreferencesKey(key)
                    preferences.remove(prefKey)
                }

                Long::class.simpleName -> {
                    val prefKey = longPreferencesKey(key)
                    preferences.remove(prefKey)
                }

                Double::class.simpleName -> {
                    val prefKey = doublePreferencesKey(key)
                    preferences.remove(prefKey)
                }

                Float::class.simpleName -> {
                    val prefKey = floatPreferencesKey(key)
                    preferences.remove(prefKey)
                }

                Boolean::class.simpleName -> {
                    val prefKey = booleanPreferencesKey(key)
                    preferences.remove(prefKey)
                }

                else -> {
                    val prefKey = stringPreferencesKey(key)
                    preferences.remove(prefKey)
                }
            }
        }
    }
}
