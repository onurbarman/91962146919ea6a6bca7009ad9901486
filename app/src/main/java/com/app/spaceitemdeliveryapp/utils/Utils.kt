package com.app.spaceitemdeliveryapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast
import com.app.spaceitemdeliveryapp.data.remote.Resource
import retrofit2.Response
import kotlin.math.pow
import kotlin.math.sqrt

object Utils {
    fun showToast(context : Context, message : String)
    {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
    }

    fun setIsPlanetCalled(context: Context, isPlanetCalled: Boolean){
        val prefences = context.getSharedPreferences(Constants.PREFS_FILENAME, Context.MODE_PRIVATE)
        val editor = prefences.edit()
        editor.putBoolean(Constants.KEY_PLANET_CALLED, isPlanetCalled)
        editor.apply()
    }

    fun getIsPlanetCalled(context: Context) : Boolean
    {
        val preferences = context.getSharedPreferences(Constants.PREFS_FILENAME, Context.MODE_PRIVATE)
        return preferences.getBoolean(Constants.KEY_PLANET_CALLED, false)
    }

    fun setIsSpacecraftCreated(context: Context, isSpaceshipCreated: Boolean){
        val prefences = context.getSharedPreferences(Constants.PREFS_FILENAME, Context.MODE_PRIVATE)
        val editor = prefences.edit()
        editor.putBoolean(Constants.KEY_SPACESHIP_CREATED, isSpaceshipCreated)
        editor.apply()
    }

    fun getIsSpacecraftCreated(context: Context) : Boolean
    {
        val preferences = context.getSharedPreferences(Constants.PREFS_FILENAME, Context.MODE_PRIVATE)
        return preferences.getBoolean(Constants.KEY_SPACESHIP_CREATED, false)
    }

    fun setSpacecraftId(context: Context, spaceshipId: Long){
        val prefences = context.getSharedPreferences(Constants.PREFS_FILENAME, Context.MODE_PRIVATE)
        val editor = prefences.edit()
        editor.putLong(Constants.KEY_SPACESHIP_ID, spaceshipId)
        editor.apply()
    }

    fun getSpacecraftId(context: Context) : Long
    {
        val preferences = context.getSharedPreferences(Constants.PREFS_FILENAME, Context.MODE_PRIVATE)
        return preferences.getLong(Constants.KEY_SPACESHIP_ID,-1)
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }

    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): Resource<T> {
        return try {
            val myResp = call.invoke()
            if (myResp.isSuccessful) {
                Resource.success(myResp.body()!!)
            } else {
                Resource.error(myResp.errorBody()?.string() ?: "Something goes wrong")
            }

        } catch (e: Exception) {
            Resource.error(e.message ?: "Internet error runs")
        }
    }

    fun calculateDistance(x1: Double, x2: Double, y1: Double, y2: Double): Int {
        return sqrt( (x2 - x1).pow(2.0) + (y2 - y1).pow(2.0)).toInt()
    }
}