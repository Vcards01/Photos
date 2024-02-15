package br.edu.ifsp.scl.photos.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import br.edu.ifsp.scl.photos.model.PhotoItem

class PhotoAdapter(
    private val activityContext: Context,
    private val photoList: MutableList<PhotoItem>
): ArrayAdapter<PhotoItem>(activityContext, android.R.layout.simple_list_item_1, photoList) {

    private data class PhotoHolder( val photoTileTv: TextView)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val productView = convertView ?: LayoutInflater.from(activityContext)
            .inflate(android.R.layout.simple_list_item_1, parent, false).apply {
                tag = PhotoHolder(findViewById(android.R.id.text1))
            }

        (productView.tag as PhotoHolder).photoTileTv.text = photoList[position].title

        return productView
    }
}