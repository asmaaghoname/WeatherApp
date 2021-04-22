package com.example.kotlinapplication.view.Favorite

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinapplication.HomeActivity
import com.example.kotlinapplication.R
import com.example.kotlinapplication.model.roomDataSource.FavoriteEntity
import com.example.kotlinapplication.viewModel.FavoriteViewModel

class FavoriteAdapter (var favoriteData: ArrayList<FavoriteEntity>): RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    lateinit var context: Context
    lateinit var viewModel: FavoriteViewModel

    fun updateList(newList: List<FavoriteEntity>, viewModel: FavoriteViewModel) {
        this.viewModel = viewModel

        favoriteData.clear()
        favoriteData.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        context = parent.context
        return FavoriteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.favorite_single_row, parent, false))
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favoriteData[position], this)
    }

    override fun getItemCount(): Int {
        return favoriteData.size
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val favoriteTitleLbl = view.findViewById<TextView>(R.id.favoriteTitleLbl)
        private val deleteButton = view.findViewById<Button>(R.id.FavDeleteButton)


        @SuppressLint("SetTextI18n")
        fun bind(favorite: FavoriteEntity, adapter: FavoriteAdapter) {
            favoriteTitleLbl.text = favorite.title

            deleteButton.setOnClickListener {
                val builder = AlertDialog.Builder(adapter.context)
                builder.setMessage("Are you sure you want to delete this location?")

                builder.setPositiveButton("Yes") { _, _ ->
                    adapter.viewModel.delete(favorite)
                    adapter.favoriteData.remove(favorite)
                    adapter.notifyDataSetChanged()
                }

                builder.setNegativeButton("No", null)
                builder.show()
            }

            itemView.setOnClickListener {
                val intent = Intent(adapter.context, HomeActivity::class.java)
                intent.putExtra("source", "favorite")
                intent.putExtra("lat", favorite.lat)
                intent.putExtra("lon", favorite.lon)
                adapter.context.startActivity(intent)
            }
        }
    }
}