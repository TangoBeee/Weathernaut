package me.tangobee.weathernaut.util

import android.content.Context

class CountryNameByCode {

    companion object {
        fun getCountryNameByCode(context: Context, countryCode: String) : String {
            val resourceName = "country_name_$countryCode"
            val countryNameID = context.resources.getIdentifier(resourceName, "string", context.packageName)

            if(countryNameID != 0) {
                return context.getString(countryNameID)
            }

            return countryCode
        }
    }

}