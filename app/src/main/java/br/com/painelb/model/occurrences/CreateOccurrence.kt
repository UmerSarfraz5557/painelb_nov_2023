package br.com.painelb.model.occurrences

import android.content.Context
import android.os.Parcelable
import br.com.painelb.R
import br.com.painelb.db.table.occurrence.OccurrencePhoto
import br.com.painelb.util.IntToBoolean
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
data class CreateOccurrence(
    @Json(name = "occurrence")
    val occurrence: Occurrence,
    @Json(name = "occurrence_witness")
    val occurreceWitness: List<OccurreceWitnes>,
    @Json(name = "occurrence_photos")
    val occurrencePhotos: List<OccurrencePhoto>,
    @Json(name = "occurrence_victim")
    val occurrenceVictim: List<OccurrenceVictim>,
    @Json(name = "vehicle_conductor")
    val vehicleConductor: List<VehicleConductor>
) {
    fun shareText(context: Context) =
        StringBuilder().apply {
            append("${context.getString(R.string.occurrence_traffic_accident)} ${occurrence.occurrenceType}\n")
            append("${context.getString(R.string.date_time_heading)} ${occurrence.date} - ${occurrence.time}\n")
            append("${context.getString(R.string.local_heading)} ${occurrence.address}\n")
            append("${context.getString(R.string.report_heading)} ${occurrence.description}\n")
            vehicleConductor.forEachIndexed { index, it ->
                append("===========================\n")
                append("${context.getString(R.string.driver_vehicle_data_heading)} - C${index + 1}\n")
                append("===========================\n")
                append("${context.getString(R.string.board_heading)} ${it.plateVehicle}\n")
                append("${context.getString(R.string.vehicle_heading)} ${it.docVehicleType}\n")
                append("${context.getString(R.string.document_number_heading)} ${it.docVehicleNumber}\n")
                append("${context.getString(R.string.damage_category_heading)} ${it.damageCategory}\n")
                append("\n")
                append("${context.getString(R.string.conductor_heading)} ${it.driverName}\n")
                append("${context.getString(R.string.driver_document_heading)} ${it.driverDocumentType}\n")
                append("${context.getString(R.string.document_number_heading)} ${it.driverDocumentNumber}\n")
                append("${context.getString(R.string.procedure_heading)} ${it.driverProcedure}\n")
            }
            occurrenceVictim.forEachIndexed { index, it ->
                append("===========================\n")
                append("${context.getString(R.string.victim_data_heading)} - V${index + 1}\n")
                append("===========================\n")
                append("${context.getString(R.string.name_heading)} ${it.name}\n")
                append("${context.getString(R.string.health_condition_heading)} ${it.statusVictim}\n")
                append("${context.getString(R.string.gender_heading)} ${it.genre}\n")
                append("${context.getString(R.string.victim_document_heading)} ${it.documentType}\n")
                append("${context.getString(R.string.document_number_heading)} ${it.documentNumber}\n")
                append("${context.getString(R.string.address_heading)} ${it.address}\n")
            }

        }.toString()

    fun shareTextForPdf(context: Context): String {

        val style =  "<style>" +
            "h1 {" +
            "font-size: 23px;" +
            "margin: 15px 0px 15px 0px;"+
            "border-bottom: 4px solid black" +
            "}" +
            "p{" +
            "font-size: 16px;" +
            "margin: 5px 10px 5px 0px;" +
            "text-align: left;" +
            "}" +
            "table {" +
            "font-family: arial, sans-serif;" +
            "border-collapse: collapse;" +
            "width: 100%;" +
            "}" +
            "td,th{" +
            "border: 1px solid #dddddd;" +
            "text-align: left;" +
            "padding: 8px;" +
            "}"+
            "tr:nth-child(even){" +
            "background-color:#dddddd;" +
            "}"+
            "</style>"

        val topHtml =
                "<html>" +
                "<head>" + style +"</head>" +
                "<body>"

        val bottomHtml = "</body>" +
                "</html>"

        return StringBuilder().apply {

            append(topHtml)
            append("<h1 style='font-size: 27px; margin-bottom: 15px; border-bottom: 1px solid #dddddd; text-align: center;'>${context.getString(R.string.type_of_occurrence).toUpperCase()}</h1>")
            append("<p>${context.getString(R.string.occurrence_traffic_accident)} ${occurrence.occurrenceType}</p>")
            append("<p>${context.getString(R.string.date_time_heading)} ${occurrence.date} - ${occurrence.time}</p>")
            append("<p>${context.getString(R.string.local_heading)} ${occurrence.address}</p>")
            append("<p>${context.getString(R.string.report_heading)} ${occurrence.description} </p>")

            vehicleConductor.forEachIndexed { index, it ->
                append("<h1 style='font-size: 23px; margin: 15px 0px 5px 0px; border-bottom: 4px solid black;' >${context.getString(R.string.driver_vehicle_data_heading)}- C${index + 1}</h1>")
                append("<table>")
                append("<tr> <td>${context.getString(R.string.board_heading)}</td><td>${it.plateVehicle}</td></tr>")
                append("<tr style='background-color:#dddddd;'><td>${context.getString(R.string.vehicle_heading)}</td><td>${it.docVehicleType}</td></tr>")
                append("<tr> <td>${context.getString(R.string.document_number_heading)}</td><td>${it.docVehicleNumber}</td></tr>")
                append("<tr style='background-color:#dddddd;'><td>${context.getString(R.string.damage_category_heading)}</td><td>${it.damageCategory}</td></tr>")
                append("<tr> <td>${context.getString(R.string.conductor_heading)}</td><td>${it.driverName}</td></tr>")
                append("<tr style='background-color:#dddddd;'><td>${context.getString(R.string.driver_document_heading)}</td><td>${it.driverDocumentType}</td></tr>")
                append("<tr> <td>${context.getString(R.string.document_number_heading)}</td><td>${it.driverDocumentNumber}</td></tr>")
                append("<tr style='background-color:#dddddd;'><td>${context.getString(R.string.procedure_heading)}</td><td>${it.driverProcedure}</td></tr>")
                append("</table>")
            }

            occurrenceVictim.forEachIndexed { index, it ->
                append("<h1 style='font-size: 23px; margin: 15px 0px 5px 0px; border-bottom: 4px solid black;'>${context.getString(R.string.victim_data_heading)} - V${index + 1}</h1>")
                append("<table>")
                append("<tr><td>${context.getString(R.string.name_heading)}</td><td>${it.name}</td></tr>")
                append("<tr style='background-color:#dddddd;'><td>${context.getString(R.string.health_condition_heading)}</td><td>${it.statusVictim}</td></tr>")
                append("<tr><td>${context.getString(R.string.gender_heading)}</td><td>${it.genre}</td></tr>")
                append("<tr style='background-color:#dddddd';><td>${context.getString(R.string.victim_document_heading)}</td><td>${it.documentType}</td></tr>")
                append("<tr><td>${context.getString(R.string.document_number_heading)}</td><td>${it.documentNumber}</td></tr>")
                append("<tr style='background-color:#dddddd;'><td>${context.getString(R.string.address_heading)}</td><td>${it.address}</td></tr>")
                append("<tr><td>${context.getString(R.string.date_of_birth_heading)}</td><td>${it.birthDate}</td></tr>")
                append("</table>")
            }

        }.append("<h1> ${context.getString(R.string.images_heading)} </h1>").append(bottomHtml).toString()
    }

}

