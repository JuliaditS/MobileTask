package com.codelabs.unikomradio.mvvm.programs

import timber.log.Timber
import java.util.*

object specifyToday{
    private const val SUNDAY = "minggu"
    private const val MONDAY = "senin"
    private const val TUESDAY = "selasa"
    private const val WEDNESDAY = "rabu"
    private const val THURSDAY = "kamis"
    private const val FRIDAY = "jum'at"
    private const val SATURDAY = "sabtu"

    private lateinit var today:String
    private var hour:Int
    private var minute:Int

    init {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_WEEK)
        hour = calendar.get(Calendar.HOUR_OF_DAY)
        minute = calendar.get(Calendar.MINUTE)

        when (day) {
            Calendar.SUNDAY -> {
                today = SUNDAY
            }
            Calendar.MONDAY -> {
                today = MONDAY
            }
            Calendar.TUESDAY -> {
                today = TUESDAY
            }
            Calendar.WEDNESDAY -> {
                today = WEDNESDAY
            }
            Calendar.THURSDAY -> {
                today = THURSDAY
            }
            Calendar.FRIDAY -> {
                today = FRIDAY
            }
            Calendar.SATURDAY -> {
                today = SATURDAY
            }
        }
    }

    fun isToday(day:String):Boolean{
        return day == today
    }

    fun isToday(startDay:String,endDay:String):Boolean{

        if ( ( getDayOrder(today) >= getDayOrder(startDay.toLowerCase()) ) &&
            ( getDayOrder(today) <= getDayOrder(endDay.toLowerCase()) )
        ) {
            return true
        }

        return false
    }

    private fun getDayOrder(day:String):Int{
        return when (day) {
            SUNDAY-> 0
            MONDAY-> 1
            TUESDAY-> 2
            WEDNESDAY-> 3
            THURSDAY-> 4
            FRIDAY-> 5
            SATURDAY-> 6
            else -> 0
        }
    }

    fun getHourSpecify():Double{
        return "$hour.$minute".toDouble()
    }

}