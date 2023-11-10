package br.com.painelb.api

import androidx.lifecycle.LiveData
import br.com.painelb.db.table.occurrence.OccurrenceType
import br.com.painelb.db.table.occurrence.VehicleType
import br.com.painelb.model.Response
import br.com.painelb.model.UpdateResponse
import br.com.painelb.model.checklist.CreateCheckList
import br.com.painelb.model.login.LoginData
import br.com.painelb.model.login.LoginResponse
import br.com.painelb.model.occurrences.CreateOccurrence
import br.com.painelb.model.registration.RegistrationData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


interface ApiService {

    @POST("auth/login")
    fun loginUser(@Body data: LoginData): LiveData<ApiResponse<LoginResponse>>

    @POST("auth/register")
    fun registrationUser(@Body data: RegistrationData): LiveData<ApiResponse<Response>>

    @GET("type_occurrence")
    fun typeOccurrence(): LiveData<ApiResponse<List<OccurrenceType>>>

    @GET("vehicle_type")
    fun typeVehicle(): LiveData<ApiResponse<List<VehicleType>>>

    @GET("update/{version}")
    fun update(@Path("version") version: String): LiveData<ApiResponse<UpdateResponse>>

    @GET("occurrence/occurrences")
    fun occurrence(): LiveData<ApiResponse<List<CreateOccurrence>>>

    @GET("occurrence/user/{id}")
    fun occurrenceByUserId(@Path("id") id : String): LiveData<ApiResponse<List<CreateOccurrence>>>

    @GET("occurrence/{id}")
    fun occurrenceById(@Path("id") id : String): LiveData<ApiResponse<List<CreateOccurrence>>>

    @GET("occurrence/{id}")
    fun getOccurrenceById(@Path("id") id: Long): LiveData<ApiResponse<CreateOccurrence>>

    @DELETE("occurrence/{id}")
    fun deleteOccurrence(@Path("id") id: Long): LiveData<ApiResponse<Response>>

    @Multipart
    @POST("occurrence/multiple-table")
    fun createOccurrence(
        @Part("occurrenceId") occurrenceId: RequestBody,
        @Part("occurrence") occurrenceData: RequestBody,
        @Part photos: List<MultipartBody.Part>
    ): LiveData<ApiResponse<Response>>

    @Multipart
    @PUT("occurrence/{id}")
    fun updateOccurrence(
        @Path("id") id: Long,
        @Part("occurrenceId") occurrenceId: RequestBody,
        @Part("occurrence") occurrenceData: RequestBody,
        @Part photos: List<MultipartBody.Part>
    ): LiveData<ApiResponse<Response>>

    @GET("checklist")
    fun checklist(): LiveData<ApiResponse<List<CreateCheckList>>>

    @GET("checklist/user/{id}")
    fun checklistByUserId(@Path("id") id:String): LiveData<ApiResponse<List<CreateCheckList>>>

    @Multipart
    @POST("checklist")
    fun createCheckList(
        @Part("checklistId") occurrenceId: RequestBody,
        @Part("checklist") occurrenceData: RequestBody,
        @Part photos: List<MultipartBody.Part>
    ): LiveData<ApiResponse<Response>>

    @DELETE("checklist/{id}")
    fun deleteChecklist(@Path("id") id: Long): LiveData<ApiResponse<Response>>

    @GET("checklist/{id}")
    fun getChecklistById(@Path("id") id: Long): LiveData<ApiResponse<CreateCheckList>>

    @Multipart
    @PUT("checklist/{id}")
    fun updateChecklist(
        @Path("id") id: Long,
        @Part("checklistId") occurrenceId: RequestBody,
        @Part("checklist") occurrenceData: RequestBody,
        @Part photos: List<MultipartBody.Part>
    ): LiveData<ApiResponse<Response>>
}
