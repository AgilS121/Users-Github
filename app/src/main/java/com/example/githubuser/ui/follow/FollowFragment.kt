package com.example.githubuser.ui.follow

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.databinding.FragmentFollowBinding
import com.example.githubuser.ui.MainViewModel
import com.example.githubuser.ui.ReviewAdapter
import com.example.githubuser.ui.details.DetailUserActivity

class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    private lateinit var adapter: ReviewAdapter
    private lateinit var viewModel: MainViewModel
    private lateinit var username: String
    private var position: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_SECTION_NUMBER)
            username = it.getString(ARG_USERNAME) ?: ""
        }

        val recyclerView = binding.recycleviewFragment
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val loadingIndicator = binding.progressBar

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        adapter = ReviewAdapter(object : ReviewAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                openDetail(data)
            }
        })
        if (position == 1) {
            viewModel.listFollowing.observe(viewLifecycleOwner){ following ->
                adapter.submitList(following)
                recyclerView.adapter = adapter
                loadingIndicator.visibility = View.GONE
            }
            viewModel.getFollowings(username)
        } else {
            viewModel.listFollower.observe(viewLifecycleOwner){ follower ->
                adapter.submitList(follower)
                recyclerView.adapter = adapter
                loadingIndicator.visibility = View.GONE
            }
            viewModel.getFollowers(username)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun openDetail(data: ItemsItem) {
        val moveIntent = Intent(requireContext(), DetailUserActivity::class.java)
        moveIntent.putExtra("username", data.login)
        startActivity(moveIntent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_USERNAME = "name"
    }
}
