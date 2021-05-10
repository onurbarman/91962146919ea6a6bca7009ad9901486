package com.app.spaceitemdeliveryapp.ui.main.space_travel.ui.favorites

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.spaceitemdeliveryapp.R
import com.app.spaceitemdeliveryapp.data.room.planets.Planets
import com.app.spaceitemdeliveryapp.utils.Utils

class FavoritesAdapter(private var favorites: List<Planets>,
                       private val onFavoriteClick: (isFavorite: Int, planet: Planets) -> Unit)
    : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val view = LayoutInflater .from(parent.context)
            .inflate(R.layout.item_favorite_planets, parent, false)
        return FavoritesViewHolder(view)
    }
    override fun getItemCount(): Int {
        return favorites.size
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bind(favorites[position])
    }

    fun updateFavorites(favorites: List<Planets>) {
        this.favorites = favorites
        notifyDataSetChanged()
    }

    fun clearAdapter() {
        this.favorites = listOf()
        notifyDataSetChanged()
    }

    inner class FavoritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val btnFavorite : ImageView = itemView.findViewById(R.id.btnFavorite)
        private val textPlanetName : TextView = itemView.findViewById(R.id.textPlanetName)
        private val textPlanetDistance : TextView = itemView.findViewById(R.id.textPlanetDistance)

        @SuppressLint("SetTextI18n")
        fun bind(favorite: Planets) {
            btnFavorite.setOnClickListener {
                if (favorite.isFavorite==1) {
                    favorite.isFavorite=0
                    onFavoriteClick.invoke(0, favorite)
                    btnFavorite.setImageDrawable(ContextCompat.getDrawable(btnFavorite.context,R.drawable.ic_star_empty))
                }
                else {
                    favorite.isFavorite=1
                    onFavoriteClick.invoke(1, favorite)
                    btnFavorite.setImageDrawable(ContextCompat.getDrawable(btnFavorite.context, R.drawable.ic_star))
                }
            }

            textPlanetName.text=favorite.name
            textPlanetDistance.text= Utils.calculateDistance(0.0,favorite.coordinateX,0.0,favorite.coordinateY).toString()+"EUS"
        }
    }
}