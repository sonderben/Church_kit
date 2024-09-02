package com.churchkit.churchkit.ui.adapter.bible

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.churchkit.churchkit.R
import com.churchkit.churchkit.database.entity.base.BaseInfo
import com.churchkit.churchkit.database.entity.bible.BibleInfo
import com.churchkit.churchkit.ui.adapter.bible.BibleAdapter.GroupByTestamentViewHolder

class TestamentAdapter(var context:Context ) : RecyclerView.Adapter<TestamentAdapter.TestamentViewHolder>() {

    var amountOldTestament:Int=0
    var amountNewTestament: Int = 0
    public fun setAmountTestament( bibleInfo:BaseInfo ){
        if ( bibleInfo is BibleInfo ){
            this.amountOldTestament = bibleInfo.amountOldTestament
            this.amountNewTestament = bibleInfo.amountNewTestament
            notifyDataSetChanged()
        }

    }

    class TestamentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView
        var bookNumber:TextView
        var img: ImageView
        init {
            textView = itemView.findViewById<TextView>(R.id.name)
            img = itemView.findViewById<ImageView>(R.id.img1)
            bookNumber = itemView.findViewById<TextView>(R.id.book_number)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestamentViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_part_group_by_lang, parent, false)
        return TestamentViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (amountNewTestament>0 && amountOldTestament> 0){
            return 2
        }
        else if ( amountNewTestament==0 && amountOldTestament== 0 ){
            return 0
        }
        return 1
    }

    override fun onBindViewHolder(holder: TestamentViewHolder, position: Int) {
        val bundle = Bundle()
        if (position == 0) {
            holder .textView.setText(R.string.old_testament)
            Glide.with((holder ).img)
                .load("https://images.pexels.com/photos/5986493/pexels-photo-5986493.jpeg?cs=srgb&dl=pexels-cottonbro-studio-5986493.jpg&fm=jpg&_gl=1*1x83rz0*_ga*MTA1MjM4NDYwNy4xNjgzMDk0MzY1*_ga_8JE65Q40S6*MTY4NDE2MzU0Ny43LjEuMTY4NDE2NDExMi4wLjAuMA..")
                .placeholder(R.mipmap.img_bg_creole)
                .error(R.mipmap.img_bg_creole)
                .into((holder ).img)
            holder.bookNumber.text = "$amountOldTestament ${context.getString(R.string.books)}"


            bundle.putInt("TESTAMENT", -2)
            holder.itemView.setOnClickListener { v ->
                val navController = findNavController(v)
                navController.graph.findNode(R.id.listChapterByTestamentFragment)!!.label =
                    context.getString(R.string.old_testament)
                navController.navigate(
                    R.id.action_bookmarkFragment_to_listBookByTestamentFragment,
                    bundle
                )
            }
        }
        if (position == 1) {
            bundle.putInt("TESTAMENT", 2)
            holder.textView.setText(R.string.new_testament)
            holder.img.setImageResource(R.mipmap.nt)
            holder.bookNumber.text = "$amountNewTestament ${context.getString(R.string.books)}"


            Glide.with(holder.img)
                .asBitmap()
                .load("https://images.pexels.com/photos/8814959/pexels-photo-8814959.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")
                .error(R.mipmap.nt)
                .placeholder(R.mipmap.nt)
                .into( holder .img )
            holder.itemView.setOnClickListener { v ->
                val navController = findNavController(v)
                navController.graph.findNode(R.id.listChapterByTestamentFragment)?.label =
                    context.getString(R.string.new_testament)
                navController.navigate(
                    R.id.action_bookmarkFragment_to_listBookByTestamentFragment,
                    bundle
                )
            }
        }
    }
}