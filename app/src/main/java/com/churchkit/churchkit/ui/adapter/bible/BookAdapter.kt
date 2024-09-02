package com.churchkit.churchkit.ui.adapter.bible

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.churchkit.churchkit.CKPreferences
import com.churchkit.churchkit.R
import com.churchkit.churchkit.database.entity.bible.BibleBook
import com.churchkit.churchkit.ui.adapter.bible.BibleAdapter.AllBookViewHolder
import com.churchkit.churchkit.ui.util.Util
import com.google.android.material.card.MaterialCardView
import java.lang.IllegalArgumentException

class BookAdapter(  ): RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

      var bibleBooks: List<BibleBook>? = null

    public fun setBibleBook(  bibleBooks: List<BibleBook> ){
        this.bibleBooks = bibleBooks
        notifyDataSetChanged()
    }


    class BookViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){

        var title: TextView
        var tileAcronym:TextView
        var number:TextView
        //var cardView: MaterialCardView
        //var img: ImageView
        var view: View

        init {
            title = itemView.findViewById(R.id.title)
            //img = itemView.findViewById(R.id.img)
            tileAcronym = itemView.findViewById(R.id.tile_abr)
            number = itemView.findViewById(R.id.number_song)
           // cardView = itemView.findViewById(R.id.cardview)

            itemView.findViewById<View?>(R.id.lang).setVisibility(View.GONE)
            view = itemView.findViewById(R.id.point)
            view.setVisibility(View.GONE)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_part, parent, false)
        return BookViewHolder(view)
    }

    override fun getItemCount() = bibleBooks?.size ?: 0


    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {

            val tempPosition = holder.absoluteAdapterPosition + 1
            val posColor = if (tempPosition < 11) tempPosition else tempPosition % 10
            val color = Util.getColorByPosition(posColor)
            holder.title.text = bibleBooks!!.get(position).title
            holder.tileAcronym.text = bibleBooks!![position].abbreviation
            holder.number.text = bibleBooks!![position].childAmount.toString() + " Chapters"
            if (CKPreferences(holder.itemView.context).getabbrColor()) {
                holder.tileAcronym.setTextColor(color)
            }
            holder.itemView.setOnClickListener { view: View? ->
                val bibleBook = bibleBooks!![position]
                val bundle = Bundle()
                bundle.putString("ID", bibleBook.id)
                bundle.putString("BOOK_NAME_ABBREVIATION", bibleBook.abbreviation)
                val navController = findNavController(view!!)
                navController.graph.findNode(R.id.listChapterFragment)?.label =
                    bibleBook.title + " "


                //navController.navigate(R.id.action_listChapterByTestamentFragment_to_listChapterFragment, bundle);
                try {
                    navController.navigate(R.id.action_bookmarkFragment_to_listChapterFragment, bundle)
                }catch (e:IllegalArgumentException){
                    navController.navigate(R.id.action_listChapterByTestamentFragment_to_listChapterFragment, bundle);

                }




            }

    }
}