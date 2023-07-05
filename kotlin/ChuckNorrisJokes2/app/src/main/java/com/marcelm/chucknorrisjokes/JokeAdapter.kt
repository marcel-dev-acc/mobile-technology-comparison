package com.marcelm.chucknorrisjokes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.marcelm.chucknorrisjokes.Joke
import com.marcelm.chucknorrisjokes.R

class JokeAdapter(private val jokes: List<Joke>) :
    RecyclerView.Adapter<JokeAdapter.JokeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_joke, parent, false)
        return JokeViewHolder(view)
    }

    override fun onBindViewHolder(holder: JokeViewHolder, position: Int) {
        val joke = jokes[position]
        holder.jokeTextView.text = joke.joke
    }

    override fun getItemCount(): Int {
        return jokes.size
    }

    class JokeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val jokeTextView: TextView = itemView.findViewById(R.id.jokeText)
    }
}
