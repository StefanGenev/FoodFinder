package com.example.foodfinder11.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodfinder11.adapters.UsersAdapter
import com.example.foodfinder11.databinding.FragmentAdminUsersBinding
import com.example.foodfinder11.dto.ResponseWrapper
import com.example.foodfinder11.model.User
import com.example.foodfinder11.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class AdminUsersFragment : Fragment() {

    private lateinit var binding: FragmentAdminUsersBinding

    private var users: ArrayList<User> = ArrayList()

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
    }

    private fun filter(text: String) {

        val filteredlist = ArrayList<User>()

        for (user in users) {

            val nameMatches = user.name.lowercase(Locale.getDefault()).contains(text.lowercase(Locale.getDefault()))
            val emailMatches = user.email.lowercase(Locale.getDefault()).contains(text.lowercase(Locale.getDefault()))

            if (nameMatches || emailMatches) {

                filteredlist.add(user)
            }
        }
        if (filteredlist.isEmpty()) {
            //TODO: Empty state
        } else {

            resetAdapters()
            usersAdapter.differ.submitList( filteredlist )
        }
    }

    private fun loadUsersRequest() {

        RetrofitInstance.getApiService().getAllUsers()
            .enqueue(object : Callback<ResponseWrapper<List<User>>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<List<User>>>,
                    response: Response<ResponseWrapper<List<User>>>
                ) {

                    val responseBody = response.body().takeIf { it != null } ?: return

                    if (responseBody.status == 200) {

                        val responseData = responseBody.data.takeIf { it != null } ?: return
                        users.addAll(responseData)

                        initUsers()

                    } else {
                        Toast.makeText(activity, responseBody.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(
                    call: Call<ResponseWrapper<List<User>>>,
                    t: Throwable
                ) {
                    Toast.makeText(activity, "Problem with request", Toast.LENGTH_SHORT).show()
                }
            })

    }

    private fun initUsers() {

        usersAdapter = UsersAdapter()

        usersAdapter.onItemClicked(object : UsersAdapter.OnItemClicked {

            override fun onClickListener(user: User) {

            }

        })

        resetAdapters()
        usersAdapter.differ.submitList( users )
    }

    private fun resetAdapters() {

        binding.rvUsers.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = usersAdapter
        }
    }

}