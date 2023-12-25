package com.example.muharya_pengingatjadwalkeretaapi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.muharya_pengingatjadwalkeretaapi.R
import com.example.muharya_pengingatjadwalkeretaapi.data.model.TiketModel
import com.example.muharya_pengingatjadwalkeretaapi.utils.TanggalDanWaktu
import java.text.SimpleDateFormat
import java.util.Calendar

class AdminListTanggalTiketAdapter(private val listTanggalTiket: ArrayList<String>, val listener: clickListener):
    RecyclerView.Adapter<AdminListTanggalTiketAdapter.AdminListTanggalTiketViewHolder>() {
    class AdminListTanggalTiketViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvHari : TextView
        val tvTanggal : TextView
        val clBody : ConstraintLayout
        init {
            tvHari = v.findViewById(R.id.tvHari)
            tvTanggal = v.findViewById(R.id.tvTanggal)
            clBody = v.findViewById(R.id.clBody)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdminListTanggalTiketViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_admin_list_tiket, parent, false)
        return AdminListTanggalTiketViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listTanggalTiket.size
    }

    override fun onBindViewHolder(holder: AdminListTanggalTiketViewHolder, position: Int) {
        val tanggal = listTanggalTiket[position]
        val tanggalDanWaktu = TanggalDanWaktu()
        holder.apply {
            tvHari.text = ambilHari(tanggal)
            tvTanggal.text = tanggalDanWaktu.konversiBulan(tanggal)

            clBody.setOnClickListener {
                listener.onClickItemListener(listTanggalTiket[position], it)
            }
        }
    }

    interface clickListener{
        fun onClickItemListener(tanggal: String, it:View)
    }

    private fun ambilHari(tanggal: String): String{
        val daysArray = arrayOf("Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu")

        val dateTimeFormat = "yyyy-MM-dd"
        val formatter = SimpleDateFormat(dateTimeFormat)

        val valueTanggal = formatter.parse(tanggal)

        val calendar = Calendar.getInstance()
        calendar.time = valueTanggal

        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        val value = daysArray[dayOfWeek-1]
        return value
    }


}