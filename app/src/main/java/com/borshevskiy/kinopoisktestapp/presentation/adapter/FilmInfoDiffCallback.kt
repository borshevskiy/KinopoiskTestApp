package com.borshevskiy.kinopoisktestapp.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.borshevskiy.kinopoisktestapp.domain.FilmInfo

object FilmInfoDiffCallback: DiffUtil.ItemCallback<FilmInfo>() {

    override fun areItemsTheSame(oldItem: FilmInfo, newItem: FilmInfo) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: FilmInfo, newItem: FilmInfo) = oldItem == newItem
}