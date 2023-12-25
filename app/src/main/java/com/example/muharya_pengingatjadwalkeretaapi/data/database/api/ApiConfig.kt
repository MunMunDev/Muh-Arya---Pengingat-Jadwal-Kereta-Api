package com.example.muharya_pengingatjadwalkeretaapi.data.database.api

import com.example.muharya_pengingatjadwalkeretaapi.data.model.KotaKabModel
import com.example.muharya_pengingatjadwalkeretaapi.data.model.MessageNotifPostModel
import com.example.muharya_pengingatjadwalkeretaapi.data.model.PesananModel
import com.example.muharya_pengingatjadwalkeretaapi.data.model.RuteModel
import com.example.muharya_pengingatjadwalkeretaapi.data.model.StasiunModel
import com.example.muharya_pengingatjadwalkeretaapi.data.model.TiketModel
import com.example.muharya_pengingatjadwalkeretaapi.data.model.UserModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiConfig {

    // GET
    //login
    @GET("api/get.php")
    fun getUser(@Query("cek_user") cek_user: String,
                @Query("username") username: String,
                @Query("password") password: String
        ): Call<ArrayList<UserModel>>

    //pesanan
    @GET("api/get.php")
    fun getPesanan(@Query("get_pesanan") get_pesanan: String,
                   @Query("id_user") id_user: Int
    ): Call<ArrayList<PesananModel>>

    //Tiket
    @GET("api/get.php")
    fun getTiket(@Query("get_tiket") get_tiket: String
    ): Call<ArrayList<TiketModel>>


    // Admin
    //Tiket
    @GET("api/get.php")
    fun getTiketAdmin(@Query("admin_get_tiket") admin_get_tiket: String
    ): Call<ArrayList<TiketModel>>

    @GET("api/get.php")
    fun getTiketAdminPertiket(@Query("admin_get_tiket_pertanggal") admin_get_tiket_pertanggal: String,
                      @Query("tanggal") tanggal: String
    ): Call<ArrayList<TiketModel>>

    // Kota Kab
    @GET("api/get.php")
    fun getKotaKab(@Query("admin_get_kota_kab") admin_get_kota_kab: String
    ): Call<ArrayList<KotaKabModel>>

    // Stasiun
    @GET("api/get.php")
    fun getStasiunAdmin(@Query("admin_get_stasiun") admin_get_stasiun: String
    ): Call<ArrayList<StasiunModel>>

    @GET("api/get.php")
    fun getRuteAdmin(@Query("admin_get_rute") get_rute: String
    ): Call<ArrayList<RuteModel>>

    // Pesanan
    @GET("api/get.php")
    fun getPesananAdmin(@Query("admin_get_pesanan") get_pesanan: String
    ): Call<ArrayList<PesananModel>>

    // Semua User
    @GET("api/get.php")
    fun getSemuaUserAdmin(@Query("admin_get_semua_user") admin_get_semua_user: String
    ): Call<ArrayList<UserModel>>



    // POST
    //Daftar user
    @FormUrlEncoded
    @POST("api/post.php")
    fun postDaftarUser(@Field("daftar") daftar: String,
                       @Field("nama") nama: String,
                       @Field("alamat") alamat: String,
                       @Field("nomor_hp") nomor_hp: String,
                       @Field("username") username: String,
                       @Field("password") password: String,
                       @Field("sebagai") sebagai: String
        ):Call<ArrayList<MessageNotifPostModel>>

    //Update user
    @FormUrlEncoded
    @POST("api/post.php")
    fun postUpdateUser(@Field("update_akun") update_akun: String,
                       @Field("id_user") id_user: Int,
                       @Field("nama") nama: String,
                       @Field("alamat") alamat: String,
                       @Field("nomor_hp") nomor_hp: String,
                       @Field("username") username: String,
                       @Field("password") password: String
    ):Call<UserModel>

    @FormUrlEncoded
    @POST("api/post.php")
    fun postUpdateWaktuNotifikasi(@Field("update_alarm") update_alarm: String,
                                  @Field("id_pesanan") id_pesanan: String,
                                  @Field("waktu_alarm") waktu_alarm: String
    ):Call<PesananModel>

    @FormUrlEncoded
    @POST("api/post.php")
    fun postPesanTiket(@Field("pesan_tiket") pesan_tiket: String,
                       @Field("id_user") id_user: Int,
                       @Field("id_tiket") id_tiket: Int,
                       @Field("dari_kota_kab") dari_kota_kab: String,
                       @Field("dari_stasiun") dari_stasiun: String,
                       @Field("sampai_kota_kab") sampai_kota_kab: String,
                       @Field("sampai_stasiun") sampai_stasiun: String,
                       @Field("tanggal") tanggal: String,
                       @Field("waktu") waktu:String,
                       @Field("jumlah_tiket") jumlah_tiket: String,
                       @Field("harga") harga: String
    ):Call<TiketModel>


    // Rute
    @FormUrlEncoded
    @POST("api/post.php")
    fun postAdminTambahRute(@Field("admin_tambah_rute") admin_tambah_rute: String,
                            @Field("dari_kota_kab") dari_kota_kab: String,
                            @Field("dari_stasiun") dari_stasiun: String,
                            @Field("sampai_kota_kab") sampai_kota_kab: String,
                            @Field("sampai_stasiun") sampai_stasiun: String,
                            @Field("harga") harga: String,
                            @Field("jumlah_tiket") jumlah_tiket: String,
                            @Field("waktu") waktu:String,
                            @Field("waktu_sampai") waktu_sampai:String
    ):Call<RuteModel>

    @FormUrlEncoded
    @POST("api/post.php")
    fun postAdminUpdateRute(@Field("admin_update_rute") admin_update_rute: String,
                            @Field("id_rute") id_rute: Int,
                            @Field("dari_kota_kab") dari_kota_kab: String,
                            @Field("dari_stasiun") dari_stasiun: String,
                            @Field("sampai_kota_kab") sampai_kota_kab: String,
                            @Field("sampai_stasiun") sampai_stasiun: String,
                            @Field("harga") harga: String,
                            @Field("jumlah_tiket") jumlah_tiket: String,
                            @Field("waktu") waktu:String,
                            @Field("waktu_sampai") waktu_sampai:String
    ):Call<RuteModel>

    @FormUrlEncoded
    @POST("api/post.php")
    fun postAdminHapusRute(@Field("admin_hapus_rute") admin_hapus_rute: String,
                            @Field("id_rute") id_rute: Int
    ):Call<RuteModel>


    // Semua Akun
    @FormUrlEncoded
    @POST("api/post.php")
    fun postAdminTambahUser(@Field("admin_tambah_user") admin_tambah_user: String,
                            @Field("nama") nama: String,
                            @Field("alamat") alamat: String,
                            @Field("nomor_hp") nomor_hp: String,
                            @Field("username") username: String,
                            @Field("password") password: String
    ):Call<UserModel>

    @FormUrlEncoded
    @POST("api/post.php")
    fun postAdminUpdateUser(@Field("admin_update_user") admin_update_user: String,
                            @Field("id_user") id_user: Int,
                            @Field("nama") nama: String,
                            @Field("alamat") alamat: String,
                            @Field("nomor_hp") nomor_hp: String,
                            @Field("username") username: String,
                            @Field("password") password: String
    ):Call<UserModel>

    @FormUrlEncoded
    @POST("api/post.php")
    fun postAdminHapusUser(@Field("admin_hapus_user") admin_hapus_user: String,
                           @Field("id_user") id_user: Int
    ):Call<UserModel>



    // Stasiun
    // Tambah Data tiket
    @FormUrlEncoded
    @POST("api/post.php")
    fun postAdminTambahStasiun(@Field("admin_tambah_stasiun") admin_tambah_stasiun: String,
                               @Field("nama_stasiun") nama_stasiun: String,
                               @Field("kota_kab") kota_kab: String
    ):Call<StasiunModel>

    @FormUrlEncoded
    @POST("api/post.php")
    fun postAdminUpdateStasiun(@Field("admin_update_stasiun") admin_update_stasiun: String,
                               @Field("id_stasiun") id_stasiun: Int,
                               @Field("nama_stasiun") nama_stasiun: String,
                               @Field("kota_kab") kota_kab: String
    ):Call<StasiunModel>

    @FormUrlEncoded
    @POST("api/post.php")
    fun postAdminHapusStasiun(@Field("admin_hapus_stasiun") admin_hapus_stasiun: String,
                           @Field("id_stasiun") id_stasiun: Int
    ):Call<StasiunModel>


    // Kota / Kab
    @FormUrlEncoded
    @POST("api/post.php")
    fun postAdminTambahKotaKab(@Field("admin_tambah_kota_kab") admin_tambah_kota_kab: String,
                               @Field("kota_kab") kota_kab: String
    ):Call<KotaKabModel>

    @FormUrlEncoded
    @POST("api/post.php")
    fun postAdminUpdateKotaKab(@Field("admin_update_kota_kab") admin_update_kota_kab: String,
                               @Field("id_kota_kab") id_kota_kab: Int,
                               @Field("kota_kab") kota_kab: String
    ):Call<KotaKabModel>

    @FormUrlEncoded
    @POST("api/post.php")
    fun postAdminHapusKotaKab(@Field("admin_hapus_kota_kab") admin_hapus_kota_kab: String,
                              @Field("id_kota_kab") id_kota_kab: Int
    ):Call<KotaKabModel>


    // Tiket
    // Tambah Perminggu
    // Tambah Data Perminggu
    @FormUrlEncoded
    @POST("api/post.php")
    fun postAdminTambahTiketPermingguStasiun(@Field("admin_tambah_tiket_perminggu") admin_tambah_tiket_perminggu: String
    ):Call<TiketModel>

    // Tambah Data
    @FormUrlEncoded
    @POST("api/post.php")
    fun postAdminTambahTiket(@Field("admin_tambah_tiket") admin_tambah_tiket: String,
                             @Field("tanggal") tanggal:String,
                             @Field("waktu") waktu:String,
                             @Field("dari_kota_kab") dari_kota_kab: String,
                             @Field("dari_stasiun") dari_stasiun: String,
                             @Field("sampai_kota_kab") sampai_kota_kab: String,
                             @Field("sampai_stasiun") sampai_stasiun: String,
                             @Field("harga") harga: String,
                             @Field("jumlah_tiket") jumlah_tiket: String

    ):Call<TiketModel>

    @FormUrlEncoded
    @POST("api/post.php")
    fun postAdminUpdateTiket(@Field("admin_update_tiket") admin_update_tiket: String,
                             @Field("id_tiket") id_tiket: Int,
                             @Field("tanggal") tanggal:String,
                             @Field("waktu") waktu:String,
                             @Field("dari_kota_kab") dari_kota_kab: String,
                             @Field("dari_stasiun") dari_stasiun: String,
                             @Field("sampai_kota_kab") sampai_kota_kab: String,
                             @Field("sampai_stasiun") sampai_stasiun: String,
                             @Field("harga") harga: String,
                             @Field("jumlah_tiket") jumlah_tiket: String

    ):Call<TiketModel>

    @FormUrlEncoded
    @POST("api/post.php")
    fun postAdminHapusTiket(@Field("admin_hapus_tiket") admin_hapus_tiket: String,
                           @Field("id_tiket") id_tiket: Int
    ):Call<TiketModel>
}
