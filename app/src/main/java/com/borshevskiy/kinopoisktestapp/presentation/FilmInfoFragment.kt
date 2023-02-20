package com.borshevskiy.kinopoisktestapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import com.borshevskiy.kinopoisktestapp.databinding.FragmentFilmInfoBinding
import dagger.hilt.android.AndroidEntryPoint

/** В данном фрагменте отображаю детальную информацию о фильме, получая аргумент с полями данных **/

@AndroidEntryPoint
class FilmInfoFragment : Fragment() {

    private var _binding: FragmentFilmInfoBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<FilmInfoFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFilmInfoBinding.inflate(layoutInflater, container, false)
        with(binding) {
            filmImage.load(args.film.imageUrl)
            filmTitle.text = args.film.name
            filmRateText.text = args.film.rating.toString()
            filmDesc.text = args.film.description
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}