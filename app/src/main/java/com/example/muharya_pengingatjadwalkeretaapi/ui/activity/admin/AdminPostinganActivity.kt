package com.example.muharya_pengingatjadwalkeretaapi.ui.activity.admin

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.muharya_pengingatjadwalkeretaapi.R
import com.example.muharya_pengingatjadwalkeretaapi.adapter.AdminPostinganAdapter
import com.example.muharya_pengingatjadwalkeretaapi.data.database.api.ApiService
import com.example.muharya_pengingatjadwalkeretaapi.data.model.PostinganModel
import com.example.muharya_pengingatjadwalkeretaapi.data.model.ResponseModel
import com.example.muharya_pengingatjadwalkeretaapi.databinding.ActivityAdminPostinganBinding
import com.example.muharya_pengingatjadwalkeretaapi.databinding.AlertDialogAdminPostinganBinding
import com.example.muharya_pengingatjadwalkeretaapi.databinding.AlertDialogKeteranganBinding
import com.example.muharya_pengingatjadwalkeretaapi.databinding.AlertDialogShowImageBinding
import com.example.muharya_pengingatjadwalkeretaapi.utils.KataAcak
import com.example.muharya_pengingatjadwalkeretaapi.utils.KontrolNavigationDrawer
import com.example.muharya_pengingatjadwalkeretaapi.utils.LoadingAlertDialog
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminPostinganActivity : Activity() {
    lateinit var binding: ActivityAdminPostinganBinding
    lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    lateinit var loading: LoadingAlertDialog

    lateinit var arrayPostingan: ArrayList<PostinganModel>
    lateinit var adapter: AdminPostinganAdapter

    private var tempView: AlertDialogAdminPostinganBinding? = null
    private var fileImage: MultipartBody.Part? = null
    private var kataAcak = KataAcak()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminPostinganBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(binding.root)

        kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminPostinganActivity)
        loading = LoadingAlertDialog(this@AdminPostinganActivity)
        loading.alertDialogLoading()

        getDataPostingan()

        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminPostinganActivity)

            btnTambah.setOnClickListener {
                dialogTambahData()
            }
