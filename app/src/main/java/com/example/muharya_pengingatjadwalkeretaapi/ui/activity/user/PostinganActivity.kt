package com.example.muharya_pengingatjadwalkeretaapi.ui.activity.user

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.muharya_pengingatjadwalkeretaapi.R
import com.example.muharya_pengingatjadwalkeretaapi.adapter.PostinganAdapter
import com.example.muharya_pengingatjadwalkeretaapi.adapter.PostinganKomentarAdapter
import com.example.muharya_pengingatjadwalkeretaapi.data.database.api.ApiService
import com.example.muharya_pengingatjadwalkeretaapi.data.model.PostinganKomentarModel
import com.example.muharya_pengingatjadwalkeretaapi.data.model.PostinganModel
import com.example.muharya_pengingatjadwalkeretaapi.data.model.ResponseModel
import com.example.muharya_pengingatjadwalkeretaapi.databinding.ActivityPostinganBinding
import com.example.muharya_pengingatjadwalkeretaapi.databinding.AlertDialogKonfirmasiBinding
import com.example.muharya_pengingatjadwalkeretaapi.databinding.BottomSheetDialogKomentarBinding
import com.example.muharya_pengingatjadwalkeretaapi.utils.KontrolNavigationDrawer
import com.example.muharya_pengingatjadwalkeretaapi.utils.LoadingAlertDialog
import com.example.muharya_pengingatjadwalkeretaapi.utils.SharedPreferencesLogin
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostinganActivity : Activity() {
    private lateinit var binding: ActivityPostinganBinding
    lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    lateinit var sharedPref : SharedPreferencesLogin
    lateinit var arrayPostingan: ArrayList<PostinganModel>
    lateinit var adapter: PostinganAdapter
    lateinit var loading:LoadingAlertDialog

    private lateinit var rvKomentar: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostinganBinding.inflate(layoutInflater)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(binding.root)

        arrayPostingan = arrayListOf()
        kontrolNavigationDrawer = KontrolNavigationDrawer(this@PostinganActivity)
        sharedPref = SharedPreferencesLogin(this@PostinganActivity)
        loading = LoadingAlertDialog(this@PostinganActivity)

        loading.alertDialogLoading()
        getPostingan()
        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@PostinganActivity)

        }
    }

    fun getPostingan(){
        ApiService.getRetrofit().getPostingan("")
            .enqueue(object: Callback<ArrayList<PostinganModel>> {
                override fun onResponse(
                    call: Call<ArrayList<PostinganModel>>,
                    response: Response<ArrayList<PostinganModel>>
                ) {
                    arrayPostingan = response.body()!!
                    if(arrayPostingan.isNotEmpty()){
                        adapter = PostinganAdapter(arrayPostingan, object: PostinganAdapter.onClickPostingan{
//                            override fun onClickLike(
//                                id_user: String,
//                                id_postingan: String,
//                                sudahLike: Boolean,
//                                holder: PostinganAdapter.ViewHolder,
//                                it: View
//                            ) {
//                                postPostinganLike(
//                                    id_postingan,
//                                    sharedPref.getId().toString(),
//                                    sudahLike,
//                                    holder.binding.ivLike
//                                )
//                            }

                            override fun onClickKomentar(
                                id_user: String,
                                id_postingan: String,
                                position:Int,
                                it: View
                            ) {
                                setShowBottomSheetDialog(id_postingan)
                            }

                        }, sharedPref.getId().toString(), 1)
                        binding.rvPostingan.layoutManager = LinearLayoutManager(this@PostinganActivity)
                        binding.rvPostingan.adapter = adapter
                    }
                    else{
                        Toast.makeText(this@PostinganActivity, "Tidak Ada Postingan", Toast.LENGTH_SHORT).show()
                    }
                    loading.alertDialogCancel()
                }

                override fun onFailure(call: Call<ArrayList<PostinganModel>>, t: Throwable) {
                    loading.alertDialogCancel()
                }

            })
    }

    private fun setShowBottomSheetDialog(idPostingan: String){

        val view = BottomSheetDialogKomentarBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(this@PostinganActivity)

        dialog.setContentView(view.root)
        dialog.setCancelable(true)
        dialog.show()

        rvKomentar = view.rvKomentar

        view.apply {
            // Full Height
            tvTtile.setOnClickListener {
                val bottomSheetDialog = dialog
                val parentLayout =
                    bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                parentLayout?.let { it ->
                    val behaviour = BottomSheetBehavior.from(it)

                    val layoutParams = it.layoutParams
                    layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
                    it.layoutParams = layoutParams

                    behaviour.state = BottomSheetBehavior.STATE_EXPANDED
                }

                etTulisKomentar.apply {
                    requestFocus()
                    dispatchTouchEvent(
                        MotionEvent.obtain(
                            SystemClock.uptimeMillis(),
                            SystemClock.uptimeMillis(),
                            MotionEvent.ACTION_UP,0f,0f,0))
                }
            }
            //Close Komentar
            btnBack.setOnClickListener {
                dialog.dismiss()
            }

            getPostinganKomentar(idPostingan)

            btnSendKomentar.setOnClickListener {
                var cek = true

                if(etTulisKomentar.text.toString().trim().isEmpty()){
                    cek = false
                }

                if(cek){
                    val komentar = etTulisKomentar.text.toString().trim()
                    postPostinganKomentar(
                        idPostingan,
                        "0",    // Jika 0 maka komentar baru
                        sharedPref.getId().toString(),
                        komentar,
                        etTulisKomentar,
                        view.root
                    )
                }
            }
        }
    }

    private fun getPostinganKomentar(idPostingan: String){
        ApiService.getRetrofit().getPostinganKomentar("", idPostingan)
            .enqueue(object : Callback<ArrayList<PostinganKomentarModel>>{
                override fun onResponse(
                    call: Call<ArrayList<PostinganKomentarModel>>,
                    response: Response<ArrayList<PostinganKomentarModel>>
                ) {
                    var arrayListPostingan: ArrayList<PostinganKomentarModel> = arrayListOf()
                    if(response.body()!!.isNotEmpty()){
                        arrayListPostingan = response.body()!!

                        val adapter = PostinganKomentarAdapter(
                            arrayListPostingan, sharedPref.getId().toString(), object : PostinganKomentarAdapter.onClickPostinganKomentar{
                                override fun onClickBalasKomentar(
                                    id_user: String,
                                    id_postingan_komentar: String,
                                    it: View
                                ) {

                                }

                                override fun onClickHapusKomentar(
                                    id_user: String,
                                    id_postingan_komentar: String,
                                    it: View
                                ) {
                                    dialogHapusKomentar(id_postingan_komentar, idPostingan)
                                }
                            })
                        rvKomentar.layoutManager = LinearLayoutManager(this@PostinganActivity, LinearLayoutManager.VERTICAL, false)
                        rvKomentar.adapter = adapter

                    } else{
                        Toast.makeText(this@PostinganActivity, "Tidak Ada Komentar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ArrayList<PostinganKomentarModel>>, t: Throwable) {
                    Toast.makeText(this@PostinganActivity, "Gagal: ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun postPostinganKomentar(
        idPostingan: String, idPostinganKomentarUser: String, idUser: String, komentar: String, etTulisKomentar: EditText, view: View
    ) {
        loading.alertDialogLoading()
        ApiService.getRetrofit().postPostinganKomentarTambah("", idPostingan, idPostinganKomentarUser, idUser, komentar)
            .enqueue(object : Callback<ResponseModel>{
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    loading.alertDialogCancel()
                    getPostinganKomentar(idPostingan)
                    etTulisKomentar.setText("")
                    hideKeyboard(view)
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    loading.alertDialogCancel()
                    Toast.makeText(this@PostinganActivity, "Gagal : ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun dialogHapusKomentar(idPostinganKomentar: String, idPostingan: String) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)
        val alertDialog = AlertDialog.Builder(this@PostinganActivity)
        alertDialog.setView(view.root)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            btnHapus.setOnClickListener {
                postHapusKomentar(idPostinganKomentar, idPostingan)
                dialogInputan.dismiss()
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postHapusKomentar(id_postingan_komentar: String, idPostingan: String){
        loading.alertDialogLoading()
        ApiService.getRetrofit().postPostinganKomentarHapus("", id_postingan_komentar)
            .enqueue(object: Callback<ResponseModel>{
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    loading.alertDialogCancel()
                    getPostinganKomentar(idPostingan)
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Toast.makeText(this@PostinganActivity, "Gagal : ${t.message}", Toast.LENGTH_SHORT).show()
                    loading.alertDialogCancel()
                }

            })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@PostinganActivity, MainActivity::class.java))
        finish()
    }
}