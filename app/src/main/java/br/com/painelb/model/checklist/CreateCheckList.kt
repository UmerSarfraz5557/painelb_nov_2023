package br.com.painelb.model.checklist

import android.content.Context
import br.com.painelb.R
import br.com.painelb.db.table.checklist.ChecklistPhotos
import br.com.painelb.util.IntToBoolean
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateCheckList(
    @Json(name = "checklist")
    val checklist: Checklist,
    @Json(name = "checklist_photos")
    val checklistPhotos: List<ChecklistPhotos>
){

    val rowStart = "<tr><td>"
    val rowEnd =   "</td></tr>"
    val rowBetween = "</td><td>"

    fun shareText(context: Context) =
        StringBuilder().apply {
            append("==============================\n")
            append("${context.getString(R.string.title_checklist)} \n")
            append("==============================\n")
            append("${context.getString(R.string.date)} ${checklist.checkDate} \n")
            append("${context.getString(R.string.team_service_heading)} ${checklist.teamService}\n")
            append("${context.getString(R.string.conductor_heading)} ${checklist.conductor}\n")
            append("${context.getString(R.string.board_heading)} ${checklist.plate}\n")
            append("${context.getString(R.string.prisma_heading)} ${checklist.prisma}\n")
            append("${context.getString(R.string.departure_time_heading)} ${checklist.departureTime}\n")
            append("${context.getString(R.string.return_time_heading)} ${checklist.returnTime}\n")
            append("${context.getString(R.string.km_departure_heading)} ${checklist.kmDeparture}\n")
            append("${context.getString(R.string.return_km_heading)} ${checklist.kmReturn}\n")
            append("${context.getString(R.string.output_fuel_quant_heading)} ${checklist.outputFuelQuantity}\n")
            append("${context.getString(R.string.return_fuel_quant_heading)} ${checklist.returnFuelQuantity}\n")
            append("==============================\n")
            append("${context.getString(R.string.check_list_heading)} \n")
            append("==============================\n")
            append("${context.getString(R.string.carpet_heading)} ${trueOrFalse(checklist.carpet)} \n")
            append("${context.getString(R.string.triangue_heading)} ${trueOrFalse(checklist.triangue)} \n")
            append("${context.getString(R.string.front_lightning_system_heading)} ${trueOrFalse(checklist.frontLightingSystem)} \n")
            append("${context.getString(R.string.rear_lighting_heading)} ${trueOrFalse(checklist.backLightingSystem)} \n")
            append("${context.getString(R.string.monkey_heading)} ${trueOrFalse(checklist.monkey)} \n")
            append("${context.getString(R.string.take_iron_heading)} ${trueOrFalse(checklist.tireIron)} \n")
            append("${context.getString(R.string.tires_heading)} ${trueOrFalse(checklist.pneus)} \n")
            append("${context.getString(R.string.sirine_heading)} ${trueOrFalse(checklist.sirene)} \n")
            append("${context.getString(R.string.flashing_heading)} ${trueOrFalse(checklist.flashing)} \n")
            append("${context.getString(R.string.crlv_heading)} ${trueOrFalse(checklist.crlv)} \n")
            append("${context.getString(R.string.supply_card_heading)} ${trueOrFalse(checklist.supplyCard)} \n")
            append("${context.getString(R.string.stereo_heading)} ${trueOrFalse(checklist.stereo)} \n")
            append("${context.getString(R.string.etiliometer_heading)} ${trueOrFalse(checklist.etilometer)} \n")
            append("${context.getString(R.string.glacier_heading)} ${trueOrFalse(checklist.glacier)} \n")
            append("${context.getString(R.string.cones_heading)} ${changeValue(checklist.cones, checklist.conesQuantities)} \n")
            append("${context.getString(R.string.super_cones_heading)} ${changeValue(checklist.superCones, checklist.superConesQuantities)} \n")
            append("${context.getString(R.string.new_jersy_heading)} ${changeValue(checklist.newJersey, checklist.newJerseyQuantities)} \n")
            append("${context.getString(R.string.hanlde_heading)} ${changeValue(checklist.handle, checklist.handleQuantities)} \n")
        }.toString()

    fun shareTextForPdf(context: Context): String{
        val style =  "<style>" +
                "h1 {" +
                "font-size: 20px;" +
                "margin-top: 5px;" +
                "margin-bottom: 5px;" +
                "border-left: 4px solid #f2f2f2;" +
                "text-align: left;" +
                "}" +
                "h2 {" +
                "font-size: 16px;" +
                "text-align: center;" +
                "}" +
                ".th_background{" +
                "background-color: #f2f2f2" +
                "}" +
                "p{" +
                "font-size: 16px;" +
                "margin: 5px 10px 5px 10px;" +
                "text-align: right;" +
                "}" +
                ".p_left {" +
                "border-right-color: white;" +
                "}" +
                ".p_center {" +
                "text-align: center;" +
                "}" +
                ".p_right {" +
                "text-align: right;" +
                "border-left-color: white;" +
                "}" +
                "table {" +
                "font-family: arial, sans-serif;" +
                "border-collapse: collapse;" +
                "width: 100%;" +
                "}" +
                "td,th{" +
                "border: 1px solid #dddddd;" +
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
            append("<h1>${context.getString(R.string.title_checklist)}</h1>\n\n")

            append("<table>\n")

                append("<tr><td colspan='4'>${context.getString(R.string.conductor_heading)} ${checklist.conductor}</td></tr>\n")

                append("<tr><td colspan='2' class='p_left'>${context.getString(R.string.team_service_heading)} ${checklist.teamService}</td>\n")
                append("<td colspan='2' class='p_right'>${context.getString(R.string.date)}: ${checklist.checkDate}</td></tr>\n")

                append("$rowStart ${context.getString(R.string.board_heading)}</td>\n")
                append("<td>${checklist.plate}</td>\n")
                append("<td>${context.getString(R.string.prisma_heading)}</td>\n")
                append("<td>${checklist.prisma} $rowEnd\n")

                append("$rowStart ${context.getString(R.string.departure_time_heading)}</td>\n")
                append("<td>${checklist.departureTime}</td>\n")
                append("<td>${context.getString(R.string.return_time_heading)}</td>\n")
                append("<td>${checklist.returnTime} $rowEnd\n")

                append("$rowStart ${context.getString(R.string.km_departure_heading)}</td>\n")
                append("<td>${checklist.kmDeparture}</td>\n")
                append("<td>${context.getString(R.string.return_km_heading)}</td>\n")
                append("<td>${checklist.kmReturn} $rowEnd\n")

                append("$rowStart ${context.getString(R.string.output_fuel_quant_heading)}</td>\n")
                append("<td>${checklist.outputFuelQuantity}</td>\n")
                append("<td>${context.getString(R.string.return_fuel_quant_heading)}</td>\n")
                append("<td>${checklist.returnFuelQuantity} $rowEnd\n\n")

            append("</table>\n")

            append("<h1>${context.getString(R.string.check_list_heading)}</h1>\n\n")

            append("<table>")

            append("<tr class='th_background'><td colspan='4'><h2>OK - Existe | NT - Não tem | D - Danificado</h2></td></tr>\n")

            append("$rowStart ${context.getString(R.string.carpet_heading)}</td>\n")
            append("<td class='p_center'>${trueOrFalse(checklist.carpet)}</td>\n")
            append("<td>${context.getString(R.string.tire_iron)}</td>\n")
            append("<td class='p_center'>${trueOrFalse(checklist.tireIron)} $rowEnd\n")

            append("$rowStart ${context.getString(R.string.triangue_heading)}</td>\n")
            append("<td class='p_center'>${trueOrFalse(checklist.triangue)}</td>\n")
            append("<td>${context.getString(R.string.monkey_heading)}</td>\n")
            append("<td class='p_center'>${trueOrFalse(checklist.monkey)} $rowEnd\n")

            append("$rowStart ${context.getString(R.string.front_lightning_system_heading)}</td>\n")
            append("<td class='p_center'>${trueOrFalse(checklist.frontLightingSystem)}</td>\n")
            append("<td>${context.getString(R.string.rear_lighting_heading)}</td>\n")
            append("<td class='p_center'>${trueOrFalse(checklist.backLightingSystem)} $rowEnd\n")

            append("$rowStart ${context.getString(R.string.sirine_heading)}</td>\n")
            append("<td class='p_center'>${trueOrFalse(checklist.sirene)}</td>\n")
            append("<td>${context.getString(R.string.flashing_heading)}</td>\n")
            append("<td class='p_center'>${trueOrFalse(checklist.flashing)} $rowEnd\n")

            append("$rowStart ${context.getString(R.string.crlv_heading)}</td>\n")
            append("<td class='p_center'>${trueOrFalse(checklist.crlv)}</td>\n")
            append("<td>${context.getString(R.string.supply_card_heading)}</td>\n")
            append("<td class='p_center'>${trueOrFalse(checklist.supplyCard)} $rowEnd\n")

            append("$rowStart ${context.getString(R.string.tires_heading)}</td>\n")
            append("<td class='p_center'>${trueOrFalse(checklist.pneus)}</td>\n")
            append("<td>${context.getString(R.string.take_iron_heading)}</td>\n")
            append("<td class='p_center'>${trueOrFalse(checklist.tireIron)} $rowEnd\n")

            append("$rowStart ${context.getString(R.string.stereo_heading)}</td>\n")
            append("<td class='p_center'>${trueOrFalse(checklist.stereo)}</td>\n")
            append("<td>${context.getString(R.string.etiliometer_heading)}</td>\n")
            append("<td class='p_center'>${trueOrFalse(checklist.etilometer)} $rowEnd\n")

            append("$rowStart ${context.getString(R.string.glacier_heading)}</td>\n")
            append("<td class='p_center'>${trueOrFalse(checklist.glacier)}</td>\n")
            append("<td>${context.getString(R.string.cones_heading)}</td>\n")
            append("<td class='p_center'>${changeValue(checklist.cones, checklist.conesQuantities)} $rowEnd\n")

            append("$rowStart ${context.getString(R.string.super_cones_heading)}</td>\n")
            append("<td class='p_center'>${changeValue(checklist.superCones, checklist.superConesQuantities)}</td>\n")
            append("<td>${context.getString(R.string.new_jersy_heading)}</td>\n")
            append("<td class='p_center'>${changeValue(checklist.newJersey, checklist.newJerseyQuantities)} $rowEnd\n")

            append("$rowStart ${context.getString(R.string.hanlde_heading)}</td>\n")
            append("<td class='p_center'>${changeValue(checklist.handle, checklist.handleQuantities)}</td>\n")
            append("<td>Guincho</td>\n")
            append("<td>$rowEnd\n")
            append("<tr><td colspan='4'>${context.getString(R.string.observations)}: ${checklist.observation_quantities}</td></tr>\n")

            append("</table>\n")

        }.append("<h1> ${context.getString(R.string.images_heading)} </h1>").append(bottomHtml).toString()

    }

    fun changeValue(checklist: Boolean, checklistQtd: String): String {
        val quantity: String
        if (checklist) { quantity = checklistQtd } else { quantity = "NT" }
        return quantity
    }

    fun trueOrFalse(checklist: Boolean): String {
        val value: String
        if (checklist) {
            value = "OK"
        } else {
            value = "NT"
        }
        return value
    }

}

