package com.example.muharya_pengingatjadwalkeretaapi.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muharya_pengingatjadwalkeretaapi.R
import com.example.muharya_pengingatjadwalkeretaapi.data.model.RuteModel
import com.example.muharya_pengingatjadwalkeretaapi.utils.KonversiRupiah
import com.example.muharya_pengingatjadwalkeretaapi.utils.TanggalDanWaktu

class AdminRuteAdapter(val arrayRute: ArrayList<RuteModel>, val listener: ClickListener): RecyclerView.Adapter<AdminRuteAdapter.AdminRuteViewHolder>() {
    class AdminRuteViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvNo: TextView
        var tvDariKotaKab: TextView
        var tvDariStasiun: TextView
        var tvSampaiKotaKab: TextView
        var tvSampaiStasiun: TextView
        var tvHargaSatuan: TextView
        var tvJumlahTiket: TextView
        var tvWaktuBerangkat: TextView
        var tvWaktuSampai: TextView
        var tvSetting: TextView

        init {
            tvNo = v.findViewById(R.id.tvNo)
            tvDariKotaKab = v.findViewById(R.id.tvDariKotaKab)
            tvDariStasiun = v.findViewById(R.id.tvDariStasiun)
            tvSampaiKotaKab = v.findViewById(R.id.tvSampaiKotaKab)
            tvSampaiStasiun = v.findViewById(R.id.tvSampaiStasiun)
            tvHargaSatuan = v.findViewById(R.id.tvHargaSatuan)
            tvJumlahTiket = v.findViewById(R.id.tvJumlahTiket)
            tvWaktuBerangkat = v.findViewById(R.id.tvWaktuBerangkat)
            tvWaktuSampai = v.findViewById(R.id.tvWaktuSampai)
            tvSetting = v.findViewById(R.id.tvSetting)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminRuteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_admin_rute_table, parent, false)
        return AdminRuteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayRute.size+1
    }

    override fun onBindViewHolder(holder: AdminRuteViewHolder, position: Int) {
        Log.d("AdminRuteAdapterTag", "posisi: $position")

        holder.apply {
            if(position==0){
                tvNo.text = "NO"
                tvDariKotaKab.text = "Dari Kota/Kab"
                tvDariStasiun.text = "Dari Stasiun"
                tvSampaiKotaKab.text = "Sampai Kota/Kab"
                tvSampaiStasiun.text = "Sampai Stasiun"
                tvHargaSatuan.text = "Harga"
                tvJumlahTiket.text = "Tiket"
                tvWaktuBerangkat.text = "Berangkat Jam"
                tvWaktuSampai.text = "Sampai Jam"
                tvSetting.text = ""

                tvNo.setBackgroundResource(R.drawable.bg_table_title)
                tvDariKotaKab.setBackgroundResource(R.drawable.bg_table_title)
                tvDariStasiun.setBackgroundResource(R.drawable.bg_table_title)
                tvSampaiKotaKab.setBackgroundResource(R.drawable.bg_table_title)
                tvSampaiStasiun.setBackgroundResource(R.drawable.bg_table_title)
                tvJumlahTiket.setBackgroundResource(R.drawable.bg_table_title)
                tvHargaSatuan.setBackgroundResource(R.drawable.bg_table_title)
                tvWaktuBerangkat.setBackgroundResource(R.drawable.bg_table_title)
                tvWaktuSampai.setBackgroundResource(R.drawable.bg_table_title)
                tvSetting.setBackgroundResource(R.drawable.bg_table_title)

                tvNo.setTextColor(Color.parseColor("#ffffff"))
                tvDariKotaKab.setTextColor(Color.parseColor("#ffffff"))
                tvDariStasiun.setTextColor(Color.parseColor("#ffffff"))
                tvSampaiKotaKab.setTextColor(Color.parseColor("#ffffff"))
                tvSampaiStasiun.setTextColor(Color.parseColor("#ffffff"))
                tvJumlahTiket.setTextColor(Color.parseColor("#ffffff"))
                tvHargaSatuan.setTextColor(Color.parseColor("#ffffff"))
                tvWaktuBerangkat.setTextColor(Color.parseColor("#ffffff"))
                tvWaktuSampai.setTextColor(Color.parseColor("#ffffff"))
                tvSetting.setTextColor(Color.parseColor("#ffffff"))

                tvNo.setTypeface(null, Typeface.BOLD)
                tvDariKotaKab.setTypeface(null, Typeface.BOLD)
                tvDariStasiun.setTypeface(null, Typeface.BOLD)
                tvSampaiKotaKab.setTypeface(null, Typeface.BOLD)
                tvSampaiStasiun.setTypeface(null, Typeface.BOLD)
                tvJumlahTiket.setTypeface(null, Typeface.BOLD)
                tvHargaSatuan.setTypeface(null, Typeface.BOLD)
                tvWaktuBerangkat.setTypeface(null, Typeface.BOLD)
                tvWaktuSampai.setTypeface(null, Typeface.BOLD)
                tvSetting.setTypeface(null, Typeface.BOLD)
            }
            else{
                val pesanan = arrayRute[(position-1)]

                val arrayWaktuBerangkat = pesanan.waktu.split(":")
                val waktuBerangkat = "${arrayWaktuBerangkat[0]}:${arrayWaktuBerangkat[1]} Wita"

                val arrayWaktuSampai = pesanan.waktu_sampai.split(":")
                val waktuSampai = "${arrayWaktuSampai[0]}:${arrayWaktuSampai[1]} Wita"

                tvNo.text = "$position"
//            tvWaktuPembelian.text = pesanan.waktu_pembelian
                tvDariKotaKab.text = pesanan.dari_kota_kab
                tvDariStasiun.text = pesanan.dari_stasiun
                tvSampaiKotaKab.text = pesanan.sampai_kota_kab
                tvSampaiStasiun.text = pesanan.sampai_stasiun
                tvJumlahTiket.text = pesanan.jumlah_tiket
                tvHargaSatuan.text = KonversiRupiah().rupiah(pesanan.harga.toLong())
                tvWaktuBerangkat.text = waktuBerangkat
                tvWaktuSampai.text = waktuSampai
                tvSetting.text = ":::"

                tvNo.setBackgroundResource(R.drawable.bg_table)
                tvDariKotaKab.setBackgroundResource(R.drawable.bg_table)
                tvDariStasiun.setBackgroundResource(R.drawable.bg_table)
                tvSampaiKotaKab.setBackgroundResource(R.drawable.bg_table)
                tvSampaiStasiun.setBackgroundResource(R.drawable.bg_table)
                tvJumlahTiket.setBackgroundResource(R.drawable.bg_table)
                tvHargaSatuan.setBackgroundResource(R.drawable.bg_table)
                tvWaktuBerangkat.setBackgroundResource(R.drawable.bg_table)
                tvWaktuSampai.setBackgroundResource(R.drawable.bg_table)
                tvSetting.setBackgroundResource(R.drawable.bg_table)

                tvNo.setTextColor(Color.parseColor("#000000"))
                tvDariKotaKab.setTextColor(Color.parseColor("#000000"))
                tvDariStasiun.setTextColor(Color.parseColor("#000000"))
                tvSampaiKotaKab.setTextColor(Color.parseColor("#000000"))
                tvSampaiStasiun.setTextColor(Color.parseColor("#000000"))
                tvJumlahTiket.setTextColor(Color.parseColor("#000000"))
                tvHargaSatuan.setTextColor(Color.parseColor("#000000"))
                tvWaktuBerangkat.setTextColor(Color.parseColor("#000000"))
                tvWaktuSampai.setTextColor(Color.parseColor("#000000"))
                tvSetting.setTextColor(Color.parseColor("#000000"))

                tvNo.setTypeface(null, Typeface.NORMAL)
                tvDariKotaKab.setTypeface(null, Typeface.NORMAL)
                tvDariStasiun.setTypeface(null, Typeface.NORMAL)
                tvSampaiKotaKab.setTypeface(null, Typeface.NORMAL)
                tvSampaiStasiun.setTypeface(null, Typeface.NORMAL)
                tvJumlahTiket.setTypeface(null, Typeface.NORMAL)
                tvHargaSatuan.setTypeface(null, Typeface.NORMAL)
                tvWaktuBerangkat.setTypeface(null, Typeface.NORMAL)
                tvWaktuSampai.setTypeface(null, Typeface.NORMAL)
                tvSetting.setTypeface(null, Typeface.NORMAL)

                tvSetting.setOnClickListener {
                    listener.clickItemListener(pesanan, it)
                }
            }
        }
    }

    interface ClickListener{
        fun clickItemListener(rute: RuteModel, it:View)
    }
}