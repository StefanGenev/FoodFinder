package com.example.foodfinder11.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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