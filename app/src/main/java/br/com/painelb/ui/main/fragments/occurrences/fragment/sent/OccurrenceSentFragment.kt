package br.com.painelb.ui.main.fragments.occurrences.fragment.sent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.painelb.R
import br.com.painelb.base.ui.BaseFragment
import br.com.painelb.databinding.FragmentOccurrenceSentBinding
import br.com.painelb.di.modules.viemodel.AppViewModelFactory
import br.com.painelb.domain.result.EventObserver
import br.com.painelb.model.ItemType
import br.com.painelb.network.Status
import br.com.painelb.ui.main.fragments.occurrences.OccurrencesFragmentDirections
import br.com.painelb.util.dividerItemDecorationVertical
import br.com.painelb.util.navigate
import br.com.painelb.util.positiveButton
import br.com.painelb.util.showDialog
import com.itextpdf.text.pdf.PdfWriter
import timber.log.Timber
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build


import android.os.ParcelFileDescriptor
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import br.com.painelb.domain.result.Event
import br.com.painelb.model.occurrences.CreateOccurrence
import com.bumptech.glide.Glide
import com.itextpdf.text.*

import com.shockwave.pdfium.PdfDocument

import com.shockwave.pdfium.PdfiumCore
import com.itextpdf.tool.xml.XMLWorkerHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*

import com.itextpdf.tool.xml.css.StyleAttrCSSResolver

import com.itextpdf.tool.xml.pipeline.css.CSSResolver
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline

import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline

import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline

import com.itextpdf.tool.xml.html.Tags

import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext
import com.itextpdf.tool.xml.XMLWorker
import com.itextpdf.tool.xml.parser.XMLParser

import com.bumptech.glide.load.engine.GlideException

import com.bumptech.glide.request.RequestListener

import com.bumptech.glide.load.DataSource
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlin.collections.ArrayList


