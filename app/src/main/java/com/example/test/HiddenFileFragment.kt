package com.example.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.example.speedmatch.databinding.FragmentHiddenFilesBinding
import com.example.test.adapter.LocalFileListAdapter
import java.io.File


class HiddenFileFragment : BasePermissionFragment<FragmentHiddenFilesBinding>() {
    private lateinit var adapter: LocalFileListAdapter

    override fun getLayout(): FragmentHiddenFilesBinding {
        return FragmentHiddenFilesBinding.inflate(LayoutInflater.from(requireContext()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = LocalFileListAdapter()
        checkCameraGalleryPermission()
        binding.rvSavedFiles.adapter = adapter

        binding.btnShowAll.setOnClickListener {
            if (adapter.itemCount > 0)
                for (pos in 0 until adapter.itemCount) {
                    adapter.getItem(pos)?.let {
                        Common.showFile(it.name, requireContext()) {
                            requireActivity().runOnUiThread {
                                try {
                                    adapter.clear()
                                }catch (e:Exception){}
                            }
                        }
                    }
                }
        }
    }

    override fun mediaPermissionGranted() {
        val list = File(Common.hidePath.toURI()).listFiles()?.toList()
        if (list != null) {
            adapter.set(list)
        }
    }

}