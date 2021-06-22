package com.example.afinal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_cal.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.bottom_navigation
import java.util.*

class CalActivity : AppCompatActivity() {
    var em=""
    private lateinit var adapter:DiaryAdapter
    private lateinit var databaseRef: DatabaseReference
    var array= Vector<String>()
    var selectYear:Int=0
    var selectMonth:String=""
    var selectDay:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cal)

        em= intent.getStringExtra("email").toString()
        Log.e("main", em)
        adapter = DiaryAdapter()
        databaseRef= FirebaseDatabase.getInstance().reference

        databaseRef.orderByKey().limitToFirst(10).addValueEventListener(object :
                ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.e("test", "loadItem:onCancelled:${error.toException()}")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                loadDiaryList(snapshot)
            }
        })

        calenderView1.setOnDateChangeListener{calenderView, i, i2, i3->
            selectYear=i
            if((i2+1)<10)
                selectMonth="0"+(i2+1).toString()
            else
                selectMonth=(i2+1).toString()
            selectDay=i3
            var num=0
            var it:Iterator<String> =array.iterator()
            while(it.hasNext()){
                if((it.next()).contentEquals("${selectYear.toString()+"-"+selectMonth+"-"+selectDay.toString()}")) {
                    num=num+1
                }
            }
            tv_num.text="일기: "+num+"건"
        }



        bottom_navigation.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.tab1 -> {


                    return@setOnNavigationItemSelectedListener true
                }
                R.id.tab2 -> {

                    var intent= Intent(this@CalActivity,MainActivity::class.java)
                    intent.putExtra("email", em)
                    startActivity(intent)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.tab3 -> {

                    var intent= Intent(this@CalActivity,SetActivity::class.java)
                    intent.putExtra("email", em)
                    startActivity(intent)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            return@setOnNavigationItemSelectedListener false
        }
    }
    fun loadDiaryList(dataSnapshot: DataSnapshot){
        val collectionIterator=dataSnapshot!!.children.iterator()
        if(collectionIterator.hasNext()){
            adapter.items.clear()
            val diarys=collectionIterator.next()
            val itemsIterator=diarys.children.iterator()
            while(itemsIterator.hasNext()){
                val currentItem=itemsIterator.next()
                val map=currentItem.value as HashMap<String, Any>

                array.add( map["date"].toString())


            }
            adapter.notifyDataSetChanged()
        }
    }
}