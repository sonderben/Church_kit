package com.churchkit.churchkit.ui.more

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.churchkit.churchkit.R


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class DeveloperFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_developer, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        val dataSet = listOf(
            DeveloperData("Firebase","https://firebase.google.com/?gad=1&gclid=CjwKCAjw7c2pBhAZEiwA88pOFx7fwf9XffCACzu6iIkdumBcVIq2WNxNpoAoVOiPvQKC_v5KAoozTBoC9poQAvD_BwE&gclsrc=aw.ds&hl=es-419"),
                    DeveloperData("Room","https://developer.android.com/jetpack/androidx/releases/room?hl=es-419"),
            DeveloperData("Preference","https://developer.android.com/reference/androidx/preference/package-summary"),
            DeveloperData("Navigation-fragment","https://developer.android.com/guide/navigation/navigation-getting-started?hl=es-419"),
            DeveloperData("Espresso","https://developer.android.com/training/testing/espresso?hl=es-419"),
            DeveloperData("Rxandroid","https://github.com/ReactiveX/RxAndroid"),
            DeveloperData("Gson","https://github.com/google/gson"),
            DeveloperData("Splashscreen","https://developer.android.com/develop/ui/views/launch/splash-screen"),
            DeveloperData("Glide","https://github.com/bumptech/glide"),
            DeveloperData("Balloon","https://github.com/skydoves/Balloon"),
            DeveloperData("Pexels","https://www.pexels.com/")
        )


        val customAdapter = CustomAdapter(dataSet.sortedBy { it.name })

        recyclerView.adapter = customAdapter
        recyclerView.addItemDecoration( DividerItemDecoration(context, DividerItemDecoration.VERTICAL) )
        recyclerView.layoutManager = LinearLayoutManager(context)
    }



    class CustomAdapter(private val localDataSet: List<DeveloperData>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
        private fun openDocumentation(activity:Activity,link: String) {
            val intent = Intent(activity, DocumentationWebViewActivity::class.java)
            intent.putExtra("documentation_link", link)
            activity.startActivity(intent)
        }
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView: TextView
            init {
                textView = view.findViewById<View>(R.id.text) as TextView
            }
        }


        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

            val view: View = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.text_row_item_developer, viewGroup, false)
            return ViewHolder(view)
        }


        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {


            viewHolder.textView.text = localDataSet[position].name

            viewHolder.itemView.setOnClickListener{
                openDocumentation(it.context as Activity,localDataSet[position].link)
            }
        }


        override fun getItemCount(): Int {
            return localDataSet.size
        }
    }

    data class DeveloperData(val name: String,val link: String){

    }


}