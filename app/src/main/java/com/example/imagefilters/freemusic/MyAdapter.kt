package com.example.imagefilters.freemusic

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.imagefilters.R
import com.squareup.picasso.Picasso

// Adapter: Connects the User Interfaces with Data Sources
class MyAdapter(val context: Activity, val dataList: List<Data>):
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private var mediaPlayer: MediaPlayer? = null
    private var playingHolder: MyViewHolder? = null
    private var currentPlayingPosition: Int = -1
    private var lastPlayedPosition: Int = -1

    // Called when RecyclerView needs a new ViewHolder of the given type to represent an item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // create the view in case the layout manager fails to create view for the data
        val itemView = LayoutInflater.from(context).inflate(R.layout.each_item, parent, false)
        return MyViewHolder(itemView)
    }

    // Returns the total number of items in the data set held by the adapter
    override fun getItemCount(): Int {
        return dataList.size;
    }

    // Called by RecyclerView to display the data at the specified position
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // populate the data into the view
        val currentData = dataList[position]

        // val mediaPlayer = MediaPlayer.create(context, currentData.preview.toUri())
        holder.title.text = currentData.title

        val originalBgColor: Int = ContextCompat.getColor(context, android.R.color.darker_gray)
        val buttonDownColor: Int = ContextCompat.getColor(context, R.color.buttonDown)
        holder.pause.backgroundTintList = ColorStateList.valueOf(originalBgColor)
        holder.play.backgroundTintList = ColorStateList.valueOf(originalBgColor)

        Picasso.get().load(currentData.album.cover).into(holder.image);

        if (lastPlayedPosition == holder.adapterPosition) {
            holder.play.backgroundTintList = ColorStateList.valueOf(buttonDownColor)
        } else {
            holder.play.backgroundTintList = ColorStateList.valueOf(originalBgColor)
        }

        holder.play.setOnClickListener {
            val currentPosition = holder.adapterPosition
            // Update lastPlayedPosition
            lastPlayedPosition = currentPosition



            // if still playing songs before, restore original color
            if (playingHolder != null) {
                playingHolder?.play?.backgroundTintList = ColorStateList.valueOf(originalBgColor)
            }

            if (currentPlayingPosition == position && mediaPlayer != null
                && mediaPlayer?.isPlaying == true) {
                mediaPlayer?.start()
                // Set OnCompletionListener to reset button color when media player finishes playing
                mediaPlayer?.setOnCompletionListener {
                    // restore the original color
                    holder.play.backgroundTintList = ColorStateList.valueOf(originalBgColor)
                    // Play the next song

                }
                holder.play.backgroundTintList = ColorStateList.valueOf(buttonDownColor)

            } else {
                stopPreviousMediaPlayer()
                mediaPlayer = MediaPlayer.create(context, currentData.preview.toUri())
                mediaPlayer?.start()
                // Set OnCompletionListener to reset button color when media player finishes playing
                mediaPlayer?.setOnCompletionListener {
                    // restore the original color
                    holder.play.backgroundTintList = ColorStateList.valueOf(originalBgColor)
                }
                playingHolder = holder
                currentPlayingPosition = holder.adapterPosition
//                holder.play.setColorFilter(ContextCompat.getColor(context, R.color.buttonDown),
//                    PorterDuff.Mode.SRC_IN)
                playingHolder?.play?.backgroundTintList = ColorStateList.valueOf(buttonDownColor)
            }
        }

        holder.pause.setOnClickListener {
            //playingHolder?.play?.setColorFilter(null)
            playingHolder?.play?.backgroundTintList = ColorStateList.valueOf(originalBgColor)
            holder.play.backgroundTintList = ColorStateList.valueOf(originalBgColor)
            mediaPlayer?.pause()

            holder.play.setOnClickListener {
                mediaPlayer?.start()
                playingHolder?.play?.backgroundTintList = ColorStateList.valueOf(buttonDownColor)
            }
        }

    }


    private fun playNextSong(position: Int) {
        // check if out of range
        if (position >= 0 && position < dataList.size) {
            val nextData = dataList[position]
            stopPreviousMediaPlayer()
        }
    }

    private fun stopPreviousMediaPlayer() {
        playingHolder?.let {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image: ImageView
        val title: TextView
        val play: ImageButton
        val pause: ImageButton

        init {
            image = itemView.findViewById(R.id.musicImage)
            title = itemView.findViewById(R.id.musicTitle)
            play = itemView.findViewById(R.id.btnPlay)
            pause = itemView.findViewById(R.id.btnPause)
        }
    }

}





















