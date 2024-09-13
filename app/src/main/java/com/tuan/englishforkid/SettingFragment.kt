package com.tuan.englishforkid

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.tuan.englishforkid.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        setupUI()
        SetOnclick()
        return binding.root
    }

    private fun SetOnclick() {
        sharedPreferences = activity?.getSharedPreferences("USER", Context.MODE_PRIVATE)!!
        val email = sharedPreferences.getString("Gmailp", null)

        binding.tvphoi.setOnClickListener {
            if (email != null) {
                sendmail()
            }
        }
    }

    private fun sendmail() {
            val toEmail = "tuantuan200201@gmail.com"
            val subject = "Góp Ý"
            val message = " "
            val mIntent = Intent(Intent.ACTION_SEND)

            mIntent.data = Uri.parse("mailto:")
            mIntent.type = "text/plain"
            mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(toEmail))
            mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            mIntent.putExtra(Intent.EXTRA_TEXT, message)

            try {
                startActivity(Intent.createChooser(mIntent, "Gửi Email"))
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Không thể gửi email: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    private fun setupUI() {
        // Lấy chế độ hiện tại của hệ thống
        val currentNightMode = AppCompatDelegate.getDefaultNightMode()

        // Cập nhật trạng thái của Switch dựa trên chế độ hiện tại của hệ thống
        when (currentNightMode) {
            AppCompatDelegate.MODE_NIGHT_YES -> {
                binding.switchDarkMode.isChecked = true
            }
            AppCompatDelegate.MODE_NIGHT_NO -> {
                binding.switchDarkMode.isChecked = false
            }
            else -> {
                // Xử lý các chế độ khác nếu cần (MODE_NIGHT_FOLLOW_SYSTEM, MODE_NIGHT_AUTO_BATTERY, ...)
                binding.switchDarkMode.isChecked = sharedPreferences.getBoolean("dark_mode", false)
            }
        }

        // Set listeners cho Switch
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            val newMode = if (isChecked) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }

            if (newMode != currentNightMode) {
                AppCompatDelegate.setDefaultNightMode(newMode)
                sharedPreferences.edit().putBoolean("dark_mode", isChecked).apply()
                requireActivity().recreate() // Tạo lại activity để áp dụng thay đổi
            }
        }
    }


//    private fun setupUI() {
//        // Set initial states of switches
//        binding.switchDarkMode.isChecked = sharedPreferences.getBoolean("dark_mode", false)
//
//        // Set listeners for switches
//        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            } else {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            }
//            sharedPreferences.edit().putBoolean("dark_mode", isChecked).apply()
//        }
//
//    }


}