package com.example.muharya_pengingatjadwalkeretaapi.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muharya_pengingatjadwalkeretaapi.R
import com.example.muharya_pengingatjadwalkeretaapi.data.database.api.ApiService
import com.example.muharya_pengingatjadwalkeretaapi.data.model.PostinganLikeModel
import com.example.muharya_pengingatjadwalkeretaapi.data.model.PostinganModel
import com.example.muharya_pengingatjadwalkeretaapi.data.model.ResponseModel
import com.example.muharya_pengingatjadwalkeretaapi.databinding.AlertDialogLoadingBinding
import com.example.muharya_pengingatjadwalkeretaapi.databinding.ListPostinganBinding
import com.example.muharya_pengingatjadwalkeretaapi.utils.LoadingAlertDialog
import com.example.muharya_pengingatjadwalkeretaapi.utils.TanggalDanWaktu
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostinganAdapter(
    private var arrayListPostingan: ArrayList<PostinganModel>,
    val listener: onClickPostingan,
    val idUser: String,
    val cekPosisiPostingan: Int     // jika 0 maka main activity, jika 1 maka postingan activity
): RecyclerView.Adapter<PostinganAdapter.ViewHolder>() {

    var arrayListPostinganTemp = arrayListPostingan

//    fun updatePostinganLike(){
//        val view = ViewHolder()
//
//    }

    val tanggalDanWaktu = TanggalDanWaktu()
    lateinit var loading : LoadingAlertDialog
    var sudahLike = false
    var idPostinganLikeTerakhir = ""
    class ViewHolder(val binding: ListPostinganBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListPostinganBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if(arrayListPostingan.isNotEmpty()){
            if(cekPosisiPostingan == 0){
                if(arrayListPostingan.size>=3){
                    3
                } else{
                    arrayListPostingan.size
                }
            } else{
                arrayListPostingan.size
            }
        } else{
            0
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = arrayListPostingan[position]
        holder.binding.apply {
            loading = LoadingAlertDialog(holder.itemView.context)

            tvCaption.text = list.caption
            val arrayWaktu = list.waktu!!.split(" ")
            val tanggal = tanggalDanWaktu.konversiBulan(arrayWaktu[0])
            tvWaktu.text = tanggal

            Glide.with(holder.itemView)
                .load("${ApiService.BASE_URL}gambar/${list.gambar}")
                .error(R.drawable.gambar_image_error)
                .placeholder(R.drawable.loading_gif)
                .into(ivPostingan)

            for(value in list.postingan_like){
                if(value.id_user_like == idUser){
                    ivLike.setImageResource(R.drawable.icon_hati_aktif)
                    sudahLike = true
                }
                idPostinganLikeTerakhir = value.id_postingan_like
            }

            ivLike.setOnClickListener{
//                listener.onClickLike(list.id_user!!, list.id_postingan!!, sudahLike, holder, it)
                postPostinganLike(holder.itemView.context, list.postingan_like[position].id_postingan_like, list.id_postingan!!, position, ivLike)
                Log.d("PostinganAdapterTAG", "size: ${arrayListPostingan[position].postingan_like.size}")
            }
            ivKomentar.setOnClickListener {
                listener.onClickKomentar(list.id_user!!, list.id_postingan!!, position, it)
            }
        }
    }

    private fun postPostinganLike(context: Context, idPostinganLike: String, idPostingan: String, position: Int, ivLike: ImageView){
        loading.alertDialogLoading()
        ApiService.getRetrofit().postPostinganLike("", idPostingan, idUser)
            .enqueue(object : Callback<ResponseModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    updateAdapterPostingan(idPostinganLike, idPostingan, ivLike, position)
                    loading.alertDialogCancel()
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    loading.alertDialogCancel()
                    Toast.makeText(context, "Gagal Like: ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
    }

    fun updateAdapterPostingan(idPostinganLike: String, idPostingan: String, ivLike: ImageView, position: Int){
        var arrayPostinganLike = arrayListOf<PostinganLikeModel>()
        arrayPostinganLike.add(PostinganLikeModel(idPostinganLike, idPostingan, idUser, tanggalDanWaktu.waktuSekarangZonaMakassar()))
        var cek = false
        var i = 0
        var cekPosisiLike = 0;
        for(value in arrayListPostingan[position].postingan_like){
            if(value.id_user_like == idUser){
                cek = true
                cekPosisiLike = i
            }
            i++
        }
        if(cek){
            ivLike.setImageResource(R.drawable.icon_hati)
            arrayListPostingan[position].postingan_like.removeAt(cekPosisiLike)
        } else{
            ivLike.setImageResource(R.drawable.icon_hati_aktif)
            arrayListPostingan[position].postingan_like.add(
                PostinganLikeModel(idPostinganLike, idPostingan, idUser, tanggalDanWaktu.waktuSekarangZonaMakassar())
            )
        }
        notifyDataSetChanged()
    }

    interface onClickPostingan{
//        fun onClickLike(id_user: String, id_postingan:String, sudahLike: Boolean, holder: ViewHolder, it: View)
        fun onClickKomentar(id_user: String, id_postingan:String, position:Int, it: View)
    }
}