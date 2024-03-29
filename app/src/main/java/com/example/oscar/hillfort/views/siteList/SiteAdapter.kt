package com.example.oscar.hillfort.views.siteList

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.oscar.hillfort.R
import com.example.oscar.hillfort.models.SiteListener
import com.example.oscar.hillfort.models.SiteModel
import kotlinx.android.synthetic.main.card_site.view.*

class SiteAdapter constructor(
    private var sites: List<SiteModel>,
    private val listener: SiteListener
) : androidx.recyclerview.widget.RecyclerView.Adapter<SiteAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent.context).inflate(
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

    class MainHolder constructor(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        fun bind(site: SiteModel, listener: SiteListener) {

            itemView.btnShare.setOnClickListener {
                val lineSeperator = System.getProperty("line.separator")
                val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                val shareBody = site.title + " (" + site.lat + "/" + site.lng + ")" +
                        lineSeperator + site.description + lineSeperator + site.notes
                val shareSub = "Hillfort App: Site Description"
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub)
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
                Intent.createChooser(sharingIntent, "Site Desription")
                ContextCompat.startActivity(itemView.context, sharingIntent, null)
            }

            itemView.txtSiteName.text = site.title
            itemView.txtLat.text = site.lat.toString().take(6)
            itemView.txtLng.text = site.lng.toString().take(6)
            itemView.dateVisited.setText(site.dateVisited)
            itemView.chkHasBeenVisited.isChecked = site.hasBeenVisited
            Glide.with(itemView.context).load(site.images[0]).into(itemView.imgSite)
            itemView.setOnClickListener { listener.onSiteClick(site) }

            if (site.hasBeenVisited) {
                itemView.dateVisited.visibility = View.VISIBLE
            } else {
                itemView.dateVisited.visibility = View.GONE
            }

            if (site.favorite) {
                itemView.favorite.visibility = View.VISIBLE
            } else {
                itemView.favorite.visibility = View.GONE
            }
        }
    }
}