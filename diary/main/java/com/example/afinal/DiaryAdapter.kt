package com.example.afinal

import android.app.Application
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.diary.view.*

class DiaryAdapter : RecyclerView.Adapter<DiaryAdapter.ViewHolder>(){
    var items=ArrayList<Diary>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryAdapter.ViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.diary,parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount()=items.size

    override fun onBindViewHolder(holder: DiaryAdapter.ViewHolder, position: Int) {
        val item=items[position]
        holder.setItem(item)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun setItem(item:Diary){
            itemView.setOnClickListener {
                Intent(itemView.context, DiaryView::class.java).apply {
                    putExtra("email",item.email)
                    putExtra("id",item.id)
                    putExtra("imgN", item.imgN)
                    putExtra("date", item.date)
                    putExtra("loca",item.loca)
                    putExtra("whe",item.whe)
                    putExtra("contents", item.contents)
                    putExtra("title", item.title)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { itemView.context.startActivity(this) }
            }
            itemView.idTextView.text=item.date
            if(item.imgN==1.toLong())
                itemView.face_img.setImageResource(R.drawable.img1)
            else if(item.imgN==2.toLong())
                itemView.face_img.setImageResource(R.drawable.img2)
            else if(item.imgN==3.toLong())
                itemView.face_img.setImageResource(R.drawable.img3)
            else if(item.imgN==4.toLong())
                itemView.face_img.setImageResource(R.drawable.img4)
            if(item.whe==1.toLong())
                itemView.whe_img.setImageResource(R.drawable.w1)
            else if(item.whe==2.toLong())
                itemView.whe_img.setImageResource(R.drawable.w2)
            else if(item.whe==3.toLong())
                itemView.whe_img.setImageResource(R.drawable.w3)
            else if(item.whe==4.toLong())
                itemView.whe_img.setImageResource(R.drawable.w4)
            itemView.idTextView2.text=item.title
        }
    }
}