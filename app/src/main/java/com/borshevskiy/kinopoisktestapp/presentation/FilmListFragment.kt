package com.borshevskiy.kinopoisktestapp.presentation

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.borshevskiy.kinopoisktestapp.R
import com.borshevskiy.kinopoisktestapp.databinding.FragmentFilmListBinding
import com.borshevskiy.kinopoisktestapp.domain.util.NetworkResult
import com.borshevskiy.kinopoisktestapp.presentation.adapter.FilmInfoAdapter
import com.borshevskiy.kinopoisktestapp.presentation.utils.ConnectionListener
import dagger.hilt.android.AndroidEntryPoint

/** В данном фрагменте отображаю список фильмов, в зависимости от запроса (весь список,
 * поисковый запрос или же отображение данных из бд, в случае отсутствия интернета если она не пуста) **/

@AndroidEntryPoint
class FilmListFragment : Fragment(), MenuProvider {

    private var _binding: FragmentFilmListBinding? = null
    private val binding get() = _binding!!

    private lateinit var sortOrder: String
    private val mainViewModel: MainViewModel by viewModels()
    private val mAdapter by lazy { FilmInfoAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFilmListBinding.inflate(layoutInflater, container, false)
        (requireActivity() as MenuHost).addMenuProvider(this, viewLifecycleOwner,
            Lifecycle.State.RESUMED)
        mainViewModel.orderQuery.observe(viewLifecycleOwner) { sortOrder = it }
        checkNetworkConnection()
        setupRecyclerView()
        return binding.root
    }

    /** В зависимости от соединения вызываю либо запрос к АПИ либо запрос КЭШа
     * по клику на FAB сортировки по году меняю значение (убывание/возрастание) **/

    private fun checkNetworkConnection() {
        ConnectionListener(requireActivity().application).observe(viewLifecycleOwner) { internetAvailable ->
            if (internetAvailable){
                with(binding) {
                    sortFab.setOnClickListener {
                        changeOrder()
                        requestApiData(sortOrder)
                    }
                    errorImageView.visibility = View.INVISIBLE
                    errorTextView.visibility = View.INVISIBLE
                }
                requestApiData(sortOrder)
            } else {
                with(binding) {
                    sortFab.setOnClickListener {
                        changeOrder()
                        loadDataFromCache(sortOrder)
                    }
                    errorImageView.visibility = View.VISIBLE
                    errorTextView.visibility = View.VISIBLE
                }
                loadDataFromCache(sortOrder)
            }
        }
    }

    /** меняю значение (убывание/возрастание) **/

    private fun changeOrder() {
        if (sortOrder == "1") mainViewModel.orderQuery.value =
            "-1" else mainViewModel.orderQuery.value = "1"
    }

    private fun setupRecyclerView() {
        binding.rvFilmList.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
        showShimmerEffect()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.film_search_menu, menu)
        (menu.findItem(R.id.menu_search_item).actionView as? SearchView)?.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (!query.isNullOrEmpty()) searchApiData(sortOrder, query)
                    return true
                }

                override fun onQueryTextChange(p0: String?) = true
            })
    }

    override fun onMenuItemSelected(menuItem: MenuItem) = true

    /** выключение шиммера **/

    private fun hideShimmerEffect() {
        with(binding) {
            shimmerLayout.stopShimmer()
            shimmerLayout.visibility = View.GONE
            rvFilmList.visibility = View.VISIBLE
        }
    }

    /** включение шиммера **/

    private fun showShimmerEffect() {
        binding.shimmerLayout.startShimmer()
    }

    /** запрос к АПИ и в зависимости от результата (отображение, тоаст ошибки, отображение загрузки **/

    private fun requestApiData(sortQuery: String) {
        mainViewModel.getFilms(sortQuery)
        mainViewModel.filmList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.submitList(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache(sortOrder)
                    Toast.makeText(context, response.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        }
    }

    /** поисковый запрос к АПИ и в зависимости от результата (отображение, тоаст ошибки, отображение загрузки **/

    private fun searchApiData(sortQuery: String, searchQuery: String) {
        showShimmerEffect()
        mainViewModel.searchFilms(sortQuery, searchQuery)
        mainViewModel.searchedFilmList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.submitList(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    Toast.makeText(context, response.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        }
    }

    /** загрузка кэша, если он есть - выключение шиммера и отображение списка **/

    private fun loadDataFromCache(sortQuery: String) {
        mainViewModel.readDatabase(sortQuery)
        mainViewModel.dbFilmList.observe(viewLifecycleOwner) { cache ->
            if (cache.isNotEmpty()) {
                hideShimmerEffect()
                binding.errorImageView.visibility = View.INVISIBLE
                binding.errorTextView.visibility = View.INVISIBLE
                mAdapter.submitList(cache)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}