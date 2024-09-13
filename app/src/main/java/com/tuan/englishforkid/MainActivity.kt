package com.tuan.englishforkid

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.tuan.englishforkid.brocastreceivers.BroadcastCheckInternet
import com.tuan.englishforkid.databinding.ActivityMainBinding
import com.tuan.englishforkid.di.NetworkConnection
import com.tuan.englishforkid.utils.Constant
import dagger.hilt.android.AndroidEntryPoint
import pl.droidsonroids.gif.GifDrawable
import java.util.Objects


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var navController: NavController? = null
    private lateinit var binding: ActivityMainBinding
    private var dialog: AlertDialog? = null
    var br = BroadcastCheckInternet()
    private var sharePreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityMainBinding.inflate(layoutInflater)
        sharePreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE)

        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(br, filter)

        disableSwipeDrawerLayout()
        handleDrawer()
        isetUpNavigationFragment()
        initView()
        setHeaderDrawer()

        setContentView(binding.root)


    }

    private fun setHandelAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this, R.style.Themecustom)
        val view = layoutInflater.inflate(R.layout.custom_dialog_logout, null)

        alertDialogBuilder.setView(view)

        val btnClose = view.findViewById<ImageView>(R.id.ivClose)
        btnClose.setOnClickListener {
            dialog?.dismiss()
        }

        val btnConfirm = view.findViewById<Button>(R.id.btnConfirm)
        btnConfirm.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            dialog?.dismiss()

            // Đăng xuất người dùng
            sharePreferences?.edit()?.putBoolean(Constant.CHECK_LOGIN, false)?.apply()

            // Xóa back stack và chuyển đến LoginFragment
            findNavController(R.id.nav_host_fragment).navigate(
                R.id.LoginFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.nav_graph, true)  // Thay R.id.nav_graph bằng ID của nav graph của bạn
                    .build()
            )

            // Xóa toàn bộ back stack của FragmentManager
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

            // Tạo Intent mới để khởi động lại Activity với task mới
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }

        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        btnCancel.setOnClickListener {
            dialog?.dismiss()
        }

        val imgGif = view.findViewById<ImageView>(R.id.img)
        val gifDrawable = GifDrawable(resources, R.drawable.logout)
        imgGif.setImageDrawable(gifDrawable)
        gifDrawable.start()

        dialog = alertDialogBuilder.create()
        dialog?.show()
    }

    private fun setHeaderDrawer() {
        val nameProfile = binding.naview.getHeaderView(0).findViewById<TextView>(R.id.nameUser)
        val sharedPreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
        nameProfile.text = sharedPreferences?.getString("NameLogin", "") ?: "tuanĐZ"
    }

    private fun disableSwipeDrawerLayout() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    private fun isetUpNavigationFragment() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

    }

    private fun handleDrawer() {
        binding.hearder.ivMenu.setOnClickListener {
            if (!binding.drawerLayout.isDrawerOpen(GravityCompat.START)) binding.drawerLayout.openDrawer(
                GravityCompat.START
            ) else binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        binding.tvOffDrawer.setOnClickListener {
            binding.drawerLayout.close()
        }
        binding.hearder.tvhead.isSelected = true
    }

    fun handleShowHeader(isShow: Boolean) {
        binding.hearder.root.visibility =
            if (isShow)
                View.VISIBLE
            else View.GONE
    }

    fun showLoading() {
        binding.indicatorProgress.visibility = View.VISIBLE
    }

    fun hideLoading() {
        binding.indicatorProgress.visibility = View.GONE
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.close()
        } else {
            super.onBackPressed()
        }
    }

    private fun initView() {
        binding.naview.setNavigationItemSelectedListener(this)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navhome -> {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                findNavController(R.id.nav_host_fragment).navigate(R.id.HomeFragment)
                true
            }

            R.id.naventertainment -> {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                findNavController(R.id.nav_host_fragment).navigate(R.id.SplashEnterFragment)
                true
            }

            R.id.navfavorite -> {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                findNavController(R.id.nav_host_fragment).navigate(R.id.favorFragment)
                true
            }

            R.id.navuser -> {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                findNavController(R.id.nav_host_fragment).navigate(R.id.InForFragment)
                true
            }

            R.id.navpractice -> {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                findNavController(R.id.nav_host_fragment).navigate(R.id.pactiveFragment)
                true
            }
            R.id.navlisten -> {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                findNavController(R.id.nav_host_fragment).navigate(R.id.listenFragment)
                true
            }

            R.id.navSetTing -> {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                findNavController(R.id.nav_host_fragment).navigate(R.id.SetingFragment)
                true
            }

            R.id.navLogOut -> {
                setHandelAlertDialog()
                true
            }

            else -> false
        }
        return false
    }


}


fun Activity.hideKeyboard(view: View) {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.hideKeyBoard(view: View) {
    activity?.hideKeyboard(view)
}