package br.com.painelb.util

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter

class NonFilterArrayAdapter<T> : ArrayAdapter<T> {
    constructor(context: Context, resource: Int) : super(context, resource) {}
    constructor(context: Context, resource: Int, textViewResourceId: Int) : super(
        context,
        resource,
        textViewResourceId
    )

    constructor(context: Context, resource: Int, objects: Array<T>) : super(
        context,
        resource,
        objects
    )

    constructor(
        context: Context,
        resource: Int,
        textViewResourceId: Int,
        objects: Array<T>
    ) : super(context, resource, textViewResourceId, objects)

    constructor(
        context: Context,
        resource: Int,
        objects: List<T>
    ) : super(context, resource, objects)

    constructor(
        context: Context,
        resource: Int,
        textViewResourceId: Int,
        objects: List<T>
    ) : super(context, resource, textViewResourceId, objects)

    override fun getFilter(): Filter {
        return ArrayFilter()
    }

    private class ArrayFilter : Filter() {
        override fun performFiltering(prefix: CharSequence?): FilterResults = FilterResults()

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        }
    }
}
