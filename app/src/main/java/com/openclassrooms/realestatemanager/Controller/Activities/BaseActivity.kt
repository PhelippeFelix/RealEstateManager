package com.openclassrooms.realestatemanager.Controller.Activities

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import com.google.android.material.textfield.TextInputEditText
import android.view.Window
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.openclassrooms.realestatemanager.Controller.Views.ActivityAddAdapter
import com.openclassrooms.realestatemanager.Models.Image
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.utils.Constants
import com.openclassrooms.realestatemanager.utils.Utils
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.custom_dialog_overlay.*
import kotlinx.android.synthetic.main.estate_info.*
import kotlinx.android.synthetic.main.toolbar.*
import java.text.SimpleDateFormat
import java.util.*
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.time.LocalDate


abstract class BaseActivity : AppCompatActivity() {

    lateinit var photoURI:Uri
    lateinit var photoFilePath:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        this.handleResponse(requestCode,resultCode,data)

    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    fun configureToolbar(){
        setSupportActionBar(simple_toolbar)
    }

    fun setOnClickListener(){
        add_activity_date.setOnClickListener{this.displayDatePicker(add_activity_date)}
        add_activity_choose_pic.setOnClickListener{this.onClickAddFile()}
        add_activity_take_pic.setOnClickListener { this.onClickTakePic() }
        add_activity_spinner.setOnClickListener{this.displayPopupMenu()}
        add_activity_save.setOnClickListener{saveEstateToDatabase()}
    }

    // ---------------------
    // ACTION
    // ---------------------

    private fun onClickAddFile() = runWithPermissions(Constants.PERMS){
        this.chooseImageFromPhone()
    }

    private fun onClickTakePic() = runWithPermissions(Constants.PERM_CAMERA,Constants.PERM_WRITE_EXT){
        this.takePhotoFromCamera()
    }

    fun showOverlay(context: Context, image: Image, position: Int, adapter:ActivityAddAdapter){
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog_overlay)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(true)

        val displayMetrics = context.resources.displayMetrics
        val dialogWidth = (displayMetrics.widthPixels * 0.85).toInt()
        val dialogHeight = (displayMetrics.heightPixels * 0.85).toInt()
        dialog.window?.setLayout(dialogWidth,dialogHeight)

        dialog.show()

        this.populateDialogWhenOpening(dialog,image)

        dialog.overlay_cancel.setOnClickListener{hideOverlayLayout(dialog)}
        dialog.overlay_save.setOnClickListener{saveImageDetails(dialog, image, position, adapter)}
    }

    private fun saveImageDetails(dialog: Dialog,image: Image, position: Int, adapter: ActivityAddAdapter){
        val error: Boolean
        var title:String? = null
        var desc:String? = null

        when{
            dialog.overlay_title.text.isNullOrBlank() || dialog.overlay_desc.text.isNullOrBlank() -> {
                error = true
                if (dialog.overlay_title.text.isNullOrBlank()) {
                    dialog.overlay_title_layout.error = resources.getString(R.string.overlay_image_title_error)
                    title = null
                }
                if(dialog.overlay_desc.text.isNullOrBlank()) {
                    dialog.overlay_desc_layout.error = resources.getString(R.string.overlay_image_desc_error)
                    desc = null
                }
            }
            else -> {
                error = false
                title = dialog.overlay_title.text.toString()
                dialog.overlay_title_layout.error = null
                desc = dialog.overlay_desc.text.toString()
                dialog.overlay_desc_layout.error = null
            }
        }

        if(!error){
            image.imageTitle = title
            image.imageDesc = desc
            adapter.notifyItemChanged(position)
            dialog.dismiss()
            Toast.makeText(this,resources.getString(R.string.overlay_image_details_saved), Toast.LENGTH_SHORT).show()
        }

    }

    private fun populateDialogWhenOpening(dialog: Dialog, image: Image){
        Glide.with(this).load(Uri.parse(image.imagePath)).apply(RequestOptions().centerCrop()).into(dialog.overlay_image_view)
        if(!(image.imageTitle.isNullOrBlank())) dialog.overlay_title.setText(image.imageTitle)
        if(!(image.imageDesc.isNullOrBlank())) dialog.overlay_desc.setText(image.imageDesc)
    }

    private fun hideOverlayLayout(dialog: Dialog){
        dialog.dismiss()
    }

    // ---------------------
    // ABSTRACT METHOD
    // ---------------------

    abstract fun saveEstateToDatabase()

    abstract fun handleResponse(requestCode: Int, resultCode: Int, data: Intent?)

    // ---------------------
    // FILE MANAGEMENT
    // ---------------------

    private fun chooseImageFromPhone() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.RC_CHOOSE_PHOTO)
    }

    private fun takePhotoFromCamera(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile:File? = try {
                    createImageFile()
                }catch (ex:IOException){
                    null
                }
                photoFile?.also {
                    photoURI = FileProvider.getUriForFile(this,"com.example.android.fileprovider",it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI )
                    startActivityForResult(takePictureIntent, Constants.RC_TAKE_PHOTO)
                }

            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
                "JPEG_${timeStamp}_",
                ".jpg",
                storageDir
        ).apply {
            photoFilePath = absolutePath
        }
    }

    // ---------------------
    // UI
    // ---------------------

    fun displayDatePicker(mView: TextInputEditText){
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val calendar: Calendar = Calendar.getInstance()
        val mYear = calendar.get(Calendar.YEAR)
        val mMonth = calendar.get(Calendar.MONTH)
        val mDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{ _, year, monthOfYear, dayOfMonth ->
            mView.setText(sdf.format(LocalDate.of(year - 1900,monthOfYear,dayOfMonth)), TextView.BufferType.EDITABLE)

        }, mYear,mMonth,mDay)
        datePickerDialog.show()
    }

    private fun displayPopupMenu(){
        val popupMenu = PopupMenu(this,add_activity_spinner)
        popupMenu.menuInflater.inflate(R.menu.popup_menu_estate_type,popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item -> add_activity_spinner.setText(item.title); true}
        popupMenu.show()
    }

    fun populateWithTodayDate(mView: TextInputEditText){mView.setText(Utils.getTodayDate(), TextView.BufferType.EDITABLE)}
}
