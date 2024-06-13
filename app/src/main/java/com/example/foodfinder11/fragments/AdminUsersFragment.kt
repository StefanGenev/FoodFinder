package com.example.foodfinder11.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodfinder11.R
import com.example.foodfinder11.adapters.UsersAdapter
import com.example.foodfinder11.databinding.FragmentAdminUsersBinding
import com.example.foodfinder11.dto.IdentifierDto
import com.example.foodfinder11.dto.NoData
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

        RetrofitInstance.getApiService().getCustomers()
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
                    Toast.makeText(activity, getString(R.string.problem_with_request), Toast.LENGTH_SHORT).show()
                }
            })

    }

    private fun initUsers() {

        usersAdapter = UsersAdapter()

        usersAdapter.onItemClicked(object : UsersAdapter.OnItemClicked {

            override fun onClickListener(user: User, position: Int) {
                onTapUser(user, position)
            }

        })

        usersAdapter.differ.submitList( users )
        resetAdapters()
    }

    private fun onTapUser(user: User, position: Int) {
        QuestionDialogFragment("Are you sure you want to delete this user?",
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

                        Toast.makeText(activity, "User deleted", Toast.LENGTH_SHORT).show()

                        resetAdapters()
                        users.remove(user)
                        usersAdapter.differ.submitList( users )
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

    private fun resetAdapters() {

        binding.rvUsers.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = usersAdapter
        }
    }

}