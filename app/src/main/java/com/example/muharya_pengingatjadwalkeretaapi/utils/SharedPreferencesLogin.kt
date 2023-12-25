package com.example.muharya_pengingatjadwalkeretaapi.utils

import android.content.Context

class SharedPreferencesLogin(val context: Context) {
    val keyIdUser = "keyIdUser"
    val keyNama = "keyNama"
    val keyAlamat = "keyAlamat"
    val keyNomorHp = "keyNomorHp"
    val keyUsername = "keyUsername"
    val keyPassword = "keyPassword"
    val keySebagai = "keySebagai"

    var sharedPref = context.getSharedPreferences("sharedpreference_login", Context.MODE_PRIVATE)
    var editPref = sharedPref.edit()

    fun setLogin(id_user:Int, nama:String, alamat:String, nomorHp:String, username:String, password:String, sebagai:String){
        editPref.apply{
            putInt(keyIdUser, id_user)
            putString(keyNama, nama)
            putString(keyAlamat, alamat)
            putString(keyNomorHp, nomorHp)
            putString(keyUsername, username)
            putString(keyPassword, password)
            putString(keySebagai, sebagai)
            apply()
        }
    }

    fun getId(): Int{
        val id_user = sharedPref.getInt(keyIdUser, 0)
        return id_user
    }
    fun getNama(): String{
        return sharedPref.getString(keyNama, "").toString()
    }
    fun getAlamat(): String{
        return sharedPref.getString(keyAlamat, "").toString()
    }
    fun getNomorHp(): String{
        return sharedPref.getString(keyNomorHp, "").toString()
    }
    fun getUsername():String{
        return sharedPref.getString(keyUsername, "").toString()
    }
    fun getPassword(): String{
        return sharedPref.getString(keyPassword, "").toString()
    }
    fun getSebagai(): String{
        return sharedPref.getString(keySebagai, "").toString()
    }
}