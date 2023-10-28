package com.tuan.englishforkid.presentation.account

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tuan.englishforkid.MainActivity
import com.tuan.englishforkid.R
import com.tuan.englishforkid.databinding.FragmentLoginBinding
import com.tuan.englishforkid.model.User
import com.tuan.englishforkid.model.UserResponse
import com.tuan.englishforkid.utils.DataResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    private var sharePreferences: SharedPreferences? = null
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private val viewModelLogin: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).handleShowHeader(false)   // ẩn header menu
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.lifecycleOwner = this
        binding.loginViewModel = viewModelLogin
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        auth = Firebase.auth

        showDataFromSharePrefer()

        binding.btnLogin.setOnClickListener {
            HandleSingIn()
        }
        binding.tvdangky.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_RegisterFragment)
        }

        binding.btnLoginGG.setOnClickListener {
            val signinClient = googleSignInClient.signInIntent
            launcher.launch(signinClient)
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

                result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

                if (task.isSuccessful) {
                    val account: GoogleSignInAccount? = task.result
                    val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            // Toast.makeText(context, "DONE", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Failed_login", Toast.LENGTH_SHORT).show()

                        }
                    }
                }
            } else {
                Toast.makeText(context, "Failed ", Toast.LENGTH_SHORT).show()
            }
        }

    private fun HandleSingIn() {
        if ((binding.edtuser.text.toString().trim() == "") || (binding.edtpass.text.toString()
                .trim() == "")
        ) {

            Toast.makeText(context, "Bạn đang bỏ trống thông tin ", Toast.LENGTH_SHORT).show()
        } else {
            checkUsser()
        }

    }

    private fun checkUsser() {
        val IsCheck = binding.chkRememberUser.isChecked
        viewModelLogin.getUser().observe(viewLifecycleOwner) { data ->
            when (data.status) {
                DataResult.Status.SUCCESS -> {
                    val listUser: ArrayList<User> = ArrayList()
                    val value = data.data?.body() as UserResponse

                    if (value.listUser != null) {
                        listUser.addAll(value.listUser!!)

                        if (listUser.isNotEmpty()) {
                            for (user in listUser) {
                                if (user.nameuser == binding.edtuser.text.toString().trim()
                                    && user.pass == binding.edtpass.text.toString().trim()
                                ) {
                                    if (IsCheck == false) {
                                        findNavController().navigate(R.id.action_LoginFragment_to_HomeFragment)
                                    } else {
                                        saveData()
                                    }
                                    saveProfile(user)
                                    return@observe // Đã tìm thấy người dùng khớp, không cần kiểm tra tiếp

                                }
                            }
                            // Nếu tới đây, có nghĩa không có người dùng nào khớp
                            Toast.makeText(
                                requireContext(),
                                "Bạn đã nhập sai tên hoặc mật khẩu",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.edtuser.text = null
                            binding.edtpass.text = null
                        } else {
                            // Danh sách người dùng trống
                            Toast.makeText(
                                requireContext(),
                                "Danh sách người dùng trống",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        // Dữ liệu từ server bị null
                        Toast.makeText(
                            requireContext(),
                            "Dữ liệu từ server không hợp lệ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                DataResult.Status.LOADING -> {

                }

                DataResult.Status.ERROR -> {
                    Toast.makeText(requireContext(), "lỗi data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveProfile(user: User) {
        val namep = user.nameuser
        val gmailp = user.gmail
        val passp = user.pass

        sharePreferences = activity?.getSharedPreferences("USER", Context.MODE_PRIVATE) //?: return
        with(sharePreferences?.edit()) {
            this?.putString("Namep", namep)
            this?.putString("Gmailp", gmailp)
            this?.putString("Passp", passp)
            this?.apply()
        }
    }

    private fun saveData() {

        val name = binding.edtuser.text.toString()
        val pass = binding.edtpass.text.toString()
        val saveuser = binding.chkRememberUser.isChecked
        sharePreferences = activity?.getSharedPreferences("LOGIN", Context.MODE_PRIVATE) //?: return
        with(sharePreferences?.edit()) {
            this?.putString("NameLogin", name)
            this?.putString("PassWord", pass)
            this?.putBoolean("SavePass", saveuser)
            this?.apply()
        }
        findNavController().navigate(R.id.action_LoginFragment_to_HomeFragment)
    }


    private fun showDataFromSharePrefer() {
        sharePreferences =
            activity?.getSharedPreferences("LOGIN", Context.MODE_PRIVATE)  // ?: return
        val data1 = sharePreferences?.getString("NameLogin", "")
        val data2 = sharePreferences?.getString("PassWord", "")
        val data3 = sharePreferences?.getBoolean("SavePass", false)
        binding.edtuser.setText(data1)
        binding.edtpass.setText(data2)
        binding.chkRememberUser.isChecked = data3!!

    }


}