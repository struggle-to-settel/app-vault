package com.example.test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.example.speedmatch.databinding.FragmentSavedFilesBinding
import com.example.test.adapter.LocalFileListAdapter
import java.io.File
import java.lang.Exception

class SavedFileFragment : BasePermissionFragment<FragmentSavedFilesBinding>() {

    private lateinit var adapter: LocalFileListAdapter

    override fun getLayout(): FragmentSavedFilesBinding {
        return FragmentSavedFilesBinding.inflate(LayoutInflater.from(requireContext()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = LocalFileListAdapter()
        binding.rvSavedFiles.adapter = adapter
        checkCameraGalleryPermission()
        binding.btnHideAll.setOnClickListener {
            if (adapter.itemCount > 0)
                for (pos in 0 until adapter.itemCount) {
                    adapter.getItem(pos)?.let {
                        Common.hideFile(it.name, requireContext()) {
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
        val list = File(Common.savePath.toURI()).listFiles()?.toList()
        list?.forEach {
            if(!it.toString().startsWith(Common.hidePath.absolutePath))
                adapter.add(it)
        }
    }

}