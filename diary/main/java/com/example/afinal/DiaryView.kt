package com.example.afinal

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import kotlinx.android.synthetic.main.choice.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringReader
import java.net.URL
import java.util.*
import kotlin.properties.Delegates

class DiaryView : AppCompatActivity(), OnMapReadyCallback {
    var em=""
    private lateinit var mMap: GoogleMap
    private lateinit var videoMark : GroundOverlayOptions
    var locationClient: FusedLocationProviderClient?=null
    var locX:Double=0.toDouble()
    var locY:Double=0.toDouble()
    private lateinit var databaseRef: DatabaseReference
    lateinit var date:String
    var imgN by Delegates.notNull<Long>()
    lateinit var title:String
    lateinit var contents:String
    lateinit var loca: String
    var whe by Delegates.notNull<Long>()
    lateinit var id:String
    lateinit var email:String
    var Year: Int = 0
    var Month: String="0"
    var Day: Int=0
    var PTY:String=""
    var SKY:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choice)

        em= intent.getStringExtra("email").toString()
        Log.e("main", em)
        databaseRef= FirebaseDatabase.getInstance().reference

        loca=intent.getSerializableExtra("loca").toString()
        var token=loca.split(",")
        locX=token[0].toDouble()
        locY=token[1].toDouble()

        id=intent.getSerializableExtra("id").toString()
        email=intent.getSerializableExtra("email").toString()
        date = intent.getSerializableExtra("date").toString()
        tv_date.text=date.toString()
        imgN=intent.getSerializableExtra("imgN")as Long
        if(imgN==1.toLong())
            r1.isChecked=true
        else if(imgN==2.toLong())
            r2.isChecked=true
        else if(imgN==3.toLong())
            r3.isChecked=true
        else if(imgN==4.toLong())
            r4.isChecked=true
        title=intent.getSerializableExtra("title").toString()
        input1.setText(title)
        contents=intent.getSerializableExtra("contents").toString()
        input2.setText(contents)

        whe=intent.getSerializableExtra("whe")as Long

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
                    date="${Year}-"+Month+"-${Day}"
                }
            }, year, month, day)
            dlg.show()
        }

        r1.setOnClickListener { imgN=1}
        r2.setOnClickListener { imgN=2}
        r3.setOnClickListener { imgN=3}
        r4.setOnClickListener { imgN=4}

        btn_loc.setOnClickListener {
            var dlg= AlertDialog.Builder(this@DiaryView)
            var dialogView = View.inflate(this@DiaryView, R.layout.location, null)
            dlg.setView(dialogView)
            dlg.setTitle("위치 정보 입력")
            val mapFragment=supportFragmentManager.findFragmentById(R.id.mapp)as SupportMapFragment?
            if (mapFragment != null) {
                mapFragment.getMapAsync (this)
            }
            locationClient= LocationServices.getFusedLocationProviderClient(this)
            try{
                locationClient?.lastLocation?.addOnSuccessListener { location->
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(locX.toDouble(), locY.toDouble()),15f))
                }
                        ?.addOnFailureListener{
                            Toast.makeText(applicationContext, "${it.message}", Toast.LENGTH_SHORT).show()
                            it.printStackTrace()
                        }
            }
            catch (e:SecurityException){
                e.printStackTrace()
            }
            dlg.setPositiveButton("확인"){dialog,which->
                loca = locX.toString()+","+locY.toString()
            }

            dlg.show()
        }

        btn_can.setOnClickListener {
            var intent= Intent(this@DiaryView,MainActivity::class.java)
            intent.putExtra("email", em)
            startActivity(intent)
            finish()
        }


        btn_save.setOnClickListener {

            val date = date
            val imgN = imgN
            val whe = whe
            val loca = loca
            val title = input1.text
            val contents = input2.text

            updateDiary(date, imgN.toLong(), whe.toLong(), loca, title.toString(), contents.toString())

            var intent= Intent(this@DiaryView,MainActivity::class.java)
            intent.putExtra("email", em)
            startActivity(intent)
            finish()

        }
    }
    fun updateDiary(date:String, imgN:Long, whe:Long, loca:String, title:String, contents:String){
        val em=email
        val key=id
        val diary= Diary(key!!,email,date,imgN,whe,loca,title,contents)
        val diaryValues : HashMap<String,Any> = diary.toMap()
        diaryValues["timestamp"]= ServerValue.TIMESTAMP
        val childUpdates: MutableMap<String, Any> = HashMap()
        childUpdates["/diarys/$key"]=diaryValues
        databaseRef.updateChildren(childUpdates)
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap=p0
        try{
            mMap.isMyLocationEnabled=true
        }catch (e:SecurityException){
            e.printStackTrace()
        }
        mMap.uiSettings.isZoomControlsEnabled=true
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(locX.toDouble(), locY.toDouble()),15f))
        mMap.setOnMapClickListener {
            point->videoMark= GroundOverlayOptions().image(
                BitmapDescriptorFactory.fromResource(R.drawable.locimg))
                .position(point,200f,200f)
            mMap.clear()
            mMap.addGroundOverlay(videoMark)
            locX=point.latitude
            locY=point.longitude

            var token=date.split("-")
            var y=token[0]
            var m=token[1]
            var d=token[2]
            var lx=locX.toLong()
            var ly=locY.toLong()
            val serviceUrl="http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst"
            val serviceKey="k3wM1TXPhbhj5vXu4xEPdXmGnstnTdM6Sn2IspkOxAPDWj%2B12Ez%2BOhd4IjkCwyXSGo8yeakKLoPv8eh%2B36Y3og%3D%3D"
            val requestUrl=serviceUrl+"?serviceKey="+serviceKey+"&pageNo=1&numOfRows=10&dataType=XML&base_date=${y}${m}${d}&base_time=0500&nx="+lx.toString()+"&ny="+ly.toString()
            fetchXML(requestUrl)
            Log.d("Main","위치 : ${requestUrl}")
            if(PTY==""||SKY=="")
                whe=1
            if(PTY=="0"){
                if(SKY=="1"||SKY=="3")
                    whe=1
                else if(SKY=="4")
                    whe=2
            }else if(PTY=="1"||PTY=="4"||PTY=="5"){
                whe=3
            }else if(PTY=="2"||PTY=="3"||PTY=="6"||PTY=="7")
                whe=4
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