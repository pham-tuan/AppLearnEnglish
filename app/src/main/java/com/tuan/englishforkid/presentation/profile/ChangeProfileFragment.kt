package com.tuan.englishforkid.presentation.profile

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tuan.englishforkid.databinding.FragmentChangeBinding


class ChangeProfileFragment : Fragment() {

    lateinit var binding : FragmentChangeBinding
    private var sharePreferences : SharedPreferences?= null
    private var sharePreferences2: SharedPreferences? = null

    companion object {
        val IMAGE_REQUEST_CODE= 100
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangeBinding.inflate(layoutInflater)

            showProfile()
            handleUpload()

        return binding.root
    }

    private fun handleUpload() {
        binding.profileimage.setOnClickListener {
            uploadimg()
        }

    }

    private fun uploadimg() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
            val ImageUridata = data?.data
            binding.profileimage.setImageURI(data?.data)

            //lưu img URI vao sharepreferences
            binding.btnsave.setOnClickListener {
                sharePreferences2 =
                    activity?.getSharedPreferences("SELECTIMAGE", Context.MODE_PRIVATE)  // ?: return
                with(sharePreferences2?.edit()){
                    this?.putString("selected_image_uri", ImageUridata.toString())
                    this?.apply()
                }
                Toast.makeText(context, "đã lưu thay đổi", Toast.LENGTH_SHORT).show()
            }

        }
    }


    private fun showProfile() {
        sharePreferences =
            activity?.getSharedPreferences("REGISTER", Context.MODE_PRIVATE)  // ?: return
        val name = sharePreferences?.getString("Name", "")
        val email = sharePreferences?.getString("Email", "")
        val pass = sharePreferences?.getString("Pass", "")
        binding.tvName.setText(name)
        binding.tvEmail.setText(email)
        binding.tvPass.setText(pass)
        binding.tvNameUser.setText(name)

//        sharePreferences2 =
//            activity?.getSharedPreferences("SELECTIMAGE", Context.MODE_PRIVATE)
//        val selectedImageUriString = sharePreferences2?.getString("selected_image_uri", "")
//        val selectedImageUri = Uri.parse(selectedImageUriString)

        // binding.profileimage.setImageURI(selectedImageUri)

    }
}