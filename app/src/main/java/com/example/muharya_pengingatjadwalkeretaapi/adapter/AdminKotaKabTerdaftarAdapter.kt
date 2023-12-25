package com.example.muharya_pengingatjadwalkeretaapi.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muharya_pengingatjadwalkeretaapi.R
import com.example.muharya_pengingatjadwalkeretaapi.data.model.KotaKabModel
import com.example.muharya_pengingatjadwalkeretaapi.ui.activity.admin.AdminKotaKabTerdaftar

class AdminKotaKabTerdaftarAdapter(val arrayKotaKab: ArrayList<KotaKabModel>, val listener: ClickListener):
    RecyclerView.Adapter<AdminKotaKabTerdaftarAdapter.AdminKotaKabTerdaftarAdapter>() {

    class AdminKotaKabTerdaftarAdapter(v: View) : RecyclerView.ViewHolder(v) {
        val tvNo: TextView
        val tvKotaKab: TextView
        val tvSetting: TextView

        init {
            tvNo = v.findViewById(R.id.tvNo)
            tvKotaKab = v.findViewById(R.id.tvKotaKab)
            tvSetting = v.findViewById(R.id.tvSetting)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdminKotaKabTerdaftarAdapter {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_admin_kota_kab_table, parent, false)
        return AdminKotaKabTerdaftarAdapter(view)
    }

    override fun getItemCount(): Int {
        return arrayKotaKab.size+1
    }

    override fun onBindViewHolder(holder: AdminKotaKabTerdaftarAdapter, position: Int) {
        holder.apply {
            if(position==0){
                tvNo.text = "NO"
                tvKotaKab.text = "Nama Kota/Kab"
                tvSetting.text = ""

                tvNo.setBackgroundResource(R.drawable.bg_table_title)
                tvKotaKab.setBackgroundResource(R.drawable.bg_table_title)
                tvSetting.setBackgroundResource(R.drawable.bg_table_title)

                tvNo.setTextColor(Color.parseColor("#ffffff"))
                tvKotaKab.setTextColor(Color.parseColor("#ffffff"))
                tvSetting.setTextColor(Color.parseColor("#ffffff"))

                tvNo.setTypeface(null, Typeface.BOLD)
                tvKotaKab.setTypeface(null, Typeface.BOLD)
                tvSetting.setTypeface(null, Typeface.BOLD)
            }
            else{
                val kotaKab = arrayKotaKab[(position-1)]

                tvNo.text = "$position"
                tvKotaKab.text = kotaKab.kota_kab
                tvSetting.text = ":::"

                tvNo.setBackgroundResource(R.drawable.bg_table)
                tvKotaKab.setBackgroundResource(R.drawable.bg_table)
                tvSetting.setBackgroundResource(R.drawable.bg_table)

                tvNo.setTextColor(Color.parseColor("#000000"))
                tvKotaKab.setTextColor(Color.parseColor("#000000"))
                tvSetting.setTextColor(Color.parseColor("#000000"))

                tvNo.setTypeface(null, Typeface.NORMAL)
                tvKotaKab.setTypeface(null, Typeface.NORMAL)
                tvSetting.setTypeface(null, Typeface.NORMAL)

                tvSetting.setOnClickListener {
                    listener.onClickItem(kotaKab, position, it)
                }
            }
        }
    }

    interface ClickListener{
        fun onClickItem(kotaKab: KotaKabModel, position: Int, it:View)
    }
}