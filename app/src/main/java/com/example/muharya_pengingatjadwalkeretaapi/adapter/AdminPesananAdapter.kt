package com.example.muharya_pengingatjadwalkeretaapi.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muharya_pengingatjadwalkeretaapi.R
import com.example.muharya_pengingatjadwalkeretaapi.data.model.PesananModel
import com.example.muharya_pengingatjadwalkeretaapi.utils.KonversiRupiah
import com.example.muharya_pengingatjadwalkeretaapi.utils.TanggalDanWaktu

class AdminPesananAdapter(val arrayPesanan: ArrayList<PesananModel>, val click: OnItemClick): RecyclerView.Adapter<AdminPesananAdapter.AdminPesananViewHolder>() {
    class AdminPesananViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvNo: TextView
        var tvWaktuPembelian: TextView
        var tvTanggalBerangkat: TextView
        var tvWaktuBerangkat: TextView
        var tvNamaUser: TextView
        var tvNomorHp: TextView
        var tvDariKotaKab: TextView
        var tvDariStasiun: TextView
        var tvSampaiKotaKab: TextView
        var tvSampaiStasiun: TextView
        var tvJumlahTiket: TextView
        var tvHargaSatuan: TextView
        var tvTotalHarga: TextView
        var tvSetting: TextView

