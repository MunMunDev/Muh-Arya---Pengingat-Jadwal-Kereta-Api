package com.example.muharya_pengingatjadwalkeretaapi.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muharya_pengingatjadwalkeretaapi.R
import com.example.muharya_pengingatjadwalkeretaapi.data.model.TiketModel
import com.example.muharya_pengingatjadwalkeretaapi.utils.KonversiRupiah
import com.example.muharya_pengingatjadwalkeretaapi.utils.TanggalDanWaktu

class AdminTiketAdapter(val arrayTiket: ArrayList<TiketModel>, val click:AdminTiketAdapter.ClickListener): RecyclerView.Adapter<AdminTiketAdapter.AdminTiketViewHolder>() {
    class AdminTiketViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvNo: TextView
        var tvTanggal: TextView
        var tvWaktu: TextView
        var tvDariKotaKab: TextView
        var tvDariStasiun: TextView
        var tvSampaiKotaKab: TextView
        var tvSampaiStasiun: TextView
        var tvJumlahTiket: TextView
        var tvHarga: TextView
        var tvSetting: TextView

        init {
            tvNo = v.findViewById(R.id.tvNo)
            tvTanggal = v.findViewById(R.id.tvTanggal)
            tvWaktu = v.findViewById(R.id.tvWaktu)
            tvDariKotaKab = v.findViewById(R.id.tvDariKotaKab)
            tvDariStasiun = v.findViewById(R.id.tvDariStasiun)
            tvSampaiKotaKab = v.findViewById(R.id.tvSampaiKotaKab)
            tvSampaiStasiun = v.findViewById(R.id.tvSampaiStasiun)
            tvJumlahTiket = v.findViewById(R.id.tvJumlahTiket)
            tvHarga = v.findViewById(R.id.tvHarga)
            tvSetting = v.findViewById(R.id.tvSetting)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminTiketViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_admin_tiket_table, parent, false)
        return AdminTiketViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayTiket.size+1
    }

    override fun onBindViewHolder(holder: AdminTiketViewHolder, position: Int) {
        holder.apply {
            if(position<=0){
                tvNo.text = "NO"
                tvTanggal.text = "Tanggal"
                tvWaktu.text = "Waktu"
                tvDariKotaKab.text = "Dari Kota/Kab"
                tvDariStasiun.text = "Dari Stasiun"
                tvSampaiKotaKab.text = "Sampai Kota/Kab"
                tvSampaiStasiun.text = "Sampai Stasiun"
                tvJumlahTiket.text = "Tiket"
                tvHarga.text = "Harga"
                tvSetting.text = ""

                tvNo.setBackgroundResource(R.drawable.bg_table_title)
                tvTanggal.setBackgroundResource(R.drawable.bg_table_title)
                tvWaktu.setBackgroundResource(R.drawable.bg_table_title)
                tvDariKotaKab.setBackgroundResource(R.drawable.bg_table_title)
                tvDariStasiun.setBackgroundResource(R.drawable.bg_table_title)
                tvSampaiKotaKab.setBackgroundResource(R.drawable.bg_table_title)
                tvSampaiStasiun.setBackgroundResource(R.drawable.bg_table_title)
                tvJumlahTiket.setBackgroundResource(R.drawable.bg_table_title)
                tvHarga.setBackgroundResource(R.drawable.bg_table_title)
                tvSetting.setBackgroundResource(R.drawable.bg_table_title)

                tvNo.setTextColor(Color.parseColor("#ffffff"))
                tvTanggal.setTextColor(Color.parseColor("#ffffff"))
                tvWaktu.setTextColor(Color.parseColor("#ffffff"))
                tvDariKotaKab.setTextColor(Color.parseColor("#ffffff"))
                tvDariStasiun.setTextColor(Color.parseColor("#ffffff"))
                tvSampaiKotaKab.setTextColor(Color.parseColor("#ffffff"))
                tvSampaiStasiun.setTextColor(Color.parseColor("#ffffff"))
                tvJumlahTiket.setTextColor(Color.parseColor("#ffffff"))
                tvHarga.setTextColor(Color.parseColor("#ffffff"))
                tvSetting.setTextColor(Color.parseColor("#ffffff"))

                tvNo.setTypeface(null, Typeface.BOLD)
                tvTanggal.setTypeface(null, Typeface.BOLD)
                tvWaktu.setTypeface(null, Typeface.BOLD)
                tvDariKotaKab.setTypeface(null, Typeface.BOLD)
                tvDariStasiun.setTypeface(null, Typeface.BOLD)
                tvSampaiKotaKab.setTypeface(null, Typeface.BOLD)
                tvSampaiStasiun.setTypeface(null, Typeface.BOLD)
                tvJumlahTiket.setTypeface(null, Typeface.BOLD)
                tvHarga.setTypeface(null, Typeface.BOLD)
                tvSetting.setTypeface(null, Typeface.BOLD)
            }
            else{
                val tiket = arrayTiket[(position-1)]
                val tanggalTanggal = "${tiket.hari}, ${TanggalDanWaktu().konversiBulanSingkatan(tiket.tanggal)}"

                val arrayWaktuBerangkat = tiket.waktu.split(":")
                val waktuBerangkat = "${arrayWaktuBerangkat[0]}:${arrayWaktuBerangkat[1]} Wita"

                tvNo.text = "$position"
//            tvWaktuPembelian.text = tiket.waktu_pembelian
                tvTanggal.text = tanggalTanggal
                tvWaktu.text = waktuBerangkat
                tvDariKotaKab.text = tiket.dari_kota_kab
                tvDariStasiun.text = tiket.dari_stasiun
                tvSampaiKotaKab.text = tiket.sampai_kota_kab
                tvSampaiStasiun.text = tiket.sampai_stasiun
                tvJumlahTiket.text = tiket.jumlah_tiket
                tvHarga.text = KonversiRupiah().rupiah(tiket.harga.toLong())
                tvSetting.text = ":::"

                tvNo.setBackgroundResource(R.drawable.bg_table)
                tvTanggal.setBackgroundResource(R.drawable.bg_table)
                tvWaktu.setBackgroundResource(R.drawable.bg_table)
                tvDariKotaKab.setBackgroundResource(R.drawable.bg_table)
                tvDariStasiun.setBackgroundResource(R.drawable.bg_table)
                tvSampaiKotaKab.setBackgroundResource(R.drawable.bg_table)
                tvSampaiStasiun.setBackgroundResource(R.drawable.bg_table)
                tvJumlahTiket.setBackgroundResource(R.drawable.bg_table)
                tvHarga.setBackgroundResource(R.drawable.bg_table)
                tvSetting.setBackgroundResource(R.drawable.bg_table)

                tvNo.setTextColor(Color.parseColor("#000000"))
                tvTanggal.setTextColor(Color.parseColor("#000000"))
                tvWaktu.setTextColor(Color.parseColor("#000000"))
                tvDariKotaKab.setTextColor(Color.parseColor("#000000"))
                tvDariStasiun.setTextColor(Color.parseColor("#000000"))
                tvSampaiKotaKab.setTextColor(Color.parseColor("#000000"))
                tvSampaiStasiun.setTextColor(Color.parseColor("#000000"))
                tvJumlahTiket.setTextColor(Color.parseColor("#000000"))
                tvHarga.setTextColor(Color.parseColor("#000000"))
                tvSetting.setTextColor(Color.parseColor("#000000"))

                tvNo.setTypeface(null, Typeface.NORMAL)
                tvTanggal.setTypeface(null, Typeface.NORMAL)
                tvWaktu.setTypeface(null, Typeface.NORMAL)
                tvDariKotaKab.setTypeface(null, Typeface.NORMAL)
                tvDariStasiun.setTypeface(null, Typeface.NORMAL)
                tvSampaiKotaKab.setTypeface(null, Typeface.NORMAL)
                tvSampaiStasiun.setTypeface(null, Typeface.NORMAL)
                tvJumlahTiket.setTypeface(null, Typeface.NORMAL)
                tvHarga.setTypeface(null, Typeface.NORMAL)
                tvSetting.setTypeface(null, Typeface.NORMAL)

                tvSetting.setOnClickListener {
                    click.onClickItemListener(tiket, it)
                }
            }
        }
    }

    interface ClickListener{
        fun onClickItemListener(tiket:TiketModel, it:View)
    }
}