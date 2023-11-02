package com.example.nt118_project.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.nt118_project.R


class SuggestFragment : Fragment() {

    private lateinit var tv: TextView
    private lateinit var lv: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_suggest, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv = view.findViewById(R.id.tv_suggest)
        var lv_suggest = view.findViewById<ListView>(R.id.lv_suggest)
        var suggest= ArrayList<String>()
        val suggest_adapter = ArrayAdapter<String>(view.context,android.R.layout.simple_list_item_1,suggest)
        lv_suggest.setAdapter(suggest_adapter)
        for (i in 0..20){
            suggest.add("Khuyến mãi thứ $i dành cho bạn")
            suggest_adapter.notifyDataSetChanged()}
    }
}