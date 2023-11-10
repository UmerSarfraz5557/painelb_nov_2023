package br.com.painelb.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import br.com.painelb.util.sharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface PreferenceStorage {
    var main: Boolean
    var login: Boolean
    var token: String
    var name : String
    var email: String
    var userId: Long
    var level : Int
}

class SharedPreferenceStorage(context: Context) : PreferenceStorage {

    private val prefs: Lazy<SharedPreferences> = lazy {
        context.sharedPreferences(PREFS_NAME)
    }
    private val observableSelectedThemeResult = MutableLiveData<String>()

    override var main by BooleanPreference(prefs, PREF_PWD)
    override var login by BooleanPreference(prefs, PREF_LOGIN)
    override var token by StringPreference(prefs, PREF_TOKEN, "")
    override var name by StringPreference(prefs, PREF_NAME, "")
    override var email by StringPreference(prefs, PREF_EMAIL, "")
    override var userId by LongPreference(prefs, PREF_USER_ID, 0)
    override var level by IntPreference(prefs, PREF_Level,0)

    companion object {
        const val PREFS_NAME = "painelb"
        const val PREF_PWD = "pref_pwd"
        const val PREF_LOGIN = "pref_login"
        const val PREF_TOKEN = "pref_token"
        const val PREF_NAME = "pref_name"
        const val PREF_EMAIL = "pref_email"
        const val PREF_USER_ID = "pref_user_id"
        const val PREF_Level = "pref_level"
    }
}

class BooleanPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Boolean = false
) : ReadWriteProperty<Any, Boolean> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        return preferences.value.getBoolean(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        preferences.value.edit { putBoolean(name, value) }
    }
}

class StringPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: String
) : ReadWriteProperty<Any, String?> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): String {
        return preferences.value.getString(name, defaultValue) ?: defaultValue
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
        preferences.value.edit { putString(name, value) }
    }
}

class IntPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Int
) : ReadWriteProperty<Any, Int> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return preferences.value.getInt(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        preferences.value.edit { putInt(name, value) }
    }
}

class LongPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Long
) : ReadWriteProperty<Any, Long> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Long {
        return preferences.value.getLong(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Long) {
        preferences.value.edit { putLong(name, value) }
    }
}