        init {
            tvNo = v.findViewById(R.id.tvNo)
            tvWaktuPembelian = v.findViewById(R.id.tvWaktuPembelian)
            tvTanggalBerangkat = v.findViewById(R.id.tvTanggalBerangkat)
            tvWaktuBerangkat = v.findViewById(R.id.tvWaktuBerangkat)
            tvNamaUser = v.findViewById(R.id.tvNamaUser)
            tvNomorHp = v.findViewById(R.id.tvNomorHp)
            tvDariKotaKab = v.findViewById(R.id.tvDariKotaKab)
            tvDariStasiun = v.findViewById(R.id.tvDariStasiun)
            tvSampaiKotaKab = v.findViewById(R.id.tvSampaiKotaKab)
            tvSampaiStasiun = v.findViewById(R.id.tvSampaiStasiun)
            tvJumlahTiket = v.findViewById(R.id.tvJumlahTiket)
            tvHargaSatuan = v.findViewById(R.id.tvHargaSatuan)
            tvTotalHarga = v.findViewById(R.id.tvTotalHarga)
            tvSetting = v.findViewById(R.id.tvSetting)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminPesananViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_admin_pesanan_table, parent, false)
        return AdminPesananViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayPesanan.size+1
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: AdminPesananViewHolder, position: Int) {
        holder.apply {
            if(position<=0){
                tvNo.text = "NO"
                tvWaktuPembelian.text = "Waktu Pembelian"
                tvTanggalBerangkat.text = "Tanggal"
                tvWaktuBerangkat.text = "Waktu"
                tvNamaUser.text = "Nama User"
                tvNomorHp.text = "Nomor HP"
                tvDariKotaKab.text = "Dari Kota/Kab"
                tvDariStasiun.text = "Dari Stasiun"
                tvSampaiKotaKab.text = "Sampai Kota/Kab"
                tvSampaiStasiun.text = "Sampai Stasiun"
                tvJumlahTiket.text = "Tiket"
                tvHargaSatuan.text = "Harga"
                tvTotalHarga.text = "Harga Total"
                tvSetting.text = ""

                tvNo.setBackgroundResource(R.drawable.bg_table_title)
                tvWaktuPembelian.setBackgroundResource(R.drawable.bg_table_title)
                tvTanggalBerangkat.setBackgroundResource(R.drawable.bg_table_title)
                tvWaktuBerangkat.setBackgroundResource(R.drawable.bg_table_title)
                tvNamaUser.setBackgroundResource(R.drawable.bg_table_title)
                tvNomorHp.setBackgroundResource(R.drawable.bg_table_title)
                tvDariKotaKab.setBackgroundResource(R.drawable.bg_table_title)
                tvDariStasiun.setBackgroundResource(R.drawable.bg_table_title)
                tvSampaiKotaKab.setBackgroundResource(R.drawable.bg_table_title)
                tvSampaiStasiun.setBackgroundResource(R.drawable.bg_table_title)
                tvJumlahTiket.setBackgroundResource(R.drawable.bg_table_title)
                tvHargaSatuan.setBackgroundResource(R.drawable.bg_table_title)
                tvTotalHarga.setBackgroundResource(R.drawable.bg_table_title)
                tvSetting.setBackgroundResource(R.drawable.bg_table_title)

                tvNo.setTextColor(Color.parseColor("#ffffff"))
                tvWaktuPembelian.setTextColor(Color.parseColor("#ffffff"))
                tvTanggalBerangkat.setTextColor(Color.parseColor("#ffffff"))
                tvWaktuBerangkat.setTextColor(Color.parseColor("#ffffff"))
                tvNamaUser.setTextColor(Color.parseColor("#ffffff"))
                tvNomorHp.setTextColor(Color.parseColor("#ffffff"))
                tvDariKotaKab.setTextColor(Color.parseColor("#ffffff"))
                tvDariStasiun.setTextColor(Color.parseColor("#ffffff"))
                tvSampaiKotaKab.setTextColor(Color.parseColor("#ffffff"))
                tvSampaiStasiun.setTextColor(Color.parseColor("#ffffff"))
                tvJumlahTiket.setTextColor(Color.parseColor("#ffffff"))
                tvHargaSatuan.setTextColor(Color.parseColor("#ffffff"))
                tvTotalHarga.setTextColor(Color.parseColor("#ffffff"))
                tvSetting.setTextColor(Color.parseColor("#ffffff"))

                tvNo.setTypeface(null, Typeface.BOLD)
                tvWaktuPembelian.setTypeface(null, Typeface.BOLD)
                tvTanggalBerangkat.setTypeface(null, Typeface.BOLD)
                tvWaktuBerangkat.setTypeface(null, Typeface.BOLD)
                tvNamaUser.setTypeface(null, Typeface.BOLD)
                tvNomorHp.setTypeface(null, Typeface.BOLD)
                tvDariKotaKab.setTypeface(null, Typeface.BOLD)
                tvDariStasiun.setTypeface(null, Typeface.BOLD)
                tvSampaiKotaKab.setTypeface(null, Typeface.BOLD)
                tvSampaiStasiun.setTypeface(null, Typeface.BOLD)
                tvJumlahTiket.setTypeface(null, Typeface.BOLD)
                tvHargaSatuan.setTypeface(null, Typeface.BOLD)
                tvTotalHarga.setTypeface(null, Typeface.BOLD)
                tvSetting.setTypeface(null, Typeface.BOLD)
            }
            else{
                val pesanan = arrayPesanan[(position-1)]
                val arrayWaktuPembelian = pesanan.waktu_pembelian.split(" ")
                val arrayWaktuPemb = arrayWaktuPembelian[1].split(":")
                val tanggalTanggalPemb = TanggalDanWaktu().konversiBulanSingkatan(arrayWaktuPembelian[0])
                val waktuTanggalPemb = "${arrayWaktuPemb[0]}:${arrayWaktuPemb[1]} Wita"

                val arrayWaktuBerangkat = pesanan.waktu.split(":")
                val waktuBerangkat = "${arrayWaktuBerangkat[0]}:${arrayWaktuBerangkat[1]} Wita"

                tvNo.text = "$position"
//            tvWaktuPembelian.text = pesanan.waktu_pembelian
                tvWaktuPembelian.text = "$tanggalTanggalPemb $waktuTanggalPemb"
                tvTanggalBerangkat.text = waktuBerangkat
                tvWaktuBerangkat.text = pesanan.waktu
                tvNamaUser.text = pesanan.nama
                tvNomorHp.text = pesanan.nomor_hp
                tvDariKotaKab.text = pesanan.dari_kota_kab
                tvDariStasiun.text = pesanan.dari_stasiun
                tvSampaiKotaKab.text = pesanan.sampai_kota_kab
                tvSampaiStasiun.text = pesanan.sampai_stasiun
                tvJumlahTiket.text = pesanan.jumlah_tiket
                tvHargaSatuan.text = KonversiRupiah().rupiah(pesanan.harga_satuan.toLong())
                tvTotalHarga.text = KonversiRupiah().rupiah(pesanan.total_harga.toLong())
                tvSetting.text = ":::"

                tvNo.setTextColor(Color.parseColor("#000000"))
                tvWaktuPembelian.setTextColor(Color.parseColor("#000000"))
                tvTanggalBerangkat.setTextColor(Color.parseColor("#000000"))
                tvWaktuBerangkat.setTextColor(Color.parseColor("#000000"))
                tvNamaUser.setTextColor(Color.parseColor("#000000"))
                tvNomorHp.setTextColor(Color.parseColor("#000000"))
                tvDariKotaKab.setTextColor(Color.parseColor("#000000"))
                tvDariStasiun.setTextColor(Color.parseColor("#000000"))
                tvSampaiKotaKab.setTextColor(Color.parseColor("#000000"))
                tvSampaiStasiun.setTextColor(Color.parseColor("#000000"))
                tvJumlahTiket.setTextColor(Color.parseColor("#000000"))
                tvHargaSatuan.setTextColor(Color.parseColor("#000000"))
                tvTotalHarga.setTextColor(Color.parseColor("#000000"))
                tvSetting.setTextColor(Color.parseColor("#000000"))

                tvNo.setTypeface(null, Typeface.NORMAL)
                tvWaktuPembelian.setTypeface(null, Typeface.NORMAL)
                tvTanggalBerangkat.setTypeface(null, Typeface.NORMAL)
                tvWaktuBerangkat.setTypeface(null, Typeface.NORMAL)
                tvNamaUser.setTypeface(null, Typeface.NORMAL)
                tvNomorHp.setTypeface(null, Typeface.NORMAL)
                tvDariKotaKab.setTypeface(null, Typeface.NORMAL)
                tvDariStasiun.setTypeface(null, Typeface.NORMAL)
                tvSampaiKotaKab.setTypeface(null, Typeface.NORMAL)
                tvSampaiStasiun.setTypeface(null, Typeface.NORMAL)
                tvJumlahTiket.setTypeface(null, Typeface.NORMAL)
                tvHargaSatuan.setTypeface(null, Typeface.NORMAL)
                tvTotalHarga.setTypeface(null, Typeface.NORMAL)
                tvSetting.setTypeface(null, Typeface.NORMAL)

                tvSetting.setOnClickListener {
                    click.clickItemListener(pesanan, it)
                }
            }
        }

    }

    interface OnItemClick{
        fun clickItemListener(pesanan: PesananModel, it:View)
    }
}