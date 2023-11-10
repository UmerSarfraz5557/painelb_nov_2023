package br.com.painelb.util

import android.annotation.SuppressLint
import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

fun Context.getDateFormat(millisecond: Long): String {
    val calendar = Calendar.getInstance(locale)
    calendar.timeInMillis = millisecond
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH).plus(1)
    val year = calendar.get(Calendar.YEAR)
    //return String.format(locale, "%04d-%02d-%02d", year, month, day)
    return String.format(locale, "%02d/%02d/%04d", day,month,year )
}

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@SuppressLint("SimpleDateFormat")
fun Context.getTimeFormat(hour: Int, minute: Int): String {
//    val format24 = SimpleDateFormat("HH:mm")
//    val format12 = SimpleDateFormat("hh:mm a")
//    val date: Date = format24.parse("$hour:$minute")
    return "$hour:$minute"
}

