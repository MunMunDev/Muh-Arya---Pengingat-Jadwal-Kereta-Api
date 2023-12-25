package com.example.muharya_pengingatjadwalkeretaapi.ui.activity.admin

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muharya_pengingatjadwalkeretaapi.R
import com.example.muharya_pengingatjadwalkeretaapi.adapter.AdminSemuaAkunAdapter
import com.example.muharya_pengingatjadwalkeretaapi.data.database.api.ApiService
import com.example.muharya_pengingatjadwalkeretaapi.data.model.RuteModel
import com.example.muharya_pengingatjadwalkeretaapi.data.model.UserModel

import com.example.muharya_pengingatjadwalkeretaapi.databinding.ActivityAdminSemuaAkunBinding
import com.example.muharya_pengingatjadwalkeretaapi.utils.KontrolNavigationDrawer
import com.example.muharya_pengingatjadwalkeretaapi.utils.LoadingAlertDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminSemuaAkunActivity : Activity() {
    lateinit var binding: ActivityAdminSemuaAkunBinding
    lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    lateinit var loading: LoadingAlertDialog

    lateinit var arrayAkun: ArrayList<UserModel>
    lateinit var adapter : AdminSemuaAkunAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminSemuaAkunBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(binding.root)

        kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminSemuaAkunActivity)
        loading = LoadingAlertDialog(this@AdminSemuaAkunActivity)
        loading.alertDialogLoading()

        getData()

        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminSemuaAkunActivity)

            btnTambah.setOnClickListener {
                dialogTambahData()
            }
        }

    }

    private fun getData(){
        ApiService.getRetrofit().getSemuaUserAdmin("get_semua_user")
            .enqueue(object: Callback<ArrayList<UserModel>>{
                override fun onResponse(
                    call: Call<ArrayList<UserModel>>,
                    response: Response<ArrayList<UserModel>>
                ) {
                    arrayAkun = arrayListOf()

                    if(response.body()!!.isNotEmpty()){
                        arrayAkun = response.body()!!

                        adapter = AdminSemuaAkunAdapter(arrayAkun, object : AdminSemuaAkunAdapter.ClickItemListener{
                            override fun onClick(user: UserModel, position: Int, it: View) {
                                val popupMenu = PopupMenu(this@AdminSemuaAkunActivity, it)
                                popupMenu.inflate(R.menu.popup_menu)
                                popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
                                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                                        when (menuItem!!.itemId) {
                                            R.id.edit -> {
                                                Toast.makeText(this@AdminSemuaAkunActivity, "Edit", Toast.LENGTH_SHORT).show()
                                                dialogUpdateData(user)
                                                return true
                                            }

                                            R.id.hapus -> {
                                                Toast.makeText(this@AdminSemuaAkunActivity, "Hapus", Toast.LENGTH_SHORT).show()
                                                dialogHapusData(user)
                                                return true
                                            }
                                        }
                                        return true
                                    }

                                })
                                popupMenu.show()
                            }

                        })
                    }

                    binding.rvSemuaAkun.layoutManager = LinearLayoutManager(this@AdminSemuaAkunActivity)
                    binding.rvSemuaAkun.adapter = adapter

                    loading.alertDialogCancel()
                }

                override fun onFailure(call: Call<ArrayList<UserModel>>, t: Throwable) {
                    Toast.makeText(this@AdminSemuaAkunActivity, "Gagal. Periksa Jaringan Anda", Toast.LENGTH_SHORT).show()

                    loading.alertDialogCancel()
                }

            })
    }

    fun dialogTambahData(){
        val viewAlertDialog = View.inflate(this@AdminSemuaAkunActivity, R.layout.alert_dialog_admin_semua_akun, null)

        val etNama = viewAlertDialog.findViewById<EditText>(R.id.etNama)
        val etAlamat = viewAlertDialog.findViewById<EditText>(R.id.etAlamat)
        val etNomorHp = viewAlertDialog.findViewById<EditText>(R.id.etNomorHp)
        val etUsername = viewAlertDialog.findViewById<EditText>(R.id.etUsername)
        val etPassword = viewAlertDialog.findViewById<EditText>(R.id.etPassword)

        val btnSimpan = viewAlertDialog.findViewById<Button>(R.id.btnSimpan)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        val alertDialog = AlertDialog.Builder(this@AdminSemuaAkunActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        btnSimpan.setOnClickListener {
            dialogInputan.dismiss()
            val nama = etNama.text.toString()
            val alamat = etAlamat.text.toString()
            val nomorHp = etNomorHp.text.toString()
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            postTambahData(nama, alamat, nomorHp, username, password)
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    fun postTambahData(nama: String, alamat: String, nomorHp: String, username: String, password: String){

        ApiService.getRetrofit().postAdminTambahUser("", nama, alamat, nomorHp, username, password)
            .enqueue(object: Callback<UserModel>{
                override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                    Toast.makeText(this@AdminSemuaAkunActivity, "Berhasil Tambah", Toast.LENGTH_SHORT).show()
                    getData()
//                    loading.alertDialogCancel()
                }

                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    Toast.makeText(this@AdminSemuaAkunActivity, "Gagal Tambah", Toast.LENGTH_SHORT).show()
                    getData()
//                    loading.alertDialogCancel()
                }

            })
    }


    fun dialogUpdateData(user: UserModel){
        val viewAlertDialog = View.inflate(this@AdminSemuaAkunActivity, R.layout.alert_dialog_admin_semua_akun, null)

        val etNama = viewAlertDialog.findViewById<EditText>(R.id.etNama)
        val etAlamat = viewAlertDialog.findViewById<EditText>(R.id.etAlamat)
        val etNomorHp = viewAlertDialog.findViewById<EditText>(R.id.etNomorHp)
        val etUsername = viewAlertDialog.findViewById<EditText>(R.id.etUsername)
        val etPassword = viewAlertDialog.findViewById<EditText>(R.id.etPassword)

        val btnSimpan = viewAlertDialog.findViewById<Button>(R.id.btnSimpan)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        etNama.setText(user.nama)
        etAlamat.setText(user.alamat)
        etNomorHp.setText(user.nomor_hp)
        etUsername.setText(user.username)
        etPassword.setText(user.password)

        val alertDialog = AlertDialog.Builder(this@AdminSemuaAkunActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        btnSimpan.setOnClickListener {
            dialogInputan.dismiss()

            val nama = etNama.text.toString()
            val alamat = etAlamat.text.toString()
            val nomorHp = etNomorHp.text.toString()
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            postUpdateData(user.id_user, nama, alamat, nomorHp, username, password)
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    fun postUpdateData(id_user:Int, nama: String, alamat: String, nomorHp: String, username: String, password: String){

        ApiService.getRetrofit().postAdminUpdateUser("", id_user, nama, alamat, nomorHp, username, password)
            .enqueue(object: Callback<UserModel>{
                override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                    Toast.makeText(this@AdminSemuaAkunActivity, "Berhasil Update Data", Toast.LENGTH_SHORT).show()
                    getData()
//                    loading.alertDialogCancel()
                }

                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    Toast.makeText(this@AdminSemuaAkunActivity, "Gagal Update Data", Toast.LENGTH_SHORT).show()
                    getData()
//                    loading.alertDialogCancel()
                }

            })
    }



    @SuppressLint("SetTextI18n")
    fun dialogHapusData(user: UserModel){
        val viewAlertDialog = View.inflate(this@AdminSemuaAkunActivity, R.layout.alert_dialog_admin_hapus, null)

        val tvHapus = viewAlertDialog.findViewById<TextView>(R.id.tvHapus)

        val btnHapus = viewAlertDialog.findViewById<Button>(R.id.btnHapus)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        tvHapus.text = "Hapus Akun \n ${user.nama} ?"

        val alertDialog = AlertDialog.Builder(this@AdminSemuaAkunActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        btnHapus.setOnClickListener {
            dialogInputan.dismiss()
            postHapusData(user.id_user)
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    fun postHapusData(id_user:Int){
        ApiService.getRetrofit().postAdminHapusUser("", id_user)
            .enqueue(object: Callback<UserModel>{
                override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                    Toast.makeText(this@AdminSemuaAkunActivity, "Berhasil Hapus Data", Toast.LENGTH_SHORT).show()
                    getData()
//                    loading.alertDialogCancel()
                }

                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    Toast.makeText(this@AdminSemuaAkunActivity, "Gagal Hapus Data", Toast.LENGTH_SHORT).show()
                    getData()
//                    loading.alertDialogCancel()
                }

            })
    }


    override fun onBackPressed() {
        startActivity(Intent(this@AdminSemuaAkunActivity, AdminHomeActivity::class.java))
        finish()
        super.onBackPressed()
    }
}