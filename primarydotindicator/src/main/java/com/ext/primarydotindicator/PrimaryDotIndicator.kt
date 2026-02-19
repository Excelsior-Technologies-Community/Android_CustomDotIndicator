package com.ext.primarydotindicator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.viewpager2.widget.ViewPager2

class PrimaryDotIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    // Dot properties
    private var dotCount = 5
    private var selectedIndex = 0

    private var dotRadius = 0f
    private var dotSpacing = 0f

    private var attachedViewPager: ViewPager2? = null

    // Paints
    private val activePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val inactivePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {

        // Default values from resources
        dotRadius = resources.getDimension(R.dimen.dot_radius)
        dotSpacing = resources.getDimension(R.dimen.dot_spacing)

        activePaint.color = resources.getColor(R.color.dot_active, null)
        inactivePaint.color = resources.getColor(R.color.dot_inactive, null)

        // Read custom XML attributes
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.PrimaryDotIndicator)

        dotCount = typedArray.getInt(
            R.styleable.PrimaryDotIndicator_dotCount,
            dotCount
        )

        selectedIndex = typedArray.getInt(
            R.styleable.PrimaryDotIndicator_selectedIndex,
            selectedIndex
        )

        activePaint.color = typedArray.getColor(
            R.styleable.PrimaryDotIndicator_activeColor,
            activePaint.color
        )

        inactivePaint.color = typedArray.getColor(
            R.styleable.PrimaryDotIndicator_inactiveColor,
            inactivePaint.color
        )

        dotRadius = typedArray.getDimension(
            R.styleable.PrimaryDotIndicator_dotRadius,
            dotRadius
        )

        dotSpacing = typedArray.getDimension(
            R.styleable.PrimaryDotIndicator_dotSpacing,
            dotSpacing
        )

        isClickable = true
        isFocusable = true

        typedArray.recycle()
    }

    // Important for wrap_content support
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val dotDiameter = dotRadius * 2

        val desiredWidth =
            (dotCount * dotDiameter + (dotCount - 1) * dotSpacing
                    + paddingLeft + paddingRight).toInt()

        val desiredHeight =
            (dotDiameter + paddingTop + paddingBottom).toInt()

        setMeasuredDimension(desiredWidth, desiredHeight)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val dotDiameter = dotRadius * 2

        // Total width occupied by all dots + spacing
        val totalWidth =
            (dotCount * dotDiameter) +
                    ((dotCount - 1) * dotSpacing)

        // Start drawing from center
        val startX = (width - totalWidth) / 2f + dotRadius

        val centerY = height / 2f

        for (i in 0 until dotCount) {

            val paint = if (i == selectedIndex)
                activePaint
            else
                inactivePaint

            val x = startX + i * (dotDiameter + dotSpacing)

            canvas.drawCircle(x, centerY, dotRadius, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        if (event.action == MotionEvent.ACTION_DOWN) {

            val clickedIndex = getClickedDotIndex(event.x)

            if (clickedIndex != -1) {

                // Update indicator
                setSelectedIndex(clickedIndex)

                // Jump ViewPager2
                attachedViewPager?.setCurrentItem(clickedIndex, true)
            }
        }

        return true
    }



    // Public API

    fun setDotCount(count: Int) {
        dotCount = count
        requestLayout()
        invalidate()
    }

    fun setSelectedIndex(index: Int) {
        selectedIndex = index
        invalidate()
    }
    fun attachToViewPager(viewPager: ViewPager2) {

        attachedViewPager = viewPager

        val itemCount = viewPager.adapter?.itemCount ?: 0
        setDotCount(itemCount)

        viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    setSelectedIndex(position)
                }
            }
        )
    }

    private fun getClickedDotIndex(touchX: Float): Int {

        val dotDiameter = dotRadius * 2

        val totalWidth =
            (dotCount * dotDiameter) +
                    ((dotCount - 1) * dotSpacing)

        val startX = (width - totalWidth) / 2f

        for (i in 0 until dotCount) {

            val dotStart = startX + i * (dotDiameter + dotSpacing)
            val dotEnd = dotStart + dotDiameter

            if (touchX in dotStart..dotEnd) {
                return i
            }
        }

        return -1
    }

}
