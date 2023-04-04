package com.example.onsaveinstancestatepro

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onsaveinstancestatepro.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var dataList : MutableList<String>
    lateinit var myAdapter: MyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        dataList = mutableListOf<String>()
        // 1. 번들에 데이터가 있으면 가져와서 기존 변수에 저장하고, 없으면 무시한다.
        if(savedInstanceState != null){
            dataList = savedInstanceState.getStringArrayList("dataList")!!.toMutableList()
        } else {
            dataList = mutableListOf<String>()
        }
        // 2. 인텐트를 돌려받을 리퀘스트 런처를 만든다.
        val activityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            val result : String = it.data?.getStringExtra("result").toString()
            Log.e("MainAcvity", "===${result}..")
            if(result != null && !result.equals("")){
                dataList.add(result)
            }
            myAdapter.notifyDataSetChanged()
        }

        // 3. MyAdapter와 리사이클러 뷰와 연결, 보여 줄 리스트 모양 결정
        val layoutManager = LinearLayoutManager(this)
        binding.mainRecyclerView.layoutManager = layoutManager
        myAdapter = MyAdapter(dataList)
        binding.mainRecyclerView.adapter = myAdapter
        binding.mainRecyclerView.addItemDecoration(DividerItemDecoration(this,LinearLayoutManager.VERTICAL))



        // 4. floating tab 클릭하면 인텐트를 요청한다.
        binding.mainFab.setOnClickListener{
            val intent = Intent(this, AddActivity::class.java)
            activityResultLauncher.launch(intent)
        }

        // 5. sharedPreference 저장하는 방법
        binding.btnShared.setOnClickListener {
            // 5-1. sharedPreference 가져오기
            val userLocalData = getSharedPreferences("dataList", MODE_PRIVATE)
            val editor = userLocalData!!.edit()
            val gson = Gson()
            val json : String = gson.toJson(dataList)
            editor.putString("oneMessage", json)
            editor.commit()
            Toast.makeText(applicationContext,"sharedPreference 저장", Toast.LENGTH_SHORT).show()
        }
        binding.btnRevert.setOnClickListener {
            val userLocalData = getSharedPreferences("dataList", MODE_PRIVATE)
            val jsonData = userLocalData.getString("oneMessage", null)
            val type : Type = object : TypeToken<ArrayList<String>>() {}.type
            val gson = Gson()
            dataList = gson.fromJson<Any>(jsonData,type) as ArrayList<String>

            val layoutManager = LinearLayoutManager(this)
            binding.mainRecyclerView.layoutManager = layoutManager
            myAdapter = MyAdapter(dataList)
            binding.mainRecyclerView.adapter = myAdapter
            binding.mainRecyclerView.addItemDecoration(DividerItemDecoration(this,LinearLayoutManager.VERTICAL))
            Toast.makeText(applicationContext,"복구 완", Toast.LENGTH_SHORT).show()
        }

    }


    // 5. 화면을 회전 할 경우에 기존의 내용을 번들에 저장해놔야함
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList("dataList", ArrayList(dataList))
    }
}