@JsonClass(generateAdapter = true)
data class Checklist(
    @Json(name = "driver")
    val conductor: String,
    @IntToBoolean
    @Json(name = "back_lighting_system")
    val backLightingSystem: Boolean,
    @IntToBoolean
    @Json(name = "carpet")
    val carpet: Boolean,
    @Json(name = "check_date")
    val checkDate: String,
    @Json(name = "checklist_id")
    val checklistId: Long,
    @IntToBoolean
    @Json(name = "cones")
    val cones: Boolean,
    @Json(name = "cones_quantities")
    val conesQuantities: String,
    @IntToBoolean
    @Json(name = "crlv")
    val crlv: Boolean,
    @Json(name = "departure_time")
    val departureTime: String,
    @IntToBoolean
    @Json(name = "etilometer")
    val etilometer: Boolean,
    @IntToBoolean
    @Json(name = "flashing")
    val flashing: Boolean,
    @IntToBoolean
    @Json(name = "front_lighting_system")
    val frontLightingSystem: Boolean,
    @IntToBoolean
    @Json(name = "glacier")
    val glacier: Boolean,
    @IntToBoolean
    @Json(name = "handle")
    val handle: Boolean,
    @Json(name = "handle_quantities")
    val handleQuantities: String,
    @Json(name = "observations")
    val observation_quantities: String,
    @Json(name = "km_departure")
    val kmDeparture: String,
    @Json(name = "km_return")
    val kmReturn: String,
    @IntToBoolean
    @Json(name = "monkey")
    val monkey: Boolean,
    @IntToBoolean
    @Json(name = "new_jersey")
    val newJersey: Boolean,
    @Json(name = "new_jersey_quantities")
    val newJerseyQuantities: String,
    @Json(name = "output_fuel_quantity")
    val outputFuelQuantity: String,
    @Json(name = "plate")
    val plate: String,
    @IntToBoolean
    @Json(name = "pneus")
    val pneus: Boolean,
    @Json(name = "prisma")
    val prisma: String,
    @Json(name = "return_fuel_quantity")
    val returnFuelQuantity: String,
    @Json(name = "return_time")
    val returnTime: String,
    @IntToBoolean
    @Json(name = "sirene")
    val sirene: Boolean,
    @IntToBoolean
    @Json(name = "stereo")
    val stereo: Boolean,
    @IntToBoolean
    @Json(name = "super_cones")
    val superCones: Boolean,
    @Json(name = "super_cones_quantities")
    val superConesQuantities: String,
    @IntToBoolean
    @Json(name = "supply_card")
    val supplyCard: Boolean,
    @IntToBoolean
    @Json(name = "tire_iron")
    val tireIron: Boolean,
    @IntToBoolean
    @Json(name = "triangue")
    val triangue: Boolean,
    @Json(name = "users_id")
    val usersId: Long,
    @Json(name = "team_service")
    val teamService: String
)
