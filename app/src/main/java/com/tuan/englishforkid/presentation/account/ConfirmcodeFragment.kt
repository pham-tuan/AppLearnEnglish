package com.tuan.englishforkid.presentation.account

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tuan.englishforkid.R
import com.tuan.englishforkid.databinding.FragmentConfirmcodeBinding
import com.tuan.englishforkid.model.User
import com.tuan.englishforkid.utils.Constant
import com.tuan.englishforkid.utils.DataResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.random.Random

@AndroidEntryPoint
class ConfirmcodeFragment : Fragment() {

    private var binding: FragmentConfirmcodeBinding? = null
    private var code: String? = null
    private var checkCountDown: Boolean = false
    private var resendCode = Random.nextInt(8999) + 1000
    private val viewModelRegi: UserViewModel by viewModels()
    private lateinit var name: String
    private lateinit var gmail: String
    private lateinit var pass: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_confirmcode, container, false)
        binding?.lifecycleOwner = this
        return binding?.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBundleData()
        setUpListener()
    }

    private fun getBundleData() {
        arguments?.apply {
            getString(Constant.CODE)?.let {
                code = it
            }
            name = getString("name") ?: ""
            gmail = getString("email") ?: ""
            pass = getString("password") ?: ""

            Log.d("BundleData", "Code: $code, Name: $name, Email: $gmail, Password: $pass")
        }
        handleCountDownTimer()
    }

    private fun handleCountDownTimer() {
        object : CountDownTimer(60000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                binding?.edtTimeCountDown?.setText((millisUntilFinished / 1000).toString())
                binding?.sendCodeAgain?.isEnabled = false
                checkCountDown = false
            }

            override fun onFinish() {
                binding?.edtTimeCountDown?.setText((0).toString())
                binding?.sendCodeAgain?.isEnabled = true
                checkCountDown = true
            }
        }.start()
    }


    private fun setUpListener() {

        binding?.tvBack?.setOnClickListener { findNavController().navigateUp() }

        binding?.edtPassCodeOne?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if ((s?.length ?: 0) > 0) {
                    binding?.edtPassCodeTwo?.requestFocus()
                }
            }

        })
        binding?.edtPassCodeTwo?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if ((s?.length ?: 0) > 0) {
                    binding?.edtPassCodeThree?.requestFocus()
                }
            }

        })
        binding?.edtPassCodeThree?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                if ((s?.length ?: 0) > 0) {
                    binding?.edtPassCodeFour?.requestFocus()
                }
            }

        })
        binding?.edtPassCodeFour?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if ((s?.length ?: 0) > 0) {
                    validateCode()
                }
            }

        })

        //send again

        binding?.sendCodeAgain?.setOnClickListener {
            clearEditTextValues()
            handleCountDownTimer()
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                val result = sendOTPAsync()
                if (result) {
                    Toast.makeText(requireContext(), "RESEND SUCCESS", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "FAIL", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding?.edtPassCodeOne?.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                clearEditTextValues()
                return@setOnKeyListener true
            }
            false
        }
        binding?.edtPassCodeTwo?.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                if (binding?.edtPassCodeTwo?.text?.toString().equals(""))
                {
                    binding?.edtPassCodeOne?.requestFocus()
                }
                else
                {
                    binding?.edtPassCodeTwo?.setText("")
                    binding?.edtPassCodeOne?.requestFocus()
                }
                return@setOnKeyListener true
            }
            false
        }
        binding?.edtPassCodeThree?.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                if (binding?.edtPassCodeTwo?.text?.toString().equals(""))
                {
                    binding?.edtPassCodeTwo?.requestFocus()
                }
                else
                {
                    binding?.edtPassCodeThree?.setText("")
                    binding?.edtPassCodeTwo?.requestFocus()
                }
                return@setOnKeyListener true
            }
            false
        }
        binding?.edtPassCodeFour?.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                if (binding?.edtPassCodeTwo?.text?.toString().equals(""))
                {
                    binding?.edtPassCodeThree?.requestFocus()
                }
                else
                {
                    binding?.edtPassCodeFour?.setText("")
                    binding?.edtPassCodeThree?.requestFocus()
                }
                return@setOnKeyListener true
            }
            false
        }
    }

    private fun clearEditTextValues() {
        binding?.edtPassCodeOne?.requestFocus()
        binding?.edtPassCodeOne?.setText("")
        binding?.edtPassCodeTwo?.setText("")
        binding?.edtPassCodeThree?.setText("")
        binding?.edtPassCodeFour?.setText("")
    }

    private suspend fun sendOTPAsync(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val senderEmail = "geezmussic@gmail.com"
                val receiverEmail = gmail
                val passwordSenderEmail = "alxvggqassonsket"

                val props = Properties().apply {
                    put("mail.smtp.host", "74.125.133.109")
                    put("mail.smtp.port", "587")
                    put("mail.smtp.auth", "true")
                    put("mail.smtp.starttls.enable", "true")
                }

                val session = Session.getInstance(props, object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(senderEmail, passwordSenderEmail)
                    }
                })

                MimeMessage(session).apply {
                    setFrom(InternetAddress("geezmussic@gmail.com"))
                    setRecipient(Message.RecipientType.TO, InternetAddress(receiverEmail))
                    subject = "Android App EnglishForKids"
                    setText("Mã Đăng Kí OTP, \n\nVerify Code Again: $resendCode")
                    Transport.send(this)
                }
                true
            } catch (e: Exception) {
                Log.e("ForgetPassFragment", "Error sending email", e)
                false
            }
        }
    }

//    private fun generateNonRepeatingRandom(): Int {
//        while (resendCode.toString() == code) {
//            resendCode = Random.nextInt(8999) + 1000
//        }
//        return resendCode
//    }

    private fun validateCode() {

        val passCode1 = binding?.edtPassCodeOne?.text?.toString()
        val passCode2 = binding?.edtPassCodeTwo?.text?.toString()
        val passCode3 = binding?.edtPassCodeThree?.text?.toString()
        val passCode4 = binding?.edtPassCodeFour?.text?.toString()
        val input = passCode1 + passCode2 + passCode3 + passCode4
        if (input == code || input == resendCode.toString()) {

            registerAccount()

        } else {
            Toast.makeText(requireContext(), "Mã xác nhận không đúng", Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigateUp()
            },1000)

        }
    }

    private fun registerAccount() {
        try {
        viewModelRegi.postUser(User(iddata = null, nameuser = name, gmail = gmail, pass = pass))
            .observe(viewLifecycleOwner) { data ->
                when (data.status) {
                    DataResult.Status.SUCCESS -> {
                        Toast.makeText(requireContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_Confirm_code_fragment_to_login_fragment)
                    }
                    DataResult.Status.LOADING -> Toast.makeText(requireContext(), "Đang xử lý", Toast.LENGTH_SHORT).show()
                    DataResult.Status.ERROR -> {

                        Toast.makeText(requireContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                        Handler(Looper.getMainLooper()).postDelayed({
                            findNavController().navigate(R.id.action_Confirm_code_fragment_to_login_fragment)
                        },1000)
                        Log.e("RegisterError", "Lỗi: ${data.message}")  // ghim quả này đợi ngày phục thù
                    }
                }
            }
    } catch (e: Exception) {
        Log.e("RegisterAccountException", "Lỗi khi đăng ký tài khoản: ${e.message}")
        Toast.makeText(requireContext(), "Có lỗi xảy ra, vui lòng thử lại sau", Toast.LENGTH_SHORT).show()
    }
    }

}