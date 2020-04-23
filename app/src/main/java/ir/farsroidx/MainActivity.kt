package ir.farsroidx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_main)
   }

    fun rate(view: View) {
        when(seekBarStar.progress){
            0 -> Toast.makeText(this, "You can not submit 0 rate!" , Toast.LENGTH_LONG).show()
            1 -> ratingView.addReview(Review.STAR_1)
            2 -> ratingView.addReview(Review.STAR_2)
            3 -> ratingView.addReview(Review.STAR_3)
            4 -> ratingView.addReview(Review.STAR_4)
            5 -> ratingView.addReview(Review.STAR_5)
        }
    }
}
