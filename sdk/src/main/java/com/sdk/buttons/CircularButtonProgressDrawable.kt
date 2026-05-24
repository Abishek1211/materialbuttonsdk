package com.sdk.buttons

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.RectF
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.view.animation.LinearInterpolator

/**
 * A lightweight animated circular progress [Drawable] used by [LoadingButton].
 *
 * Draws a spinning arc with a configurable colour and stroke width.
 * Implements [Animatable] — call [start] / [stop] to control the animation.
 */
class CircularButtonProgressDrawable(
    color: Int = android.graphics.Color.WHITE
) : Drawable(), Animatable {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style      = Paint.Style.STROKE
        strokeWidth = 3.5f
        this.color = color
        strokeCap  = Paint.Cap.ROUND
    }

    private var startAngle = 0f

    private val animator = ValueAnimator.ofFloat(0f, 360f).apply {
        duration      = 900L
        repeatCount   = ValueAnimator.INFINITE
        interpolator  = LinearInterpolator()
        addUpdateListener {
            startAngle = it.animatedValue as Float
            invalidateSelf()
        }
    }

    /** Change the spinner colour at any time. */
    fun setColor(color: Int) {
        paint.color = color
        invalidateSelf()
    }

    override fun draw(canvas: Canvas) {
        val padding = paint.strokeWidth
        val oval = RectF(
            bounds.left   + padding,
            bounds.top    + padding,
            bounds.right  - padding,
            bounds.bottom - padding
        )
        canvas.drawArc(oval, startAngle, 270f, false, paint)
    }

    override fun setAlpha(alpha: Int)              { paint.alpha = alpha; invalidateSelf() }
    override fun setColorFilter(cf: ColorFilter?)  { paint.colorFilter = cf; invalidateSelf() }
    override fun getOpacity()                      = PixelFormat.TRANSLUCENT
    override fun getIntrinsicWidth()               = 48
    override fun getIntrinsicHeight()              = 48
    override fun start()                           { animator.start() }
    override fun stop()                            { animator.cancel(); invalidateSelf() }
    override fun isRunning()                       = animator.isRunning
}

