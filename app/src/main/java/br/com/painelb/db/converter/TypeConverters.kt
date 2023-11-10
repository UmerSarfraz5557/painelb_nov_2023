package br.com.painelb.db.converter

import androidx.room.TypeConverter
import br.com.painelb.model.checklist.Checklist
import br.com.painelb.model.occurrences.OccurreceWitnes
import br.com.painelb.model.occurrences.Occurrence
import br.com.painelb.model.occurrences.OccurrenceVictim
import br.com.painelb.model.occurrences.VehicleConductor
import br.com.painelb.util.IntToBooleanAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class OccurrenceTypeConverters {
    @TypeConverter
    fun stringToOccurrence(json: String): Occurrence {
        val moshi = Moshi.Builder().add(IntToBooleanAdapter()).build()
        val adapter = moshi.adapter(Occurrence::class.java)
        return adapter.fromJson(json)!!
    }

    @TypeConverter
    fun occurrenceToString(data: Occurrence): String {
        val moshi = Moshi.Builder().add(IntToBooleanAdapter()).build()
        val adapter = moshi.adapter(Occurrence::class.java)
        return adapter.toJson(data)
    }
}


class OccurrenceVictimTypeConverters {
    @TypeConverter
    fun stringToOccurrenceVictim(json: String): List<OccurrenceVictim> {
        val moshi = Moshi.Builder().build()
        val listMyData =
            Types.newParameterizedType(List::class.java, OccurrenceVictim::class.java)
        val adapter =
            moshi.adapter<List<OccurrenceVictim>>(listMyData)
        return adapter.fromJson(json)!!
    }

    @TypeConverter
    fun occurrenceVictimToString(list: List<OccurrenceVictim>): String {
        val moshi = Moshi.Builder().build()
        val listMyData =
            Types.newParameterizedType(List::class.java, OccurrenceVictim::class.java)
        val adapter =
            moshi.adapter<List<OccurrenceVictim>>(listMyData)
        return adapter.toJson(list)
    }
}


class OccurreceWitnesTypeConverters {
    @TypeConverter
    fun stringToOccurreceWitnes(json: String): List<OccurreceWitnes> {
        val moshi = Moshi.Builder().build()
        val listMyData =
            Types.newParameterizedType(List::class.java, OccurreceWitnes::class.java)
        val adapter =
            moshi.adapter<List<OccurreceWitnes>>(listMyData)
        return adapter.fromJson(json)!!
    }

    @TypeConverter
    fun occurreceWitnesToString(list: List<OccurreceWitnes>): String {
        val moshi = Moshi.Builder().build()
        val listMyData =
            Types.newParameterizedType(List::class.java, OccurreceWitnes::class.java)
        val adapter =
            moshi.adapter<List<OccurreceWitnes>>(listMyData)
        return adapter.toJson(list)
    }
}

class VehicleConductorTypeConverters {
    @TypeConverter
    fun stringToVehicleConductor(json: String): List<VehicleConductor> {
        val moshi = Moshi.Builder().build()
        val listMyData =
            Types.newParameterizedType(List::class.java, VehicleConductor::class.java)
        val adapter =
            moshi.adapter<List<VehicleConductor>>(listMyData)
        return adapter.fromJson(json)!!
    }

    @TypeConverter
    fun vehicleConductorToString(list: List<VehicleConductor>): String {
        val moshi = Moshi.Builder().build()
        val listMyData =
            Types.newParameterizedType(List::class.java, VehicleConductor::class.java)
        val adapter =
            moshi.adapter<List<VehicleConductor>>(listMyData)
        return adapter.toJson(list)
    }
}

class ChecklistTypeConverters {

    @TypeConverter
    fun stringToChecklist(json: String): Checklist {
        val moshi = Moshi.Builder().add(IntToBooleanAdapter()).build()
        val adapter = moshi.adapter(Checklist::class.java)
        return adapter.fromJson(json)!!
    }

    @TypeConverter
    fun checklistToString(data: Checklist): String {
        val moshi = Moshi.Builder().add(IntToBooleanAdapter()).build()
        val adapter = moshi.adapter(Checklist::class.java)
        return adapter.toJson(data)
    }
}