class OccurrenceSentFragment :
    BaseFragment<FragmentOccurrenceSentBinding>(R.layout.fragment_occurrence_sent) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val viewModel by viewModels<OccurrencesSentViewModel> { viewModelFactory }
    lateinit var createOccirence : CreateOccurrence
    lateinit var bottomSheetDialog: BottomSheetDialog
    var filteredList = ArrayList<CreateOccurrence>()
    var actualList = ArrayList<CreateOccurrence>()

    private var isFirstTime = true;
    private var textShare : String = ""
    var imageFile  : File? = null
    var pdfFile  : File? = null


    companion object{
        var toReloadData = false
    }

    private val CSS =
            "h1 {" +
            "font-size: 23px;" +
            "padding: 15px 0px 15px 0px;"+
            "border-bottom: 4px solid black;" +
            "}" +
            "p{" +
            "font-size: 16px;" +
            "margin: 5px 10px 5px 10px;" +
            "text-align: right;" +
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
            "}"

    private lateinit var occurrenceAdapter: OccurrenceAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = viewModel
        mBinding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
        viewModel.refreshState.observe(viewLifecycleOwner, Observer {
            mBinding.swipeRefresh.isRefreshing = it == Status.LOADING
        })

        mBinding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }

        viewModel.navigateToLogoutDialog.observe(viewLifecycleOwner, EventObserver {
          showDeleteConfirmationDialog(it)
        })

        viewModel.messageEvent.observe(viewLifecycleOwner, EventObserver {
            run {
                showDialog {
                    setMessage(it)
                    positiveButton(context.getString(R.string.ok))
                }
            }
        })
        occurrenceAdapter = OccurrenceAdapter(viewModel, savedInstanceState)
        mBinding.recyclerView.apply {
            adapter = occurrenceAdapter
            addItemDecoration(context.dividerItemDecorationVertical)
        }

        viewModel.occurrenceItemLiveData.observe(viewLifecycleOwner, Observer {

            if (it != null){
                actualList.clear()
                for (occurence in it){
                    actualList.add(occurence)
                }
            }


            occurrenceAdapter.submitList(it) {
                val layoutManager =
                    (mBinding.recyclerView.layoutManager as LinearLayoutManager)
                val position = layoutManager.findFirstCompletelyVisibleItemPosition()
                if (position != RecyclerView.NO_POSITION) mBinding.recyclerView.scrollToPosition(
                    position
                )
            }
        })

        viewModel.navigateToUpdate.observe(viewLifecycleOwner, EventObserver {
            navigate(
                OccurrencesFragmentDirections.navigateToCreateOccurrencesFragment(
                    it,
                    ItemType.REMOTE
                )
            )
        })
        viewModel.shareEvent.observe(viewLifecycleOwner, EventObserver {
            textShare = it



        })

        viewModel.sharePdfEvent.observe(viewLifecycleOwner, EventObserver {
            Log.d("Pdf","Started")
            savepdf(it)
            showBottomSheetDialogue()

        })

        viewModel.createOccurrence.observe(viewLifecycleOwner, EventObserver {
            createOccirence = it
        })


        mBinding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty()){
                    filteredList.clear()
                    for (occurrence in actualList){
                        if (occurrence.occurrence.description.toString().toLowerCase().contains(newText.toString().toLowerCase()) || occurrence.occurrence.occurrenceType.toString().toLowerCase().contains(newText.toString().toLowerCase()) ){
                            filteredList.add(occurrence)
                        }
                    }

                    if(filteredList.size > 0){
                        occurrenceAdapter.submitList(filteredList.toList())
                    }else{
                        occurrenceAdapter.submitList(actualList.toList())
                    }
                }else{
                    occurrenceAdapter.submitList(actualList.toList())
                }

                println("New text${newText.length}")
                return false
            }
        })

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        occurrenceAdapter.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        if(toReloadData){
            viewModel.refresh()
        }
        toReloadData  = false;
    }

    private fun showShareDialog(data: String, typeOfDoc:String) {
        Timber.d(data)
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, data)
            type = typeOfDoc
        }
        val shareIntent = Intent.createChooser(sendIntent, getString(R.string.occurrences))
        startActivity(shareIntent)
    }


    private fun showShareDialog( imageOrPdf :File , typeOfDoc:String) {
        val uri = if (Build.VERSION.SDK_INT < 24) Uri.fromFile(imageOrPdf) else Uri.parse(imageOrPdf.path)
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = typeOfDoc
            putExtra(Intent.EXTRA_STREAM, uri)
//            putExtra(
//                Intent.EXTRA_TEXT,
//                "Sharing Bill purchase items..."
//            )
        }
        startActivity(Intent.createChooser(shareIntent, "Share Via"))
    }

    private fun showBottomSheetDialogue(){
        bottomSheetDialog = BottomSheetDialog(activity as Context, R.style.BottomSheetDailougeTheme)
        val bottomSheet = LayoutInflater.from(activity!!.applicationContext)
            .inflate(R.layout.bottom_sheet_layout, activity!!.findViewById(R.id.bottom_sheet_container))
        bottomSheet.background =
            ContextCompat.getDrawable(activity as Context, R.drawable.bottom_sheet_background)

        bottomSheet.findViewById<LinearLayout>(R.id.text_share_ll).setOnClickListener {
           showShareDialog(textShare,"text/plain")
        }

        bottomSheet.findViewById<LinearLayout>(R.id.pdf_share_ll).setOnClickListener {
            if (pdfFile!= null){
                showShareDialog(pdfFile!!,"application/pdf")
            }

        }

        bottomSheet.findViewById<LinearLayout>(R.id.image_share_ll).setOnClickListener {
            if (imageFile!= null){
                showShareDialog(imageFile!!,"image/jpeg")
            }
        }

        bottomSheetDialog.setContentView(bottomSheet)
        bottomSheetDialog.show()
    }




    private fun savepdf(text : String) {
        lifecycleScope.launch(Dispatchers.IO) {
           savePdfSuspend(text)
        }
    }

    suspend fun savePdfSuspend(text: String){
        Log.d("Html",text)
        val doc = Document()
        val file: String = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(System.currentTimeMillis())
        val filepath: String =
            getAppSpecificAlbumStorageDir(activity as Context,"Painelb/pdf")?.absolutePath + "/" + file + ".pdf"
        val smallBold = Font(Font.FontFamily.TIMES_ROMAN, 12f)

        try {
            val writer =  PdfWriter.getInstance(doc, FileOutputStream(filepath))
            doc.open()

            // CSS Resolver
            val cssResolver: CSSResolver = StyleAttrCSSResolver()
            val cssFile = XMLWorkerHelper.getCSS(ByteArrayInputStream(CSS.toByteArray()))
            cssResolver.addCss(cssFile)

            // HTML
            val htmlContext = HtmlPipelineContext(null)
            htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory())

            // Pipelines

            // Pipelines
            val pdf = PdfWriterPipeline(doc, writer)
            val html = HtmlPipeline(htmlContext, pdf)
            val css = CssResolverPipeline(cssResolver, html)

            val worker = XMLWorker(css, true)
            val p = XMLParser(worker)
            p.parse(text.byteInputStream())


            createOccirence.occurrencePhotos.forEachIndexed { index, occurrencePhoto ->
                 Glide.with((activity as Context)).asBitmap().load(occurrencePhoto.photo)
                    .listener(object : RequestListener<Bitmap?> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: com.bumptech.glide.request.target.Target<Bitmap?>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.d("PDF","Image loading failed")
                            if (index == createOccirence.occurrencePhotos.lastIndex){
                                endDoc(doc, filepath, file)
                            }
                            return false
                        }

                        override fun onResourceReady(
                            resource: Bitmap?,
                            model: Any?,
                            target: com.bumptech.glide.request.target.Target<Bitmap?>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            if (resource!= null){
                                var newBitmap = resource!!
                                val imagePath =  getAppSpecificAlbumStorageDir(activity as Context,"Painelb/temp")?.absolutePath + "/"+ "20211106_024947"+".png"
                                val bitmapFile : File = File(imagePath)
                                val fileOuputStream = FileOutputStream(bitmapFile)
                                newBitmap = Bitmap.createScaledBitmap(newBitmap,100,100,true);
                                newBitmap.compress(Bitmap.CompressFormat.PNG, 70, fileOuputStream)
                                fileOuputStream.close()
                                val i = Image.getInstance(imagePath)
                                var c = Chunk(i, 0f, -80f)
                                doc.add(c)
                                c = Chunk("     ")
                                doc.add(c)

                                if (index == createOccirence.occurrencePhotos.lastIndex){
                                    endDoc(doc, filepath, file)
                                }
                            }
                            Log.d("PDF","Image loading Success")
                           return true
                        }

                    }).submit()

            }

           // endDoc(doc, filepath, file)
            if(createOccirence.occurrencePhotos.isEmpty()){
              endDoc(doc, filepath, file)
            }

        } catch (e: Exception) {
            Log.d("Pdf","This is Error msg : " + e.message)

        }
    }

    fun getAppSpecificAlbumStorageDir(context: Context, albumName: String): File? {
        // Get the pictures directory that's inside the app-specific directory on
        // external storage.
        val file = File(context.getExternalFilesDir(
            Environment.DIRECTORY_DOCUMENTS), albumName)
        if (!file?.mkdirs()) {
            Log.e("Pdf", "Directory not created")
        }
        return file
    }

    private fun getBitmap(file: File?): Bitmap? {
        var pageNum = 0
        val pdfiumCore = PdfiumCore(context)
        try {
            var bitmapList = ArrayList<Bitmap>()
            val pdfDocument: PdfDocument = pdfiumCore.newDocument(openFile(file))

            while (pageNum < pdfiumCore.getPageCount(pdfDocument)){
                Log.d("UmerPdf","${pdfiumCore.getPageCount(pdfDocument)}")
                pdfiumCore.openPage(pdfDocument, pageNum)
                val w =  pdfiumCore.getPageWidthPoint(pdfDocument, pageNum)
                val h = pdfiumCore.getPageHeightPoint(pdfDocument, pageNum)

                Log.d("UmerPdf","${w}  ${h}")
                val bitmap = Bitmap.createBitmap(
                    w, h,
                    Bitmap.Config.RGB_565
                )
                pdfiumCore.renderPageBitmap(
                    pdfDocument, bitmap, pageNum,0, 0,
                    w,h)
                Log.d("UmerPdf2","O  ${w}  ${h}")

                bitmapList.add(bitmap)
                pageNum++
            }

            Log.d("UmerPdf","${bitmapList.size}")

            //if you need to render annotations and form fields, you can use
            //the same method above adding 'true' as last param
            pdfiumCore.closeDocument(pdfDocument) // important!
            return combineImageIntoOne(bitmapList)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return null
    }

    private fun openFile(file: File?): ParcelFileDescriptor? {
        return try {
                ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                return null
            }
    }

  private fun endDoc(doc: Document, filePath : String, file:String) {
      doc.close()
      val pdfFile = File(filePath)
      this.pdfFile = pdfFile
      Log.d("Pdf","$file.pdf is saved to $filePath")
      val bitmap =  getBitmap(pdfFile)
      if (bitmap != null){
          val imagePath =  getAppSpecificAlbumStorageDir(activity as Context,"Painelb/img")?.absolutePath + "/"+ file+".png"
          val bitmapFile : File = File(imagePath)
          val fileOuputStream = FileOutputStream(bitmapFile)
          bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOuputStream)
          fileOuputStream.close()
          imageFile = bitmapFile

          Log.d("Img","$file.png is saved to $imagePath")
      }
  }

    override fun onDestroy() {
        super.onDestroy()

        deleteRecursive(File(getAppSpecificAlbumStorageDir(activity as Context,"Painelb/img")?.absolutePath))
        deleteRecursive(File(getAppSpecificAlbumStorageDir(activity as Context,"Painelb/pdf")?.absolutePath))
    }

    fun deleteRecursive(fileOrDirectory: File) {
        if (fileOrDirectory.isDirectory) for (child in fileOrDirectory.listFiles()) deleteRecursive(
            child
        )
        fileOrDirectory.delete()
    }

    private fun combineImageIntoOne(bitmap: ArrayList<Bitmap>): Bitmap? {
        var w = 0
        var h = 0
        var i = 0

        while (i < bitmap.size){
            if (i==0  && bitmap.size == 1){
                w = bitmap[i].width
            }
            else if (i < bitmap.size - 1) {
                w = if (bitmap[i].width > bitmap[i + 1].width) bitmap[i].width else bitmap[i + 1].width
            }
            h += bitmap[i].height
            i++
        }

        val temp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(temp)
        var top = 0
        for (i in 0 until bitmap.size) {
            Log.d("HTML", "Combine: " + i + "/" + bitmap.size + 1)
            top = if (i == 0) 0 else top + bitmap[i].height
            canvas.drawBitmap(bitmap[i], 0f, top.toFloat(), null)
        }
        return temp
    }


    private fun showDeleteConfirmationDialog(item : CreateOccurrence) {
        val builder = AlertDialog.Builder(requireContext()).apply {
            setMessage(R.string.delete_occurence)
            setPositiveButton(R.string.yes) { _, _ -> viewModel.deleteConfirmed(item) }
            setNegativeButton(R.string.no) { _, _ -> }
        }
        builder.create().show()
    }

}


