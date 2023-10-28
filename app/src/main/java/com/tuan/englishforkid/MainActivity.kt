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
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.navigation.NavigationView
import com.tuan.englishforkid.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Objects

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var navController: NavController? = null
    private lateinit var binding: ActivityMainBinding
    private var dialog: AlertDialog? = null
    private var sharePreferences: SharedPreferences? = null
    var br = BroadcastCheckInternet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityMainBinding.inflate(layoutInflater)

        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(br, filter)

        disableSwipeDrawerLayout()
        handleDrawer()
        isetUpNavigationFragment()
        initView()
        setHeaderDraer()
        setContentView(binding.root)
    }

    private fun checkInternet() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (NetworkConnection().isNetworkAvailable(Objects.requireNonNull(this))) {
                findNavController(R.id.HomeFragment)
            } else {
                showDialog()
            }
        }
    }

    private fun setHandelAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this, R.style.Themecustom)
        val view = layoutInflater.inflate(R.layout.custom_dialog_logout, null)

        alertDialogBuilder.setView(view)

        var btnClose = view.findViewById<ImageView>(R.id.ivClose)
        btnClose.setOnClickListener {
            dialog?.dismiss()
        }
        var btnConfirm = view.findViewById<Button>(R.id.btnConfirm)
        btnConfirm.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            dialog?.dismiss()
            findNavController(R.id.nav_host_fragment).navigate(R.id.LoginFragment)
        }
        var btnCancel = view.findViewById<Button>(R.id.btnCancel)
        btnCancel.setOnClickListener {
            dialog?.dismiss()
        }

        dialog = alertDialogBuilder.create()
        dialog?.show()
    }

    private fun setHeaderDraer() {
        // val view = layoutInflater.inflate(R.layout.nav_header,null)
        val headerView: View = binding.naview.getHeaderView(0)
        var nameProfile = headerView.findViewById<TextView>(R.id.nameUser)
        // hiển thị lên headrerDrawer

        sharePreferences =
            this.getSharedPreferences("LOGIN", Context.MODE_PRIVATE)  // ?: return
        val data1 = sharePreferences?.getString("NameLogin", "") ?: "tuanĐZ"
        nameProfile.setText(data1)
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

    private fun showDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.check_internet)
        builder.setMessage(R.string.check_your_internet)
        builder.setCancelable(false)
        builder.setNegativeButton(R.string.exit) { dialog, which ->
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
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
                findNavController(R.id.nav_host_fragment).navigate(R.id.EntertainmentFragment)
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