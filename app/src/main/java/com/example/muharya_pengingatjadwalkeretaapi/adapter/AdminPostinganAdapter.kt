package com.example.muharya_pengingatjadwalkeretaapi.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muharya_pengingatjadwalkeretaapi.R
import com.example.muharya_pengingatjadwalkeretaapi.data.database.api.ApiService
import com.example.muharya_pengingatjadwalkeretaapi.data.model.PostinganModel

class AdminPostinganAdapter(
    val arrayPostingan: ArrayList<PostinganModel>,
    val listener: ClickListener
): RecyclerView.Adapter<AdminPostinganAdapter.ViewModel>() {
    class ViewModel(v: View) : RecyclerView.ViewHolder(v) {
        val tvNo: TextView
        val tvCaption: TextView
        val tvGambar: TextView
        val ivGambar: ImageView
        val tvWaktu: TextView
        val tvSetting: TextView

        init {
            tvNo = v.findViewById(R.id.tvNo)
            tvCaption = v.findViewById(R.id.tvCaption)
            tvGambar = v.findViewById(R.id.tvGambar)
            ivGambar = v.findViewById(R.id.ivGambar)
            tvWaktu = v.findViewById(R.id.tvWaktu)
            tvSetting = v.findViewById(R.id.tvSetting)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewModel {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_admin_postingan_table, parent, false)
        return ViewModel(view)
    }

    override fun getItemCount(): Int {
        return arrayPostingan.size+1
    }

    override fun onBindViewHolder(holder: ViewModel, position: Int) {
        holder.apply {
            if(position==0){
                tvNo.text = "NO"
                tvCaption.text = "Caption"
                tvGambar.text = "IMG"
                tvWaktu.text = "Waktu"
                tvSetting.text = ""

                tvNo.setBackgroundResource(R.drawable.bg_table_title)
                tvCaption.setBackgroundResource(R.drawable.bg_table_title)
                tvGambar.setBackgroundResource(R.drawable.bg_table_title)
                tvWaktu.setBackgroundResource(R.drawable.bg_table_title)
                tvSetting.setBackgroundResource(R.drawable.bg_table_title)

                tvNo.setTextColor(Color.parseColor("#ffffff"))
                tvCaption.setTextColor(Color.parseColor("#ffffff"))
                tvGambar.setTextColor(Color.parseColor("#ffffff"))
                tvWaktu.setTextColor(Color.parseColor("#ffffff"))
                tvSetting.setTextColor(Color.parseColor("#ffffff"))

                tvNo.setTypeface(null, Typeface.BOLD)
                tvCaption.setTypeface(null, Typeface.BOLD)
                tvGambar.setTypeface(null, Typeface.BOLD)
                tvWaktu.setTypeface(null, Typeface.BOLD)
                tvSetting.setTypeface(null, Typeface.BOLD)
            }
            else{
                val postingan = arrayPostingan[(position-1)]

                tvNo.text = "$position"
                tvCaption.text = postingan.caption
                tvWaktu.text = postingan.waktu
                tvSetting.text = ":::"

                tvGambar.visibility = View.GONE
                ivGambar.visibility = View.VISIBLE

                Glide.with(holder.itemView.context)
                    .load("${ApiService.BASE_URL}gambar/${postingan.gambar}")
                    .error(R.drawable.gambar_image_error)
                    .placeholder(R.drawable.loading_gif)
                    .into(ivGambar)

                tvNo.setBackgroundResource(R.drawable.bg_table)
                tvCaption.setBackgroundResource(R.drawable.bg_table)
                ivGambar.setBackgroundResource(R.drawable.bg_table)
                tvWaktu.setBackgroundResource(R.drawable.bg_table)
                tvSetting.setBackgroundResource(R.drawable.bg_table)

                tvNo.setTextColor(Color.parseColor("#000000"))
                tvCaption.setTextColor(Color.parseColor("#000000"))
                tvWaktu.setTextColor(Color.parseColor("#000000"))
                tvSetting.setTextColor(Color.parseColor("#000000"))

                tvNo.setTypeface(null, Typeface.NORMAL)
                tvCaption.setTypeface(null, Typeface.NORMAL)
                tvWaktu.setTypeface(null, Typeface.NORMAL)
                tvSetting.setTypeface(null, Typeface.NORMAL)

                tvCaption.setOnClickListener {
                    listener.onClickCaption(postingan.caption!!, position, it)
                }
                ivGambar.setOnClickListener {
                    listener.onClickGambar(postingan.gambar!!, position, it)
                }
                tvSetting.setOnClickListener {
                    listener.onClickSetting(postingan, position, it)
                }
            }
        }
    }

    interface ClickListener{
        fun onClickSetting(postingan: PostinganModel, position: Int, it: View)
        fun onClickCaption(caption: String, position: Int, it: View)
        fun onClickGambar(gambar: String, position: Int, it: View)
    }
}