//            btnKoordinat.setOnClickListener{
//                startActivity(Intent(this@AdminPostinganActivity, AdminKoordinatMapsActivity::class.java))
//            }
        }

    }

    private fun getDataPostingan(){
        ApiService.getRetrofit().getPostingan("")
            .enqueue(object: Callback<ArrayList<PostinganModel>> {
                override fun onResponse(
                    call: Call<ArrayList<PostinganModel>>,
                    response: Response<ArrayList<PostinganModel>>
                ) {
                    arrayPostingan = arrayListOf()
                    if(response.body()!!.isNotEmpty()){
                        arrayPostingan = response.body()!!

                        adapter = AdminPostinganAdapter(arrayPostingan, object: AdminPostinganAdapter.ClickListener{
                            override fun onClickSetting(
                                postingan: PostinganModel,
                                position: Int,
                                it: View
                            ) {
                                val popupMenu = PopupMenu(this@AdminPostinganActivity, it)
                                popupMenu.inflate(R.menu.popup_menu)
                                popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
                                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                                        when (menuItem!!.itemId) {
                                            R.id.edit -> {
                                                dialogUpdateData(postingan)
                                                return true
                                            }

                                            R.id.hapus -> {
                                                dialogHapusData(postingan)
                                                return true
                                            }
                                        }
                                        return true
                                    }

                                })
                                popupMenu.show()
                            }

                            override fun onClickCaption(caption: String, position: Int, it: View) {
                                dialogShowKeterangan("Caption", caption)
                            }

                            override fun onClickGambar(gambar: String, position: Int, it: View) {
                                dialogShowGambar("Gambar", gambar)
                            }

                        })

                        binding.rvPostingan.layoutManager = LinearLayoutManager(this@AdminPostinganActivity)
                        binding.rvPostingan.adapter = adapter

                    }
                    loading.alertDialogCancel()
                }

                override fun onFailure(call: Call<ArrayList<PostinganModel>>, t: Throwable) {
                    Toast.makeText(this@AdminPostinganActivity, "Gagal. Periksa Jaringan Anda", Toast.LENGTH_SHORT).show()
                    loading.alertDialogCancel()
                }

            })
    }

    private fun dialogShowKeterangan(judul: String, keterangan: String){
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)
        val alertDialog = AlertDialog.Builder(this@AdminPostinganActivity)
        alertDialog.setView(view.root)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKeterangan.text = judul
            tvBodyKeterangan.text = keterangan

            btnClose.setOnClickListener{
                dialogInputan.dismiss()
            }
        }
    }

    private fun dialogShowGambar(judul: String, gambar: String){
        val view = AlertDialogShowImageBinding.inflate(layoutInflater)
        val alertDialog = AlertDialog.Builder(this@AdminPostinganActivity)
        alertDialog.setView(view.root)
        val dialogInputan = alertDialog.create()
        dialogInputan.setCancelable(false)
        dialogInputan.show()

        view.apply {
            tvTitle.text = judul

            Glide.with(this@AdminPostinganActivity)
                .load("${ApiService.BASE_URL}gambar/$gambar")
                .error(R.drawable.gambar_image_error)
                .placeholder(R.drawable.loading_gif)
                .into(ivShowImage)

            btnClose.setOnClickListener{
                dialogInputan.dismiss()
            }
        }
    }


    // Tambah
    fun dialogTambahData(){
        val view = AlertDialogAdminPostinganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminPostinganActivity)
        alertDialog.setView(view.root)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tempView = view

        view.apply {
            etGambar.setOnClickListener {
                if(checkPermission()){
                    pickImageFile()
                } else{
                    requestPermission()
                }
            }
            btnSimpan.setOnClickListener {
                var cek = true
                if(etCaption.text.toString().trim().isEmpty()){
                    etCaption.error = "Tidak Boleh Kosong"
                    cek = false
                }
                if(etGambar.text.toString().trim().isEmpty()){
                    etGambar.error = "Masukkan Gambar"
                    cek = false
                }
                if(cek){
                    loading.alertDialogLoading()
                    dialogInputan.dismiss()

                    val caption = etCaption.text.toString()
                    postTambahData(caption, fileImage!!)
                }
            }

            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    fun postTambahData(caption: String, fileImage: MultipartBody.Part){
        ApiService.getRetrofit().postAdminTambahPostingan(
            convertStringToMultipartBody(""),
            convertStringToMultipartBody(caption),
            convertStringToMultipartBody(kataAcak.getHurufDanAngka()),
            fileImage
        ).enqueue(object: Callback<ResponseModel>{
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                loading.alertDialogCancel()
                Toast.makeText(this@AdminPostinganActivity, "berhasil", Toast.LENGTH_SHORT).show()
                getDataPostingan()
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                loading.alertDialogCancel()
                Toast.makeText(this@AdminPostinganActivity, "Gagal: ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
//        ApiService.getRetrofit().postAdminTambahStasiun("", namaStasiun, kotaKab)
//            .enqueue(object: Callback<PostinganModel> {
//                override fun onResponse(call: Call<PostinganModel>, response: Response<PostinganModel>) {
//                    Toast.makeText(this@AdminPostinganActivity, "Berhasil Tambah", Toast.LENGTH_SHORT).show()
//                    getDataPostingan()
//                }
//
//                override fun onFailure(call: Call<PostinganModel>, t: Throwable) {
//                    Toast.makeText(this@AdminPostinganActivity, "Gagal Tambah", Toast.LENGTH_SHORT).show()
//                    getDataPostingan()
//                }
//
//            })
    }

    // Update
    fun dialogUpdateData(postingan: PostinganModel){
        val view = AlertDialogAdminPostinganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminPostinganActivity)
        alertDialog.setView(view.root)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tempView = view

        view.apply {
            etCaption.setText(postingan.caption)

            etGambar.setOnClickListener {
                if(checkPermission()){
                    pickImageFile()
                } else{
                    requestPermission()
                }
            }
            btnSimpan.setOnClickListener {
                var cek = true
                var cekGambar = true
                if(etCaption.text.toString().trim().isEmpty()){
                    etCaption.error = "Tidak Boleh Kosong"
                    cek = false
                }

                if(etGambar.text.toString().trim().isEmpty()){
                    cekGambar = false
                }

                if(cek && cekGambar){
                    loading.alertDialogLoading()
                    dialogInputan.dismiss()

                    val idPostingan = postingan.id_postingan!!
                    val caption = etCaption.text.toString()

                    postUpdateData(idPostingan, caption, fileImage!!)
                } else{
                    loading.alertDialogLoading()

                    val idPostingan = postingan.id_postingan!!
                    val caption = etCaption.text.toString()
                    postUpdateDataNoImage(idPostingan, caption)
                }
            }

            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    fun postUpdateData(idPostingan: String, caption: String, fileImage: MultipartBody.Part){
        ApiService.getRetrofit().postAdminUpdatePostingan(
            convertStringToMultipartBody(""),
            convertStringToMultipartBody(idPostingan),
            convertStringToMultipartBody(caption),
            convertStringToMultipartBody(kataAcak.getHurufDanAngka()),
            fileImage
        ) .enqueue(object: Callback<ResponseModel>{
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                loading.alertDialogCancel()
                Toast.makeText(this@AdminPostinganActivity, "Berhasil", Toast.LENGTH_SHORT).show()
                getDataPostingan()
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                loading.alertDialogCancel()
                Toast.makeText(this@AdminPostinganActivity, "Gagal: ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun postUpdateDataNoImage(idPostingan: String, caption: String) {
        ApiService.getRetrofit().postAdminUpdatePostinganNoImage(
            "", idPostingan, caption
        ) .enqueue(object: Callback<ResponseModel>{
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                loading.alertDialogCancel()
                Toast.makeText(this@AdminPostinganActivity, "Berhasil", Toast.LENGTH_SHORT).show()
                getDataPostingan()
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                loading.alertDialogCancel()
                Toast.makeText(this@AdminPostinganActivity, "Gagal: ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }


    // Update
    @SuppressLint("SetTextI18n")
    fun dialogHapusData(postingan: PostinganModel){
        val viewAlertDialog = View.inflate(this@AdminPostinganActivity, R.layout.alert_dialog_admin_hapus, null)

        val tvHapus = viewAlertDialog.findViewById<TextView>(R.id.tvHapus)

        val btnHapus = viewAlertDialog.findViewById<Button>(R.id.btnHapus)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        val alertDialog = AlertDialog.Builder(this@AdminPostinganActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tvHapus.text = "Hapus Postingan Ini ?"

        btnHapus.setOnClickListener {
            dialogInputan.dismiss()
            loading.alertDialogLoading()
            postHapusData(postingan.id_postingan!!)
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    fun postHapusData(id_postingan: String){
        ApiService.getRetrofit().postAdminHapusPostingan("", id_postingan)
            .enqueue(object: Callback<ResponseModel>{
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    loading.alertDialogCancel()
                    Toast.makeText(this@AdminPostinganActivity, "Berhasil", Toast.LENGTH_SHORT).show()
                    getDataPostingan()
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    loading.alertDialogCancel()
                    Toast.makeText(this@AdminPostinganActivity, "gagal: ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
    }

    // permission add image
    private fun requestPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if (Environment.isExternalStorageManager()) {
                startActivity(Intent(this, AdminPostinganActivity::class.java))
            } else { //request for the permission
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        } else{
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                ApiService.STORAGE_PERMISSION_CODE
            )
        }
    }

    private fun pickImageFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
        }

        startActivityForResult(intent, ApiService.IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ApiService.IMAGE_CODE && resultCode == Activity.RESULT_OK && data != null) {
            // Mendapatkan URI file PDF yang dipilih
            val fileUri = data.data!!

            val nameImage = getNameFile(fileUri)

            tempView?.let {
                it.etGambar.text = nameImage
            }

            // Mengirim file PDF ke website menggunakan Retrofit
            fileImage = uploadImageToStorage(fileUri, nameImage, "gambar_postingan")
        }
    }

    private fun getNameFile(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        val nameIndex = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor?.moveToFirst()
        val name = cursor?.getString(nameIndex!!)
        cursor?.close()
        return name!!
    }

    @SuppressLint("Recycle")
    private fun uploadImageToStorage(imageUri: Uri?, fileName: String, nameAPI:String): MultipartBody.Part? {
        var pdfPart : MultipartBody.Part? = null
        imageUri?.let {
            val file = contentResolver.openInputStream(imageUri)?.readBytes()

            if (file != null) {
//                // Membuat objek RequestBody dari file PDF
//                val requestFile = file.toRequestBody("application/pdf".toMediaTypeOrNull())
//                // Membuat objek MultipartBody.Part untuk file PDF
//                pdfPart = MultipartBody.Part.createFormData("materi_pdf", fileName, requestFile)

                pdfPart = convertFileToMultipartBody(file, fileName, nameAPI)
            }
        }
        return pdfPart
    }

    private fun convertFileToMultipartBody(file: ByteArray, fileName: String, nameAPI:String): MultipartBody.Part?{
//        val requestFile = file.toRequestBody("application/pdf".toMediaTypeOrNull())
        val requestFile = file.toRequestBody("application/image".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData(nameAPI, fileName, requestFile)

        return filePart
    }

    private fun convertStringToMultipartBody(data: String): RequestBody {
        return RequestBody.create("multipart/form-data".toMediaTypeOrNull(), data)
    }

    private fun checkPermission(): Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android is 11(R) or above
            Environment.isExternalStorageManager()
        }
        else{
            //Android is below 11(R)
            val write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@AdminPostinganActivity, AdminHomeActivity::class.java))
        finish()
        super.onBackPressed()
    }
}