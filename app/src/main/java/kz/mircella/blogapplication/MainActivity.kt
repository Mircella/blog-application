package kz.mircella.blogapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.app_bar_main.toolbar
import kotlinx.android.synthetic.main.main.drawer_layout
import kotlinx.android.synthetic.main.main.nav_view
import kotlinx.android.synthetic.main.user_profile.view.nav_header_user_profile_login_tv
import kotlinx.android.synthetic.main.user_profile.view.nav_header_user_profile_status_tv
import kz.mircella.blogapplication.databinding.MainBinding
import kz.mircella.blogapplication.navigation.Navigator
import kz.mircella.blogapplication.userprofile.UserProfileViewModel
import kz.mircella.blogapplication.utils.extensions.observe
import me.vponomarenko.injectionmanager.x.XInjectionManager
import timber.log.Timber

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var userProfileViewModel: UserProfileViewModel
    private lateinit var mainBinding: MainBinding
    private val navigator: Navigator by lazy {
        XInjectionManager.findComponent<Navigator>()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = DataBindingUtil.setContentView(this, R.layout.main)
        setSupportActionBar(toolbar)
        setupUserProfile()
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_slideshow)
        }
        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun setupUserProfile() {
        userProfileViewModel = ViewModelProviders.of(this).get(UserProfileViewModel::class.java)
        observe(userProfileViewModel.userProfileLogin, { nav_view.getHeaderView(0).nav_header_user_profile_login_tv.text = it })
        observe(userProfileViewModel.userProfileStatus, { nav_view.getHeaderView(0).nav_header_user_profile_status_tv.text = it })
    }

    @SuppressLint("LogNotTimber", "CheckResult")
    override fun onResume() {
        super.onResume()
        navigator.bind(findNavController(R.id.nav_host_fragment))
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_item_home -> {
                navigator.openHomeFragment()
            }
            R.id.nav_item_video -> {
                navigator.openVideoFragment()
            }
            R.id.nav_item_forum -> {
                navigator.openForumFragment()
            }
            else -> {
                Timber.d("Smth else was pressed")
            }
        }
//        setTitle(item.title)
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPause() {
        super.onPause()
        navigator.unbind()
    }

    override fun onSupportNavigateUp(): Boolean = findNavController(R.id.nav_host_fragment).navigateUp()
}
