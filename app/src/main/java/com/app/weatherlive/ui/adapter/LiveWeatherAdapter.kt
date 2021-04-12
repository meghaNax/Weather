package com.app.weatherlive.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.weatherlive.R
import com.app.weatherlive.data.remote.beans.Hourly
import com.app.weatherlive.databinding.AdapterWeatherRecyclerViewBinding
import java.text.SimpleDateFormat
import java.util.*


class LiveWeatherAdapter constructor(private val context: Context) :


    RecyclerView.Adapter<LiveWeatherAdapter.LiveWeatherViewHolder>() {

    val hourlyData: ArrayList<Hourly> = ArrayList<Hourly>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiveWeatherViewHolder {
        val inflater = LayoutInflater.from(context)
        return LiveWeatherViewHolder(AdapterWeatherRecyclerViewBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holderLive: LiveWeatherViewHolder, position: Int) {
        holderLive.bind()
    }

    override fun getItemCount(): Int {
        return run {
            val size = (hourlyData.size / 2) - 1
            if (size > 0) size else 0
        }
    }

    inner class LiveWeatherViewHolder(private val binding: AdapterWeatherRecyclerViewBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(){
            hourlyData[adapterPosition].let { item->
                binding.temperatureToday.text =
                    binding.root.context.getString(R.string.txt_temp, item.temp.toString())
                binding.humidityToday.text = item.humidity.toString()
                binding.windspeedToday.text =item.windSpeed.toString()
                binding.hourTime.text =item.dt?.let { getDateTime(it) }
            }
        }
    }

    private fun getDateTime(timeStamp: Int): String {
        return try {
            val date = Date(timeStamp * 1000L)
            val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.ROOT)
            dateFormat.timeZone = TimeZone.getTimeZone("IST")
            dateFormat.format(date)
        } catch (e: Exception) {
            e.toString()
        }
    }

    fun updateList(updatedList: List<Hourly>) {
        hourlyData.clear()
        hourlyData.addAll(updatedList)
        notifyDataSetChanged()
    }


}