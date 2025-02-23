package com.example.labs.data.lab4

import android.content.Context
import android.content.SharedPreferences
import java.math.BigInteger

class KeyStorage(val context: Context) {

//    private val sharedPreferences: SharedPreferences =
//        context.getSharedPreferences("RSA_KEYS", Context.MODE_PRIVATE)
//
//    fun saveKeys(n: BigInteger, e: BigInteger, d: BigInteger) {
//        val editor = sharedPreferences.edit()
//        editor.putString("n", n.toString())
//        editor.putString("e", e.toString())
//        editor.putString("d", d.toString())
//        editor.apply()
//    }
//    fun getPublicKey(): Pair<BigInteger, BigInteger>? {
//        val n = sharedPreferences.getString("n", null)?.toBigInteger()
//        val e = sharedPreferences.getString("e", null)?.toBigInteger()
//        return if (n != null && e != null) Pair(n, e) else null
//    }
//
//    fun getPrivateKey(): Pair<BigInteger, BigInteger>? {
//        val n = sharedPreferences.getString("n", null)?.toBigInteger()
//        val d = sharedPreferences.getString("d", null)?.toBigInteger()
//        return if (n != null && d != null) Pair(n, d) else null
//    }
}