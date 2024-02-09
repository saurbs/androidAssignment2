package com.example.sauravgautam_assignment2

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.edit

data class ContactsData(val name: String, val num: String)

class DataStoreContacts(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("Contacts")

        val CONTACT_KEY = stringPreferencesKey("data")
    }

    fun getContactsData(): Flow<List<ContactsData>> {
        return context.dataStore.data.map { preferences ->
            val savedData = preferences[CONTACT_KEY]
            savedData?.split(",")?.mapNotNull { entry ->
                val (name, num) = entry.split(" ")
                ContactsData(name, num)
            } ?: emptyList()
        }
    }

    suspend fun saveContactsData(data: ContactsData) {
        context.dataStore.edit { preferences ->
            val existingData = preferences[CONTACT_KEY]?.split(",") ?: emptyList()
            val updatedData = existingData + "${data.name} ${data.num}"
            preferences[CONTACT_KEY] = updatedData.joinToString(",")
        }
    }

    suspend fun clearContactsData() {
        context.dataStore.edit { preferences ->
            preferences.remove(CONTACT_KEY)
        }
    }
}
