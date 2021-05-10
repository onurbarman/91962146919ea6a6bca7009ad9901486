package com.app.spaceitemdeliveryapp.ui.main.space_travel.ui.planets

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.spaceitemdeliveryapp.R
import com.app.spaceitemdeliveryapp.data.room.planets.Planets
import com.app.spaceitemdeliveryapp.data.room.spacecraft.Spacecrafts
import com.app.spaceitemdeliveryapp.utils.TravelInterface
import com.app.spaceitemdeliveryapp.utils.Utils
import java.util.*
import kotlin.collections.ArrayList

class PlanetsAdapter(
    private var currentSpacecraft: Spacecrafts?,
    private var currentPlanet: Planets?,
    private var allPlanets: MutableList<Planets>,
    private var planets: List<Planets>,
    private val travelInterface: TravelInterface
) : RecyclerView.Adapter<PlanetsAdapter.PlanetsViewHolder>(), Filterable {

    private var listFilter: OrderFilterAdapter? = null
    private var originalData: List<Planets>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanetsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_planet, parent, false)
        return PlanetsViewHolder(view)
    }

    override fun getItemCount(): Int = planets.size

    override fun onBindViewHolder(holder: PlanetsViewHolder, position: Int) {
        holder.bind(planets[position])
    }

    fun updatePlanets(
        currentSpacecraft: Spacecrafts?,
        currentPlanet: Planets?,
        planets: List<Planets>
    ) {
        this.currentSpacecraft = currentSpacecraft
        this.currentPlanet = currentPlanet
        this.allPlanets.addAll(planets)
        this.planets = planets
        if (originalData == null) {
            originalData = planets
        }
        notifyDataSetChanged()
    }

    fun clearFilter() {
        this.planets = allPlanets
        if (originalData == null) {
            originalData = planets
        }
        notifyDataSetChanged()
    }

    fun setPlanets(planets: List<Planets>) {
        this.planets = planets
        if (originalData == null) {
            originalData = planets
        }
        notifyDataSetChanged()
    }

    fun clearAdapter() {
        this.allPlanets = mutableListOf()
        this.planets = listOf()
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        if (listFilter == null)
            if (originalData != null)
                listFilter = OrderFilterAdapter(this, originalData!!)

        return listFilter!!
    }

    inner class PlanetsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val btnFavorite: ImageView = itemView.findViewById(R.id.btnFavorite)
        private val textCapacityStock: TextView = itemView.findViewById(R.id.textCapacityStock)
        private val textDistance: TextView = itemView.findViewById(R.id.textDistance)
        private val textPlanetName: TextView = itemView.findViewById(R.id.textPlanetName)
        private val btnTravel: AppCompatButton = itemView.findViewById(R.id.btnTravel)

        @SuppressLint("SetTextI18n")
        fun bind(planet: Planets) {
            if (planet.isComplete == 1) {
                Log.e("planet",planet.id.toString()+"...iscomplete")
                btnTravel.isEnabled = false
                btnTravel.setBackgroundColor(ContextCompat.getColor(btnTravel.context,R.color.hintColor))
            }
            else{
                btnTravel.isEnabled = true
                btnTravel.setBackgroundColor(ContextCompat.getColor(btnTravel.context,R.color.colorPrimary))
            }

            if (planet.isFavorite == 1)
                btnFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        btnFavorite.context,
                        R.drawable.ic_star
                    )
                )

            currentPlanet?.run {
                currentSpacecraft?.let {
                    if(it.currentLocationId==planet.id) {
                        Log.e("planet",planet.id.toString()+"...bulundugum yer")
                        btnTravel.isEnabled = false
                        btnTravel.setBackgroundColor(ContextCompat.getColor(btnTravel.context,R.color.hintColor))
                    }
                    //Calculate Speed
                    val distance = Utils.calculateDistance(coordinateX, planet.coordinateX, coordinateY, planet.coordinateY)
                    textDistance.text = distance.toString() + "EUS"
                    if (it.universalSpaceTime < distance) {
                        Log.e("planet",planet.id.toString()+"...süre")
                        btnTravel.isEnabled = false
                        btnTravel.setBackgroundColor(ContextCompat.getColor(btnTravel.context,R.color.hintColor))
                        return@run
                    }



                    btnTravel.setOnClickListener {
                        travelInterface.travelClick(distance,planet)
                    }

                    btnFavorite.setOnClickListener {
                        if (planet.isFavorite == 1) {
                            btnFavorite.setImageDrawable(ContextCompat.getDrawable(btnFavorite.context, R.drawable.ic_star_empty))
                            planet.isFavorite = 0
                        } else {
                            btnFavorite.setImageDrawable(ContextCompat.getDrawable(btnFavorite.context, R.drawable.ic_star))
                            planet.isFavorite = 1
                        }
                        travelInterface.favoriteClick(planet)
                    }

                    textPlanetName.text = planet.name
                    textCapacityStock.text =
                        planet.capacity.toString() + "/" + planet.stock.toString()
                }
            }

        }
    }
}

private class OrderFilterAdapter(
    private val listAdapter: PlanetsAdapter,
    private val originalData: List<Planets>
) : Filter() {

    private val filteredData: ArrayList<Planets> = ArrayList()

    override fun performFiltering(constraint: CharSequence): FilterResults {
        filteredData.clear()

        val results = FilterResults()
        Log.d("performFiltering: ", constraint.toString())
        if (TextUtils.isEmpty(constraint.toString())) {
            filteredData.addAll(originalData)
        } else {
            val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim { it <= ' ' }
            for (tow in originalData) {
                // set condition for filter here
                if (tow.name.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                    filteredData.add(tow)
                }
            }
        }
        results.values = filteredData
        results.count = filteredData.size
        return results
    }

    override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
        listAdapter.setPlanets((filterResults.values as List<Planets>))
        listAdapter.notifyDataSetChanged()
    }
}