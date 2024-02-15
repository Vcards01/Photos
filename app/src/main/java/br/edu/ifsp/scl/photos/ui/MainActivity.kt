package br.edu.ifsp.scl.photos.ui


import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Toast
import br.edu.ifsp.scl.photos.R
import br.edu.ifsp.scl.photos.adapter.PhotoAdapter
import br.edu.ifsp.scl.photos.databinding.ActivityMainBinding
import br.edu.ifsp.scl.photos.model.JsonPlaceHolderAPI
import br.edu.ifsp.scl.photos.model.PhotoItem
import com.android.volley.toolbox.ImageRequest

class MainActivity : AppCompatActivity() {

    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val photoList: MutableList<PhotoItem> = mutableListOf()
    private val photoAdapter: PhotoAdapter by lazy {
        PhotoAdapter(this, photoList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.mainTb.apply {
            title = getString(R.string.app_name)
        })

        amb.photosSp.apply {
            adapter = photoAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    amb.photoIv.setImageDrawable(null)
                    amb.thumbnailIv.setImageDrawable(null)
                    retrivePhotoImage(photoList[position].url, amb.photoIv)
                    retrivePhotoImage(photoList[position].thumbnailUrl, amb.thumbnailIv)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    //NSA
                }
            }
        }

        retrivePhotos()
    }

    private fun retrivePhotos() = JsonPlaceHolderAPI.PhotoRequest({photoList ->
        photoList.also {
            photoAdapter.addAll(it)
        }
    }, {
        Toast.makeText(this, getString(R.string.erro_na_solicita_o), Toast.LENGTH_SHORT).show()
    }).also {
        JsonPlaceHolderAPI.getInstance(this).addToRequestQueue(it)
    }

    private fun retrivePhotoImage(photoUrl: String, imageView: ImageView) =
            ImageRequest(
                photoUrl,
                {response ->
                    imageView.setImageBitmap(response)
                }   ,
                0,
                0,
                ImageView.ScaleType.CENTER,
                Bitmap.Config.ARGB_8888,
                {
                    Toast.makeText(this, getString(R.string.erro_na_solicita_o), Toast.LENGTH_SHORT).show()
                }
            ).also {
                JsonPlaceHolderAPI.getInstance(this).addToRequestQueue(it)
            }
}