@JsonClass(generateAdapter = true)
data class Occurrence(
    @Json(name = "address")
    val address: String,
    @Json(name = "date")
    val date: String,
    @Json(name = "description")
    val description: String,
    @IntToBoolean
    @Json(name = "has_victim")
    val hasVictim: Boolean,
    @IntToBoolean
    @Json(name = "has_witness")
    val hasWitness: Boolean,
    @Json(name = "occurrence_id")
    val occurrenceId: Long,
    @Json(name = "occurrence_type")
    val occurrenceType: String,
    @Json(name = "perimeter")
    val perimeter: String,
    @Json(name = "time")
    val time: String,
    @Json(name = "users_id")
    val usersId: Long
)

@Parcelize
@JsonClass(generateAdapter = true)
data class OccurreceWitnes(
    @Json(name = "witness_id")
    val witnessId: Long,
    @Json(name = "occurrence_id")
    val occurrenceId: Long,
    @Json(name = "name")
    val name: String,
    @Json(name = "document_type")
    val documentType: String,
    @Json(name = "document_number")
    val documentNumber: String,
    @Json(name = "address")
    val address: String
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class OccurrenceVictim(
    @Json(name = "address")
    val address: String,
    @Json(name = "document_number")
    val documentNumber: String,
    @Json(name = "document_type")
    val documentType: String,
    @Json(name = "genre")
    val genre: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "occurrence_id")
    val occurrenceId: Long,
    @Json(name = "victim_status")
    val statusVictim: String,
    @Json(name = "victim_id")
    val victimId: Long,
    @Json(name ="birthdate")
    var birthDate : String
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class VehicleConductor(
    @Json(name = "damage_category")
    val damageCategory: String,
    @Json(name = "doc_vehicle_number")
    val docVehicleNumber: String,
    @Json(name = "doc_vehicle_type")
    val docVehicleType: String,
    @Json(name = "driver_document_number")
    val driverDocumentNumber: String,
    @Json(name = "driver_document_type")
    val driverDocumentType: String,
    @Json(name = "driver_name")
    val driverName: String,
    @Json(name = "driver_procedure")
    val driverProcedure: String,
    @Json(name = "occurrence_id")
    val occurrenceId: Long,
    @Json(name = "plate_vehicle")
    val plateVehicle: String,
    @Json(name = "vehicle_id")
    val vehicleId: Long,
    @Json(name = "vehicle_procedure")
    val vehicleProcedure: String,
    @Json(name = "vehicle_type")
    val vehicleType: String,
    @Transient
    var isCheck: Boolean = false
) : Parcelable

