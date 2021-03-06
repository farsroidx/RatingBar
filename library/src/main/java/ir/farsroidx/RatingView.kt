package ir.farsroidx

import android.content.Context
import android.content.res.TypedArray
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import kotlin.math.pow

class RatingView : FrameLayout {

    private lateinit var mLayoutInflater: LayoutInflater

    private lateinit var mView: View
    private lateinit var mTypedArray: TypedArray

    private lateinit var mBaseRoot: FrameLayout

    private lateinit var mPrg1: ContentLoadingProgressBar
    private lateinit var mPrg2: ContentLoadingProgressBar
    private lateinit var mPrg3: ContentLoadingProgressBar
    private lateinit var mPrg4: ContentLoadingProgressBar
    private lateinit var mPrg5: ContentLoadingProgressBar
    private lateinit var mTextRate: AppCompatTextView
    private lateinit var mTextReview: AppCompatTextView
    private lateinit var mRatingBar: AppCompatRatingBar

    private var mLayoutMode = 0
    private var mReviewText: String? = ""

    private var mEventController = EventController.getDefault()

    private val mReviewList = mutableListOf<Int>()
    private var mProgressAnimation = ProgressAnimation()

    private var onRateChange: OnRateChange? = null

    private var mFinalRate = 0f

    companion object {
        private const val ADDED_REVIEW_KEY = "addedReview"
        private const val ADDED_REVIEWS_KEY = "addedReviews"

        private const val MODE_RTL = 0
        private const val MODE_LTR = 1
    }

