package com.example.afinal

import android.app.DatePickerDialog
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.*
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.choice.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringReader
import java.net.URL
import java.util.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var videoMark : GroundOverlayOptions
    private lateinit var adapter:DiaryAdapter
    private lateinit var databaseRef: DatabaseReference
    var Year: Int = 0
    var Month: String=""
    var Day: Int=0
    var radioN=0
    var locationClient: FusedLocationProviderClient?=null
    var locX:Double=0.toDouble()
    var locY:Double=0.toDouble()
    var PTY:String=""
    var SKY:String=""
    var whether:Long=0
    var em=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        em= intent.getStringExtra("email").toString()
        Log.e("main", em)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.setReverseLayout(true)  // 최신것 먼저 나오도록 역순으로 저장
        layoutManager.setStackFromEnd(true)
        recyclerView.layoutManager = layoutManager

        adapter = DiaryAdapter()
        recyclerView.adapter=adapter

        databaseRef= FirebaseDatabase.getInstance().reference


        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.LOCATION)
                .onGranted{permissions->
                    Log.d("Main","허용된 권한 갯수 : ${permissions.size}")
                }
                .onDenied{permissions->
                    Log.d("Main","거부된 권한 갯수 : ${permissions.size}")
                }
                .start()


        create.setOnClickListener {
            setContentView(R.layout.choice)
            btn_date.setOnClickListener {
                    val today = GregorianCalendar()
                    val year : Int = today.get(Calendar.YEAR)
                    val month : Int = today.get(Calendar.MONTH)
                    val day : Int = today.get(Calendar.DATE)
                    val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                            Year=year
                            if(month+1<10)
                                Month="0"+(month+1).toString()
                            else
                                Month=(month+1).toString()
                            Day=dayOfMonth
                            tv_date.text="날짜: "+"${year}년 ${month+1}월 ${dayOfMonth}일"
                        }
                    }, year, month, day)
                    dlg.show()
            }

            r1.setOnClickListener { radioN=1 }
            r2.setOnClickListener { radioN=2 }
            r3.setOnClickListener { radioN=3 }
            r4.setOnClickListener { radioN=4 }

            btn_loc.setOnClickListener {
                if(Year==0)
                    Toast.makeText(applicationContext, "날짜를 입력해주세요", Toast.LENGTH_SHORT).show()
                else {
                    var dlg = AlertDialog.Builder(this@MainActivity)
                    var dialogView = View.inflate(this@MainActivity, R.layout.location, null)
                    dlg.setView(dialogView)
                    dlg.setTitle("위치 정보 입력")
                    val mapFragment = supportFragmentManager.findFragmentById(R.id.mapp) as SupportMapFragment?
                    if (mapFragment != null) {
                        mapFragment.getMapAsync(this)
                    }
                    locationClient = LocationServices.getFusedLocationProviderClient(this)
                    try {
                        locationClient?.lastLocation?.addOnSuccessListener { location ->
                            if (location == null) {
                                Toast.makeText(applicationContext, "위치 확인 실패", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(applicationContext, "위치 확인 성공", Toast.LENGTH_SHORT).show()
                                locX = location.latitude
                                locY = location.longitude
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 15f))
                            }
                        }
                                ?.addOnFailureListener {
                                    Toast.makeText(applicationContext, "${it.message}", Toast.LENGTH_SHORT).show()
                                    it.printStackTrace()
                                }
                    } catch (e: SecurityException) {
                        e.printStackTrace()
                    }
                    dlg.setPositiveButton("확인") { dialog, which ->
                        val serviceUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst"
                        val serviceKey = "k3wM1TXPhbhj5vXu4xEPdXmGnstnTdM6Sn2IspkOxAPDWj%2B12Ez%2BOhd4IjkCwyXSGo8yeakKLoPv8eh%2B36Y3og%3D%3D"
                        var x = locX.toLong()
                        var y = locY.toLong()
                        val requestUrl = serviceUrl + "?serviceKey=" + serviceKey + "&pageNo=1&numOfRows=10&dataType=XML&base_date=${Year}" + Month + "${Day}&base_time=0500&nx=" + x.toString() + "&ny=" + y.toString()
                        Log.d("Main", "위치 : ${x}, ${y}")
                        fetchXML(requestUrl)
                        Log.d("Main", "URL : ${Year}" + Month + "${Day}")
                        if (PTY == "" || SKY == "")
                            whether = 1
                        if (PTY == "0") {
                            if (SKY == "1" || SKY == "3")
                                whether = 1
                            else if (SKY == "4")
                                whether = 2
                        } else if (PTY == "1" || PTY == "4" || PTY == "5") {
                            whether = 3
                        } else if (PTY == "2" || PTY == "3" || PTY == "6" || PTY == "7")
                            whether = 4
                    }

                    dlg.show()
                }
            }



            btn_can.setOnClickListener {
                var intent = Intent(this@MainActivity, MainActivity::class.java)
                intent.putExtra("email", em)
                startActivity(intent)
                finish()
            }

            btn_save.setOnClickListener {
                if(Year==0)
                    Toast.makeText(applicationContext, "날짜를 입력해주세요", Toast.LENGTH_SHORT).show()
                else if(radioN==0)
                    Toast.makeText(applicationContext, "기분을 정해주세요", Toast.LENGTH_SHORT).show()
                else if(locX==0.toDouble()&&locY==0.toDouble())
                    Toast.makeText(applicationContext, "위치를 추가해주세요", Toast.LENGTH_SHORT).show()
                else {
                    val email = intent.getStringExtra("email")
                    val date = "${Year}-"+Month+"-${Day}"
                    val imgN = radioN
                    val whe = whether
                    val loca = locX.toString()+","+locY.toString()
                    val title = input1.text
                    val contents = input2.text

                    saveDiary(email.toString(), date, imgN.toLong(), whe.toLong(), loca, title.toString(), contents.toString())

                    var intent = Intent(this@MainActivity, MainActivity::class.java)
                    intent.putExtra("email", em)
                    startActivity(intent)
                    finish()
                }
            }
        }

        databaseRef.orderByKey().limitToFirst(10).addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.e("test", "loadItem:onCancelled:${error.toException()}")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                loadDiaryList(snapshot)
            }
        })

        bottom_navigation.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.tab1 -> {

                    var intent= Intent(this@MainActivity,CalActivity::class.java)
                    intent.putExtra("email", em)
                    startActivity(intent)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.tab2 -> {


                    return@setOnNavigationItemSelectedListener true
                }
                R.id.tab3 -> {

                    var intent= Intent(this@MainActivity,SetActivity::class.java)
                    Log.d("Main","이메일 : ${em}")
                    intent.putExtra("email", em)
                    startActivity(intent)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            return@setOnNavigationItemSelectedListener false
        }
    }

    fun saveDiary(email:String, date:String, imgN:Long, whe:Long, loca:String, title:String, contents:String){
        val em=intent.getStringExtra("email")
        var key : String?=databaseRef.child("diarys").push().getKey()
        val diary= Diary(key!!,email,date,imgN,whe,loca,title,contents)
        val diaryValues : HashMap<String,Any> = diary.toMap()
        diaryValues["timestamp"]=ServerValue.TIMESTAMP
        val childUpdates: MutableMap<String, Any> = HashMap()
        childUpdates["/diarys/$key"]=diaryValues
        databaseRef.updateChildren(childUpdates)
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
                val id=map["id"].toString()
                val email=map["email"].toString()
                val RecordDate= map["date"].toString()
                val imgN=map["imgN"]as Long
                val whe=map["whe"]as Long
                val loca=map["loca"].toString()
                val title=map["title"].toString()
                val contents = map["contents"]as String

                adapter.items.add(Diary(id, email, RecordDate, imgN, whe, loca, title, contents))
            }
            adapter.notifyDataSetChanged()
        }
    }
    override fun onMapReady(p0: GoogleMap) {
        mMap=p0
        try{
            mMap.isMyLocationEnabled=true
        }catch (e:SecurityException){
            e.printStackTrace()
        }
        mMap.uiSettings.isZoomControlsEnabled=true
        locX=37.568256
        locY=126.897240
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.568256,126.897240),15f))
        mMap.setOnMapClickListener {
            point->videoMark=GroundOverlayOptions().image(
                BitmapDescriptorFactory.fromResource(R.drawable.locimg))
                .position(point,200f,200f)
            mMap.clear()
            mMap.addGroundOverlay(videoMark)
            locX=point.latitude
            locY=point.longitude
        }
    }

    fun fetchXML(myURL:String){
        lateinit var page : String

        class getDangerGrade: AsyncTask<Void, Void, Void>(){
            override fun onPostExecute(result: Void?) { // xml 파싱
                super.onPostExecute(result)
                var bSet1=false
                var bSet2=false
                var bSet11=false
                var bSet22=false
                var fcstValue1 : String=""
                var fcstValue2 : String=""
                var factory= XmlPullParserFactory.newInstance()
                factory.setNamespaceAware(true)
                var xpp=factory.newPullParser()//XML 파서
                xpp.setInput(StringReader(page))
                var eventType=xpp.eventType
                while (eventType!= XmlPullParser.END_DOCUMENT){
                    if(eventType== XmlPullParser.START_DOCUMENT){}
                    else if(eventType== XmlPullParser.START_TAG){
                        var tag_name=xpp.name
                        if(tag_name.equals("category")) bSet1=true
                        else if(tag_name.equals("fcstValue")) bSet2=true
                    }
                    if(eventType== XmlPullParser.TEXT){
                        if(bSet1){
                            if(xpp.text.toString().equals("PTY"))
                                bSet11=true
                            else if(xpp.text.toString().equals("SKY"))
                                bSet22=true
                            bSet1=false
                        }
                        else if(bSet2){
                            if(bSet11){
                                fcstValue1=xpp.text.toString()
                                Log.d("Main","PTY : ${xpp.text.toString()}")
                                bSet11=false
                            }
                            else if(bSet22){
                                fcstValue2=xpp.text.toString()
                                bSet22=false
                            }
                            bSet2=false
                        }
                    }
                    if(eventType== XmlPullParser.END_TAG){}
                    eventType=xpp.next()
                }
                PTY=fcstValue1
                SKY=fcstValue2
                Log.d("Main","날씨 : ${PTY}, ${SKY}")
            }

            override fun doInBackground(vararg params: Void?): Void? { // url -> xml
                val stream = URL(myURL).openStream()
                val bufreader= BufferedReader(InputStreamReader(stream, "UTF-8"))
                var line=bufreader.readLine()
                page=""
                while (line!=null){
                    page+=line
                    line=bufreader.readLine()
                }

                return null
            }
        }

        getDangerGrade().execute()
    }
}