package com.example.oscar.hillfort.views.siteList

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.oscar.hillfort.R
import com.example.oscar.hillfort.models.SiteListener
import com.example.oscar.hillfort.models.SiteModel
import com.example.oscar.hillfort.views.BaseView
import kotlinx.android.synthetic.main.activity_site_list.*

class SiteListView : BaseView(), SiteListener {

    lateinit var presenter: SiteListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_site_list)
        init(toolbarList, false)

        presenter = initPresenter(SiteListPresenter(this)) as SiteListPresenter
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        presenter.showSites()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun showSites(sites: List<SiteModel>) {
        recyclerView.adapter = SiteAdapter(sites, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_add -> presenter.doAddSite()
            R.id.item_map -> presenter.doShowSitesMap()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSiteClick(site: SiteModel) {
        presenter.doEditSite(site)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        recyclerView.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }
}