package com.example.foodfinder11.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodfinder11.R
import com.example.foodfinder11.adapters.UsersAdapter
import com.example.foodfinder11.dataObjects.AdminRestaurantsFilter
import com.example.foodfinder11.databinding.FragmentAdminUsersBinding
import com.example.foodfinder11.dto.IdentifierDto
import com.example.foodfinder11.dto.NoData
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.User
import com.example.foodfinder11.retrofit.RetrofitInstance
import com.example.foodfinder11.viewModel.AdminViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale


class AdminUsersFragment : Fragment() {

    private lateinit var binding: FragmentAdminUsersBinding
    private val mainViewModel: AdminViewModel by activityViewModels()

    private var users: List<User> = listOf()

    private lateinit var usersAdapter: UsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAdminUsersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadUsersRequest()

        // below line is to call set on query text listener method.
        binding.idSearchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(newText)
                return false
            }
        })

        val filter = mainViewModel.getUsersFilter()
        binding.idSearchView.setQuery(filter, true)
    }

    override fun onResume() {
        super.onResume()

        loadUsersRequest()
    }

    private fun filter(text: String) {

        binding.emptyStateLayout.visibility = View.GONE

        val filteredlist = ArrayList<User>()

        for (user in users) {

            val nameMatches = user.name.lowercase(Locale.getDefault()).contains(text.lowercase(Locale.getDefault()))
            val emailMatches = user.email.lowercase(Locale.getDefault()).contains(text.lowercase(Locale.getDefault()))

            if (nameMatches || emailMatches) {

                filteredlist.add(user)
            }
        }

        if (filteredlist.isEmpty()) {
            showEmptyState()
        }

        mainViewModel.setUsersFilter(text)

        resetAdapters()
        usersAdapter.differ.submitList(filteredlist)
    }

    private fun loadUsersRequest(reload: Boolean = false) {

        mainViewModel.loadAllCustomers(reload)

        mainViewModel.getAllCustomersLiveData()
            .observe(viewLifecycleOwner, Observer { customers ->

                if (customers.isEmpty()) {

                    showEmptyState()

                } else {

                    this.users = customers
                    initUsers()
                }

            })
    }

    private fun showEmptyState() {

        binding.emptyStateLayout.visibility = View.VISIBLE
    }

    private fun initUsers() {

        var filter = mainViewModel.getUsersFilter()
        filter(filter)
    }

    private fun onTapUser(user: User, position: Int) {
        QuestionDialogFragment(
            getString(R.string.are_you_sure_you_want_to_delete_this_user),
            getString(R.string.yes),
            getString(R.string.no),
            onOkAction = { dialog, id ->

                deleteUserRequest(user, position)
            }
            , onCancelAction = { dialog, id ->
            } ).show(parentFragmentManager, "QuestionDialog")
    }

    private fun deleteUserRequest(user: User, position: Int) {

        val dto = IdentifierDto(id = user.id)

        RetrofitInstance.getApiService().deleteUser(dto)

            .enqueue(object : Callback<ResponseWrapper<NoData>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<NoData>>,
                    response: Response<ResponseWrapper<NoData>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {

                        Toast.makeText(activity,
                            getString(R.string.user_deleted), Toast.LENGTH_SHORT).show()

                        reloadUsers()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<NoData>>,
                    t: Throwable
                ) {
                    Toast.makeText(activity, getString(R.string.problem_with_request), Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun reloadUsers() {
        resetAdapters()
        loadUsersRequest(true)
    }

    private fun resetAdapters() {

        usersAdapter = UsersAdapter()

        usersAdapter.onItemClicked(object : UsersAdapter.OnItemClicked {

            override fun onClickListener(user: User, position: Int) {
                onTapUser(user, position)
            }

        })

        binding.rvUsers.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = usersAdapter
        }
    }

}