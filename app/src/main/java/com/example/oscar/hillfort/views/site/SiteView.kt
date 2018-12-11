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

        btnAddImage.setOnClickListener { presenter.doSelectImage() }
        btnAddLocation.setOnClickListener { presenter.doSetLocation() }
    }

    fun showSite(site: SiteModel) {
        txtSiteName.setText(site.title)
        txtSiteDescription.setText(site.description)
        txtAdditionNotes.setText(site.notes)
        txtDateVisited.setText(site.dateVisited)
        chkVisited.isChecked = site.hasBeenVisited

        siteImage0.setImageBitmap(readImageFromPath(this, site.images[0]))
        siteImage1.setImageBitmap(readImageFromPath(this, site.images[1]))
        siteImage2.setImageBitmap(readImageFromPath(this, site.images[2]))
        siteImage3.setImageBitmap(readImageFromPath(this, site.images[3]))

        if(site.images[3] != "") btnAddImage.text = getString(R.string.btn_edit)
        btnAdd.setText(R.string.save_images)
    }

    fun updateImages(site : SiteModel){
        siteImage0.setImageBitmap(readImageFromPath(this, site.images[0]))
        siteImage1.setImageBitmap(readImageFromPath(this, site.images[1]))
        siteImage2.setImageBitmap(readImageFromPath(this, site.images[2]))
        siteImage3.setImageBitmap(readImageFromPath(this, site.images[3]))

        if(site.images[3] != "") btnAddImage.text = getString(R.string.btn_edit)
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