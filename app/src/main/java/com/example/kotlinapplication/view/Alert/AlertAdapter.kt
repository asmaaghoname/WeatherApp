package com.example.kotlinapplication.view.Alert

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinapplication.R
import com.example.kotlinapplication.model.roomDataSource.AlertEntity
import com.example.kotlinapplication.viewModel.AlertViewModel

class AlertAdapter class AlertRecyclerViewAdapter(var data: ArrayList<AlertEntity>): RecyclerView.Adapter<AlertRecyclerViewAdapter.AlertViewHolder>() {
    lateinit var context: Context
    lateinit var viewModel: AlertViewModel

    fun updateList(newList: List<AlertEntity>, viewModel: AlertViewModel) {
        this.viewModel = viewModel

        data.clear()
        data.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        context = parent.context
        return AlertViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.alert_single_row, parent, false))
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        holder.bind(data[position], this)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    class AlertViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val alertTimeLbl = view.findViewById<TextView>(R.id.alertTimeLbl)
        private val alertDayLbl = view.findViewById<TextView>(R.id.alertDayLbl)
        private val alertEventLbl = view.findViewById<TextView>(R.id.alertEventLbl)
        private val alertTypeLbl = view.findViewById<TextView>(R.id.alertTypeLbl)
        private val alertSwitch = view.findViewById<Switch>(R.id.alertSwitch)
        private val deleteButton = view.findViewById<Button>(R.id.deleteButton)


        @SuppressLint("SetTextI18n")
        fun bind(alert: AlertEntity, adapter: AlertRecyclerViewAdapter) {
            alertTimeLbl.text = alert.alertTime
            alertEventLbl.text = alert.alertEvent
            alertTypeLbl.text = alert.alertType
            alertSwitch.isChecked = alert.enabled

            //val str = StringBuilder()
            if(alert.alertDay  == "ALL"){
                alertDayLbl.text = "All Days"
            }else if (alert.alertDay == "WEEKEND"){
                alertDayLbl.text = "Weekend"
            }else if (alert.alertDay == "NONE"){
                alertDayLbl.text = "No Days"
            }else{

                val pref = PreferenceManager.getDefaultSharedPreferences(adapter.context.applicationContext)
                val language = pref.getString("language", "en")!!

                if (language == "en"){
                    alertDayLbl.text = alert.alertDay
                }else{
                    alertDayLbl.text = alert.alertDayAr
                }
            }

            alertSwitch.setOnClickListener {
                alert.enabled = alertSwitch.isChecked
                adapter.viewModel.insert(alert)
                true
            }


            deleteButton.setOnClickListener {
                val builder = AlertDialog.Builder(adapter.context)

                val pref = PreferenceManager.getDefaultSharedPreferences(adapter.context.applicationContext)
                val language = pref.getString("language", "en")!!

                if (language == "en"){
                    builder.setTitle("Warning")
                    builder.setMessage("Are you sure you want delete this?")


                    builder.setPositiveButton("Yes") { _, _ ->
                        adapter.viewModel.delete(alert)
                        adapter.data.remove(alert)
                        adapter.notifyDataSetChanged()
                    }

                    builder.setNegativeButton("No", null)
                    builder.show()
                }else {
                    builder.setTitle("تنبيه")
                    builder.setMessage("هل تريد حذف هذا المنبه؟")



                    builder.setPositiveButton("تأكيد") { _, _ ->
                        adapter.viewModel.delete(alert)
                        adapter.data.remove(alert)
                        adapter.notifyDataSetChanged()
                    }

                    builder.setNegativeButton("إلغاء", null)
                    builder.show()

                }
            }
        }
    }
}