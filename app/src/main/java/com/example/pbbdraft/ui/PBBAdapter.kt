package com.example.pbbdraft.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.example.pbbdraft.R
import com.example.pbbdraft.room.PBB
import kotlinx.android.synthetic.main.adapter_pbb.view.*


class PBBAdapter (private val pajaks: ArrayList<PBB>, private val listener: OnAdapterListener)
    : RecyclerView.Adapter<PBBAdapter.pajakViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): pajakViewHolder {
        return pajakViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_pbb, parent, false)
        )
    }

    override fun getItemCount() = pajaks.size
    override fun onBindViewHolder(holder: pajakViewHolder, position: Int) {
        val pajak = pajaks[position]
        holder.view.text_title.text = pajak.nama
        holder.view.NOP.text = pajak.NOP
        holder.view.Alamat.text = pajak.alamat
        holder.view.luasTanah.text = pajak.luasTanah.toString()
        holder.view.pajakDitetapkan.text = pajak.pajakDitetapkan.toString()
        holder.view.text_title.setOnClickListener{
            listener.onClik( pajak )
        }
        holder.view.icon_edit.setOnClickListener{
            listener.onUpdate( pajak )
        }
        holder.view.icon_delete.setOnClickListener{
            listener.onDelete( pajak )
        }
    }
    class pajakViewHolder(val view: View) : RecyclerView.ViewHolder(view)
    fun setData(list: List<PBB>){
        pajaks.clear()
        pajaks.addAll(list)
        notifyDataSetChanged()
    }
    interface OnAdapterListener{
        fun onClik(pajak : PBB)
        fun onUpdate(pajak : PBB)
        fun onDelete(pajak : PBB)
    }
}