package com.example.muharya_pengingatjadwalkeretaapi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muharya_pengingatjadwalkeretaapi.R
import com.example.muharya_pengingatjadwalkeretaapi.data.database.api.ApiService
import com.example.muharya_pengingatjadwalkeretaapi.data.model.PostinganKomentarModel
import com.example.muharya_pengingatjadwalkeretaapi.data.model.PostinganModel
import com.example.muharya_pengingatjadwalkeretaapi.databinding.ListPostinganBinding
import com.example.muharya_pengingatjadwalkeretaapi.databinding.ListPostinganKomentarBinding
import com.example.muharya_pengingatjadwalkeretaapi.utils.TanggalDanWaktu

class PostinganKomentarAdapter(
    private val arrayListPostinganKomentar: ArrayList<PostinganKomentarModel>,
    private val idUser: String,
    val listener: onClickPostinganKomentar
): RecyclerView.Adapter<PostinganKomentarAdapter.ViewHolder>() {
    val tanggalDanWaktu = TanggalDanWaktu()
    class ViewHolder(val binding: ListPostinganKomentarBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListPostinganKomentarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayListPostinganKomentar.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = arrayListPostinganKomentar[position]
        holder.binding.apply {
            val id_postingan_komentar = list.id_postingan_komentar
            val id_postingan = list.id_postingan
            val id_postingan_komentar_user = list.id_postingan_komentar_user
            val id_user_komentar = list.id_user_komentar
            val komentar = list.komentar
            val waktu = list.waktu

            val namaUser = list.user[0].nama
            val firstLetter = namaUser.substring(0, 1)

            tvNamaUser.text = namaUser
            tvInisial.text = firstLetter
            tvKomentar.text = komentar
            val arrayWaktu = list.waktu.split(" ")
            val tanggal = tanggalDanWaktu.konversiBulan(arrayWaktu[0])
            tvWaktu.text = tanggal

            val listBgCircle = ArrayList<Int>()
            listBgCircle.add(R.drawable.bg_circle_1)
            listBgCircle.add(R.drawable.bg_circle_2)
            listBgCircle.add(R.drawable.bg_circle)
            listBgCircle.add(R.drawable.bg_circle_4)
            listBgCircle.add(R.drawable.bg_circle_5)

            if(idUser == id_user_komentar){
                tvInisial.setBackgroundResource(R.drawable.bg_circle_3)
                tvNamaUser.text = "Anda"
                tvInisial.text = "A"

                tvHapus.visibility = View.VISIBLE
            } else{
                val mathRandom = (Math.random()*5).toInt()
                tvInisial.setBackgroundResource(listBgCircle[mathRandom])

                tvHapus.visibility = View.GONE
            }

            tvHapus.setOnClickListener {
                listener.onClickHapusKomentar(idUser, id_postingan_komentar, it)
            }

//            tvBalas.setOnClickListener {
//
//            }

        }
    }

    interface onClickPostinganKomentar{
        fun onClickBalasKomentar(id_user: String, id_postingan_komentar:String, it: View)
        fun onClickHapusKomentar(id_user: String, id_postingan_komentar:String, it: View)
    }
}