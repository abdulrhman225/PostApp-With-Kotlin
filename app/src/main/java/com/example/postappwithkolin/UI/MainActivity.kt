package com.example.postappwithkolin.UI

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.postappwithkolin.Model.UserPost
import com.example.postappwithkolin.Model.Post_recycler
import com.example.postappwithkolin.R
import com.example.postappwithkolin.SourceData.SAGDataFromDataBase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), Post_recycler.OnItemClickListener {


    lateinit var iv_UserImage: ImageView
    lateinit var tv_UserName: TextView
    lateinit var main_rv: RecyclerView
    lateinit var FAB_newPost: FloatingActionButton
    lateinit var toobar :Toolbar
    lateinit var swipe:SwipeRefreshLayout


    //Initialize FireBaseAuth
    val mAuth = Firebase.auth

    //Initialize RecyclerView
    var rv: Post_recycler? = null


    var userName: String? = null
    var userPhoto: String? = null
    var userPhotoInPost: String? = null

    var posts: ArrayList<UserPost> = ArrayList()


    //Initialize SAGDataFromFireBase
    var model: SAGDataFromDataBase = SAGDataFromDataBase()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Initialize Views
        iv_UserImage = findViewById(R.id.main_IV_userImage)
        tv_UserName = findViewById(R.id.main_UserName)
        main_rv = findViewById(R.id.Main_Recycler)
        FAB_newPost = findViewById(R.id.main_FLB)
        swipe = findViewById(R.id.MainSwipe)
        toobar = findViewById(R.id.mainToolbar)


        setSupportActionBar(toobar)


        //put UserName And UserPhoto in the MainActivity
        putUserNameAndUserphoto()

        //observer to updated data
        model = ViewModelProvider(this).get(SAGDataFromDataBase::class.java)
        updateData()




        //Going to NewPostActivity
        FAB_newPost.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, NewPostActivity::class.java)
            intent.putExtra("userName", tv_UserName.text)
            intent.putExtra("userPhoto", userPhotoInPost)
            startActivity(intent)
        })

        //swipe to update data
        swipe.setOnRefreshListener {
            updateData()
            swipe.isRefreshing = false
        }

    }


    //Create Option Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = MenuInflater(this)
        inflater.inflate(R.menu.log_out_menu, menu)
        return true
    }

    //Initialize menuItem for signOut
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_Logout -> {
                mAuth.signOut()
                val intent = Intent(this, LogInActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    //Checking If there is User Or Not if Not Her Will Send Me To LoginActivity
    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser == null) {
            var intent = Intent(applicationContext, LogInActivity::class.java)
            startActivity(intent)
        } else {
            //put UserName and UserPhoto from UserInformation
            tv_UserName.text = mAuth.currentUser!!.displayName
            Picasso.get().load(mAuth.currentUser!!.photoUrl).into(iv_UserImage)
            userPhotoInPost = mAuth.currentUser!!.photoUrl.toString()
        }
    }


    //get UserName and UserPhoto from Register Activity and update the UserInformation
    fun putUserNameAndUserphoto() {
        val intent = getIntent()
        userName = intent.getStringExtra("userName")
        userPhoto = intent.getStringExtra("userPhoto")
        if (userName != null && userPhoto != null) {
            val user = mAuth.currentUser
            val updateProfile = userProfileChangeRequest {
                setDisplayName(userName)
                setPhotoUri(Uri.parse(userPhoto))

            }
            user!!.updateProfile(updateProfile).addOnCompleteListener {
                if (it.isSuccessful) {
                    tv_UserName.text = mAuth.currentUser!!.displayName.toString()
                    Picasso.get().load(userPhoto).into(iv_UserImage)
                    userPhotoInPost = userPhoto
                }


            }
        }
    }

    //send userName and userPhoto when the user click on recyclerView item
    override fun onItemClick(position: Int) {
        val user: UserPost = posts[position]
        val intent = Intent(this, profileActivity::class.java)
        intent.putExtra("UserName", user.UserName)
        intent.putExtra("UserPhoto", user.UserPhoto)
        startActivity(intent)


    }
    fun updateData(){
        model.getPosts()
        model.mutable.observe(this, Observer {
            posts.addAll(it)
            rv = Post_recycler(it, this)
            main_rv.adapter = rv
            main_rv.layoutManager = LinearLayoutManager(this)
            main_rv.setHasFixedSize(true)
        })
    }


}