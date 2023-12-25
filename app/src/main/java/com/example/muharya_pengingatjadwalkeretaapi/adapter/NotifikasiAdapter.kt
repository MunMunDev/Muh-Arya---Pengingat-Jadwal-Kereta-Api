package com.example.muharya_pengingatjadwalkeretaapi.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.muharya_pengingatjadwalkeretaapi.R
import com.example.muharya_pengingatjadwalkeretaapi.data.model.PesananModel
import com.example.muharya_pengingatjadwalkeretaapi.utils.TanggalDanWaktu

class NotifikasiAdapter(var context: Context, var arrayList: ArrayList<PesananModel>, var listener: OnClickItemListener): RecyclerView.Adapter<NotifikasiAdapter.NotifikasiViewHolder>() {

    class NotifikasiViewHolder(v: View): RecyclerView.ViewHolder(v) {
        //Header
        val clHeader : ConstraintLayout
        val tvTanggal : TextView
        val clBody : ConstraintLayout
        val clKeberangkatan: ConstraintLayout
        val tvAsal : TextView
        val tvTujuan : TextView
        val tvWaktuBerangkat: TextView
        val tvWaktuNotifikasi : TextView
        val tvStatusNotifikasi: TextView
        val ivSetting: ImageView

        init {
            clHeader = v.findViewById(R.id.clHeader)
            tvTanggal = v.findViewById(R.id.tvTanggal)
            clBody = v.findViewById(R.id.clBody)
            clKeberangkatan = v.findViewById(R.id.clKeberangkatan)
            tvAsal = v.findViewById(R.id.tvAsal)
            tvTujuan = v.findViewById(R.id.tvTujuan)
            tvWaktuBerangkat = v.findViewById(R.id.tvWaktuBerangkat)
            tvWaktuNotifikasi = v.findViewById(R.id.tvWaktuNotifikasi)
            tvStatusNotifikasi = v.findViewById(R.id.tvStatusNotifikasi)
            ivSetting = v.findViewById(R.id.ivSetting)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifikasiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_notifikasi, parent, false)
        return NotifikasiViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NotifikasiViewHolder, position: Int) {
        val valueArray = arrayList[position]
        var konversi = TanggalDanWaktu()
        val tanggal = konversi.konversiBulan(valueArray.tanggal)
        holder.apply {
            if(valueArray.id_pesanan.toString() == "-"){    // Header
                clHeader.visibility = View.VISIBLE
                clBody.visibility = View.GONE

//                tvTanggal.text = valueArray.tanggal
                tvTanggal.text = tanggal
            }
            else{                                           // Body
                clHeader.visibility = View.GONE
                clBody.visibility = View.VISIBLE

//                tvAsal.text = "${valueArray.dari_stasiun}, ${valueArray.dari_kota}"
//                tvTujuan.text = "${valueArray.sampai_stasiun}, ${valueArray.sampai_kota}"
//                val arrayWaktuKeberangkatan = valueArray.waktu.split(":")
//                tvWaktuBerangkat.text = "${arrayWaktuKeberangkatan[0]}:${arrayWaktuKeberangkatan[1]} Wita"
//                val arrayWaktuNotifikasi = valueArray.waktu_alarm.split(":")
//                tvWaktuNotifikasi.text = "${arrayWaktuNotifikasi[0]}:${arrayWaktuNotifikasi[1]} Wita"
//                tvStatusNotifikasi.text = valueArray.status_alarm

                tvAsal.text = "${valueArray.dari_stasiun}, ${valueArray.dari_kota_kab}"
                tvTujuan.text = "${valueArray.sampai_stasiun}, ${valueArray.sampai_kota_kab}"
                val arrayWaktuKeberangkatan = valueArray.waktu.split(":")
                tvWaktuBerangkat.text = "${arrayWaktuKeberangkatan[0]}:${arrayWaktuKeberangkatan[1]} Wita"
                val arrayWaktuNotifikasi = valueArray.waktu_alarm.split(":")
                tvWaktuNotifikasi.text = "${arrayWaktuNotifikasi[0]}:${arrayWaktuNotifikasi[1]} Wita"
                tvStatusNotifikasi.text = valueArray.status_alarm
            }

            clKeberangkatan.setOnClickListener {
                listener.OnClick(valueArray)
            }
            ivSetting.setOnClickListener {
                listener.OnSettingClick(valueArray, it)
            }
        }
    }

    interface OnClickItemListener{
        fun OnClick(arrayList: PesananModel)
        fun OnSettingClick(arrayList: PesananModel, it:View)
    }
}