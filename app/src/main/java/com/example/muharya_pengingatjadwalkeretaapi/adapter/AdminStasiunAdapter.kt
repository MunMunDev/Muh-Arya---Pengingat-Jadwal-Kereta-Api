package com.example.muharya_pengingatjadwalkeretaapi.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muharya_pengingatjadwalkeretaapi.R
import com.example.muharya_pengingatjadwalkeretaapi.data.model.StasiunModel

class AdminStasiunAdapter(val arrayStasiun: ArrayList<StasiunModel>, val listener: ClickListener): RecyclerView.Adapter<AdminStasiunAdapter.AdminStasiunViewHolder>() {
    class AdminStasiunViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvNo: TextView
        val tvNamaStasiun: TextView
        val tvKotaKab: TextView
        val tvSetting: TextView

        init {
            tvNo = v.findViewById(R.id.tvNo)
            tvNamaStasiun = v.findViewById(R.id.tvNamaStasiun)
            tvKotaKab = v.findViewById(R.id.tvKotaKab)
            tvSetting = v.findViewById(R.id.tvSetting)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminStasiunViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_admin_stasiun_table, parent, false)
        return AdminStasiunViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayStasiun.size+1
    }

    override fun onBindViewHolder(holder: AdminStasiunViewHolder, position: Int) {
        holder.apply {
            if(position==0){
                tvNo.text = "NO"
                tvNamaStasiun.text = "Nama Stasiun"
                tvKotaKab.text = "Nama Kota/Kab"
                tvSetting.text = ""

                tvNo.setBackgroundResource(R.drawable.bg_table_title)
                tvNamaStasiun.setBackgroundResource(R.drawable.bg_table_title)
                tvKotaKab.setBackgroundResource(R.drawable.bg_table_title)
                tvSetting.setBackgroundResource(R.drawable.bg_table_title)

                tvNo.setTextColor(Color.parseColor("#ffffff"))
                tvNamaStasiun.setTextColor(Color.parseColor("#ffffff"))
                tvKotaKab.setTextColor(Color.parseColor("#ffffff"))
                tvSetting.setTextColor(Color.parseColor("#ffffff"))

                tvNo.setTypeface(null, Typeface.BOLD)
                tvNamaStasiun.setTypeface(null, Typeface.BOLD)
                tvKotaKab.setTypeface(null, Typeface.BOLD)
                tvSetting.setTypeface(null, Typeface.BOLD)
            }
            else{
                val stasiun = arrayStasiun[(position-1)]

                tvNo.text = "$position"
                tvNamaStasiun.text = stasiun.nama_stasiun
                tvKotaKab.text = stasiun.kota_kab
                tvSetting.text = ":::"

                tvNo.setBackgroundResource(R.drawable.bg_table)
                tvNamaStasiun.setBackgroundResource(R.drawable.bg_table)
                tvKotaKab.setBackgroundResource(R.drawable.bg_table)
                tvSetting.setBackgroundResource(R.drawable.bg_table)

                tvNo.setTextColor(Color.parseColor("#000000"))
                tvNamaStasiun.setTextColor(Color.parseColor("#000000"))
                tvKotaKab.setTextColor(Color.parseColor("#000000"))
                tvSetting.setTextColor(Color.parseColor("#000000"))

                tvNo.setTypeface(null, Typeface.NORMAL)
                tvNamaStasiun.setTypeface(null, Typeface.NORMAL)
                tvKotaKab.setTypeface(null, Typeface.NORMAL)
                tvSetting.setTypeface(null, Typeface.NORMAL)

                tvSetting.setOnClickListener {
                    listener.onClick(stasiun, position, it)
                }
            }
        }
    }

    interface ClickListener{
        fun onClick(stasiun: StasiunModel, position: Int, it: View)
    }
}