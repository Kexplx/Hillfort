package com.example.oscar.hillfort.views.siteList

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.oscar.hillfort.R
import com.example.oscar.hillfort.helpers.readImageFromPath
import com.example.oscar.hillfort.models.SiteListener
import com.example.oscar.hillfort.models.SiteModel
import kotlinx.android.synthetic.main.card_site.view.*

class SiteAdapter constructor(private var sites: List<SiteModel>,
                              private val listener: SiteListener) : RecyclerView.Adapter<SiteAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.card_site,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val site = sites[holder.adapterPosition]
        holder.bind(site, listener)
    }

    override fun getItemCount(): Int = sites.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(site: SiteModel, listener : SiteListener) {
            itemView.txtSiteName.text = site.title
            itemView.txtLat.text = site.lat.toString()
            itemView.txtLng.text = site.lng.toString()
            itemView.chkHasBeenVisited.isChecked = site.hasBeenVisited
            itemView.imgSite.setImageBitmap(readImageFromPath(itemView.context, site.images[0]))
            itemView.setOnClickListener { listener.onSiteClick(site) }
        }
    }
}