package com.example.oscar.hillfort.views.site

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.oscar.hillfort.R
import com.example.oscar.hillfort.helpers.readImageFromPath
import com.example.oscar.hillfort.models.SiteModel
import kotlinx.android.synthetic.main.activity_site.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast

class SiteView: AppCompatActivity(), AnkoLogger {

    lateinit var presenter: SitePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_site)
        toolbarSite.setTitle(R.string.hillfort_title)
        setSupportActionBar(toolbarSite)

        presenter = SitePresenter(this)

        btnAdd.setOnClickListener {
            if (txtSiteName.text.toString().isEmpty()) {
                toast(getString(R.string.toast_TitelEinfügen))
            } else {
                presenter.doAddOrSave(txtSiteName.text.toString(), txtSiteDescription.text.toString(),txtAdditionNotes.text.toString(),txtDateVisited.text.toString(), chkVisited.isChecked)
            }
        }

        imgBtn0.setOnClickListener {presenter.doSelectImage(presenter.IMAGE_0_REQUEST)}
        imgBtn1.setOnClickListener {presenter.doSelectImage(presenter.IMAGE_1_REQUEST)}
        imgBtn2.setOnClickListener {presenter.doSelectImage(presenter.IMAGE_2_REQUEST)}
        imgBtn3.setOnClickListener {presenter.doSelectImage(presenter.IMAGE_3_REQUEST)}

        btnAddLocation.setOnClickListener { presenter.doSetLocation() }
    }

    fun showSite(site: SiteModel) {
        txtSiteName.setText(site.title)
        txtSiteDescription.setText(site.description)
        txtAdditionNotes.setText(site.notes)
        txtDateVisited.setText(site.dateVisited)
        chkVisited.isChecked = site.hasBeenVisited

        imgBtn0.setImageBitmap(readImageFromPath(this, site.images[0]))
        imgBtn1.setImageBitmap(readImageFromPath(this, site.images[1]))
        imgBtn2.setImageBitmap(readImageFromPath(this, site.images[2]))
        imgBtn3.setImageBitmap(readImageFromPath(this, site.images[3]))

        btnAdd.setText(R.string.save_images)
    }

    fun updateImages(imageNumber : Int, imagePath : String){
        when(imageNumber){
            0 -> imgBtn0.setImageBitmap(readImageFromPath(this, imagePath))
            1 -> imgBtn1.setImageBitmap(readImageFromPath(this, imagePath))
            2 -> imgBtn2.setImageBitmap(readImageFromPath(this, imagePath))
            3 -> imgBtn3.setImageBitmap(readImageFromPath(this, imagePath))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_site, menu)
        if (presenter.edit) menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_delete -> {
                presenter.doDelete()
            }
            R.id.item_cancel -> {
                presenter.doCancel()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            presenter.doActivityResult(requestCode, resultCode, data)
        }
    }
}