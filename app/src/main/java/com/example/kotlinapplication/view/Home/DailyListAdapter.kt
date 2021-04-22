package com.example.kotlinapplication.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kotlinapplication.R
import com.example.kotlinapplication.model.Daily
import kotlinx.android.synthetic.main.daily_row.view.*
import java.text.SimpleDateFormat
import java.util.*

class DailyListAdapter(var dailyList: ArrayList<Daily>) : RecyclerView.Adapter<DailyListAdapter.DailyViewHolder>(){


    fun updateHoursList(newDailyList: List<Daily>) {
        dailyList.clear()
        dailyList.addAll(newDailyList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = DailyViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.daily_row, parent, false)
    )


    override fun getItemCount(): Int {
        return dailyList.size
    }

    override fun onBindViewHolder(holder: DailyListAdapter.DailyViewHolder, position: Int) {
        holder.bind(dailyList[position])
    }

    class DailyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val dt=itemView.txt_daily_dt
//        private val day=itemView.daily_day
        private val dailyDayIcon=itemView.dailyDay_icon
        private val dailyDayTemp=itemView.dailyDay_temp
        private val sunrise=itemView.daily_sunrise



        fun bind(daily: Daily){

            val calendar = Calendar.getInstance()
            val tz = TimeZone.getDefault()
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.timeInMillis))
            val sdf = SimpleDateFormat(" EE:dd:MM", Locale.getDefault())
            val currenTimeZone = (daily.dt?.toLong())?.times(1000)?.let { it1 -> Date(it1) }
            dt.text= sdf.format(currenTimeZone)
            dailyDayTemp.text=(daily.temp?.day)?.toInt().toString()
            sunrise.text=getDt(daily.sunset)
            val dayIcon=daily.weather?.get(0)?.icon
            val options = RequestOptions()
                .error(R.mipmap.ic_launcher_round)
            Glide.with(dailyDayIcon.context)
                .setDefaultRequestOptions(options)
                .load(dayIcon?.let { it2 -> getIcon(it2) })
                .into(dailyDayIcon)

            val nightIcon=daily.weather?.get(0)?.icon?.substring(0,2)+"n"
            //10d -- 10n

            Log.i("TAG", "bind: $nightIcon")
//            Glide.with(dailyNightIcon.context)
//                .setDefaultRequestOptions(options)
//                .load(nightIcon?.let { it3 -> getIcon(it3) })
//                .into(dailyNightIcon)

        }
        fun getIcon(icon: String): String {
            return "http://openweathermap.org/img/w/${icon}.png"
        }
        fun getDt(dt:Int?):String{
            val calendar = Calendar.getInstance()
            val tz = TimeZone.getDefault()
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.timeInMillis))
            val sdf = SimpleDateFormat("HH:mm a", Locale.getDefault())
            val currenTimeZone = (dt?.toLong())?.times(1000)?.let { it1 -> Date(it1) }
            return sdf.format(currenTimeZone)
        }
    }
}