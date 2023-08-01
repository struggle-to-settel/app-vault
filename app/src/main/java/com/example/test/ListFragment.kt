package com.example.test

import android.os.Bundle
import android.view.View
import com.example.speedmatch.R
import com.example.speedmatch.databinding.FragmentListBinding
import com.example.test.Common.dismissProgressDialog
import com.example.test.Common.getUsers
import com.example.test.Common.mediaJSON
import com.example.test.Common.showProgressDialog
import com.example.test.adapter.FileListAdapter
import com.google.gson.Gson

class ListFragment : BasePermissionFragment<FragmentListBinding>() {

    private lateinit var adapter: FileListAdapter
    private lateinit var dataSource: Videos
    override fun getLayout(): FragmentListBinding {
        return FragmentListBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataSource = Gson().fromJson(mediaJSON, Videos::class.java)
        adapter = FileListAdapter {
            adapter.getItem(it)?.let {
                if (it.userId.startsWith("http")) Common.downloadImage(
                    it.profile ?: "", requireContext()
                )
                else if (it.profile?.startsWith("http") == true) Common.downloadImage(
                    it.profile,
                    requireContext()
                )
            }

        }
        binding.rvList.adapter = adapter
        checkCameraGalleryPermission()

        binding.btnAll.setOnClickListener {
            checkCameraGalleryPermission()
        }

        binding.tvHiddenFiles.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.flMain, HiddenFileFragment()).addToBackStack("").commit()
        }

        binding.tvSavedFiles.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.flMain, SavedFileFragment()).addToBackStack("").commit()
        }

    }


    override fun mediaPermissionGranted() {
        showProgressDialog(requireContext())
        getUsers {
            requireActivity().runOnUiThread {
                dismissProgressDialog()
                adapter.set(it)
            }
        }
//        adapter.set(dataSource.categories[0].videos)
    }


}