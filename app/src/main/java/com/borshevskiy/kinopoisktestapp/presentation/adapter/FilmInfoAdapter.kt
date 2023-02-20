package com.borshevskiy.kinopoisktestapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.borshevskiy.kinopoisktestapp.R
import com.borshevskiy.kinopoisktestapp.databinding.FilmRowLayoutBinding
import com.borshevskiy.kinopoisktestapp.domain.FilmInfo
import com.borshevskiy.kinopoisktestapp.presentation.FilmListFragmentDirections

/** Использую листадаптер с дифутилколбеком, для того чтоб лишний раз не обновлять список фильмов,
 * если он одинаковый **/

class FilmInfoAdapter: ListAdapter<FilmInfo, FilmInfoAdapter.FilmInfoViewHolder>(FilmInfoDiffCallback) {

    class FilmInfoViewHolder(val binding: FilmRowLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmInfoViewHolder {
        val binding =
            FilmRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilmInfoViewHolder, position: Int) {
        val film = getItem(position)
        with(holder.binding) {
            with(film) {
                filmName.text = film.name
                filmDesc.text = film.description
                filmRate.text = film.rating.toString()
                if (film.imageUrl.isNotEmpty()) filmImage.load(film.imageUrl) else filmImage.setImageResource(
                    R.drawable.ic_error_placeholder)
                root.setOnClickListener {
                    root.findNavController().navigate(FilmListFragmentDirections.actionFilmListFragmentToFilmInfoFragment(film))
                }
            }
        }
    }
}