    constructor(context: Context) :
            this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) :
            this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        initial(context, attrs, defStyleAttr)
    }

    private fun initial(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int = 0){
        initialAttributes(context, attrs, defStyleAttr, defStyleRes)

        mEventController.addObserver<Review>(this, ADDED_REVIEW_KEY){
            mReviewList.add(getReview(it))
            syncData()
        }

        mEventController.addObserver<List<Int>>(this, ADDED_REVIEWS_KEY){
            mReviewList.addAll(it)
            syncData()
        }
    }

    private fun initialAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int = 0){
        attachingView(context)

        mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                as LayoutInflater

        attrs?.let {
            mTypedArray = context.theme!!
                .obtainStyledAttributes(
                    attrs , R.styleable.RatingView, defStyleAttr, defStyleRes
                )
            mLayoutMode = mTypedArray.getInt(R.styleable.RatingView_ratingBarMode , MODE_LTR)
            mReviewText = mTypedArray.getString(R.styleable.RatingView_ratingReviewText)
            mTypedArray.recycle()

            addModeView(getLayout(mLayoutMode))

            if (mReviewText == null){
                mReviewText = context.resources.getString(R.string.default_review)
            }

            mTextReview.text = mReviewText?.replace("#" , 0.toString())
        }
    }

    private fun attachingView(context: Context) {
        mView = inflate(context, R.layout.inflate_rating_view, this)
        mBaseRoot = mView.findViewById(R.id.mBaseRoot)
    }

    private fun addModeView(layout: View){
        mBaseRoot.removeAllViews()
        mBaseRoot.addView(layout)
        bindViews(layout)
    }

    private fun getLayout(mode: Int): View {
        if (mode == MODE_RTL) return mLayoutInflater.inflate(
            R.layout.inflate_rating_view_rtl, mBaseRoot , false
        )
        return mLayoutInflater.inflate(
            R.layout.inflate_rating_view_ltr, mBaseRoot, false
        )
    }

    private fun bindViews(view: View){
        mPrg5 = view.findViewById(R.id.prg5)
        mPrg4 = view.findViewById(R.id.prg4)
        mPrg3 = view.findViewById(R.id.prg3)
        mPrg2 = view.findViewById(R.id.prg2)
        mPrg1 = view.findViewById(R.id.prg1)
        mTextRate = view.findViewById(R.id.textRate)
        mTextReview = view.findViewById(R.id.textReview)
        mRatingBar = view.findViewById(R.id.ratingBar)
    }

    private fun syncData(){

        val mTotalReviewCount = mReviewList.size
        var mTotalReview = 0f
        var mTotalReview5 = 0
        var mTotalReview4 = 0
        var mTotalReview3 = 0
        var mTotalReview2 = 0
        var mTotalReview1 = 0

        for (i in 0 until mReviewList.size) {
            when {
                mReviewList[i] == 5 -> {
                    mTotalReview5 += 1
                    mTotalReview += 5
                }
                mReviewList[i] == 4 -> {
                    mTotalReview4 += 1
                    mTotalReview += 4
                }
                mReviewList[i] == 3 -> {
                    mTotalReview3 += 1
                    mTotalReview += 3
                }
                mReviewList[i] == 2 -> {
                    mTotalReview2 += 1
                    mTotalReview += 2
                }
                mReviewList[i] == 1 -> {
                    mTotalReview1 += 1
                    mTotalReview += 1
                }
            }
        }

        val mTotalPoint = mTotalReview5 + mTotalReview4 + mTotalReview3 + mTotalReview2 + mTotalReview1

        mProgressAnimation.setProgress5(mPrg5, mPrg5.progress, (mTotalReview5 * 100) / mTotalPoint)
        mProgressAnimation.setProgress4(mPrg4, mPrg4.progress, (mTotalReview4 * 100) / mTotalPoint)
        mProgressAnimation.setProgress3(mPrg3, mPrg3.progress, (mTotalReview3 * 100) / mTotalPoint)
        mProgressAnimation.setProgress2(mPrg2, mPrg2.progress, (mTotalReview2 * 100) / mTotalPoint)
        mProgressAnimation.setProgress1(mPrg1, mPrg1.progress, (mTotalReview1 * 100) / mTotalPoint)

        val mRate = ((mTotalReview / mTotalReviewCount) * 10.0.pow(1.0)).toInt() / 10.0.pow(1.0).toFloat()
        mFinalRate = mRate

        onRateChange?.onChange(mRate, mTotalReviewCount, mTotalPoint)

        mRatingBar.rating = mRate
        mTextRate.text = "$mRate"
        mTextReview.text = mReviewText?.replace("#" , mTotalReviewCount.toString())

        mView.requestLayout()
        mView.startAnimation(mProgressAnimation)
    }

    private fun getReview(review: Review): Int {
        return when(review){
            Review.STAR_5 -> 5
            Review.STAR_4 -> 4
            Review.STAR_3 -> 3
            Review.STAR_2 -> 2
            Review.STAR_1 -> 1
        }
    }

    fun addReview(review: Review){
        mEventController.post(ADDED_REVIEW_KEY, review)
    }

    fun addReview(reviews: List<Int>){
        mEventController.post(ADDED_REVIEWS_KEY, reviews)
    }

    fun getRate(): Float {
        return mFinalRate
    }

    fun getAllReviewsCont(): Int {
        return mReviewList.size
    }

    fun setOnRateChange(onRateChange: OnRateChange) {
        this.onRateChange = onRateChange
    }

    interface OnRateChange {
        fun onChange(rate: Float, totalReviewCount: Int, totalPoint: Int)
    }

    private inner class ProgressAnimation : Animation() {

        private var progressBar5: ContentLoadingProgressBar? = null
        private var from5 = 0
        private var to5 = 0

        private var progressBar4: ContentLoadingProgressBar? = null
        private var from4 = 0
        private var to4 = 0

        private var progressBar3: ContentLoadingProgressBar? = null
        private var from3 = 0
        private var to3 = 0

        private var progressBar2: ContentLoadingProgressBar? = null
        private var from2 = 0
        private var to2 = 0

        private var progressBar1: ContentLoadingProgressBar? = null
        private var from1 = 0
        private var to1 = 0

        init {
            duration = 800
            interpolator = FastOutSlowInInterpolator()
        }

        fun setProgress5(progressBar: ContentLoadingProgressBar, from: Int , to: Int){
            this.progressBar5 = progressBar
            this.from5 = from
            this.to5 = to
        }

        fun setProgress4(progressBar: ContentLoadingProgressBar, from: Int , to: Int){
            this.progressBar4 = progressBar
            this.from4 = from
            this.to4 = to
        }

        fun setProgress3(progressBar: ContentLoadingProgressBar, from: Int , to: Int){
            this.progressBar3 = progressBar
            this.from3 = from
            this.to3 = to
        }

        fun setProgress2(progressBar: ContentLoadingProgressBar, from: Int , to: Int){
            this.progressBar2 = progressBar
            this.from2 = from
            this.to2 = to
        }

        fun setProgress1(progressBar: ContentLoadingProgressBar, from: Int , to: Int){
            this.progressBar1 = progressBar
            this.from1 = from
            this.to1 = to
        }

        override fun applyTransformation(interpolatedTime: Float, transformation: Transformation?) {
            super.applyTransformation(interpolatedTime, transformation)

            if (progressBar5 != null) {
                val value = from5 + (to5 - from5) * interpolatedTime
                progressBar5?.progress = value.toInt()
            }

            if (progressBar4 != null) {
                val value = from4 + (to4 - from4) * interpolatedTime
                progressBar4?.progress = value.toInt()
            }

            if (progressBar3 != null) {
                val value = from3 + (to3 - from3) * interpolatedTime
                progressBar3?.progress = value.toInt()
            }

            if (progressBar2 != null) {
                val value = from2 + (to2 - from2) * interpolatedTime
                progressBar2?.progress = value.toInt()
            }

            if (progressBar1 != null) {
                val value = from1 + (to1 - from1) * interpolatedTime
                progressBar1?.progress = value.toInt()
            }
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable("instanceState", super.onSaveInstanceState())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            super.onRestoreInstanceState(state.getParcelable("instanceState"))
            return
        }
        super.onRestoreInstanceState(state)
    }
}