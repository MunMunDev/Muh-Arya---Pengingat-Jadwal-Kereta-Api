package com.example.muharya_pengingatjadwalkeretaapi.utils

class KataAcak {
    fun getHurufSaja(): String{
        val str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        var hurufAcak = "1"
        for(i in 1..15){
            hurufAcak+=str.random()
        }
        return hurufAcak
    }
    fun getHurufDanAngka(): String{
        val str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
        var hurufAcak = "1"
        for(i in 1..15){
            hurufAcak+=str.random()
        }
        return hurufAcak
    }
}