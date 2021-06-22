package com.example.afinal

import com.google.firebase.database.Exclude

data class Diary(
    var id: String,
    var email: String,
    var date: String,
    var imgN: Long,
    var whe: Long,
    var loca: String,
    var title: String,
    var contents: String,
    var timestamp: Long=0
) {
    @Exclude
    fun toMap(): HashMap<String, Any>{
        val result: HashMap<String, Any> = HashMap()
        result["id"]=id
        result["email"]=email
        result["date"]=date
        result["imgN"]=imgN
        result["whe"]=whe
        result["loca"]=loca
        result["title"]=title
        result["contents"]=contents
        result["timestamp"]=timestamp
        return result
    }
}