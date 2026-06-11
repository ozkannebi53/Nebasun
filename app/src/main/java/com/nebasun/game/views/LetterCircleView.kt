package com.nebasun.game.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.*

/**
 * LetterCircleView
 * - Harfleri daire üzerinde gösterir
 * - Parmak sürükleme ile harf seçimi
 * - Aynı harfe iki kez gitmeye izin verir (örn: ALFA -> A-L-F-A)
 * - Seçilen harfler arasında renkli şerit çizer
 */
class LetterCircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Callback for word formed
    var onWordFormed: ((String) -> Unit)? = null
    var onLetterSelected: ((String) -> Unit)? = null

    private var letters: List<String> = emptyList()
    private val selectedIndices = mutableListOf<Int>()
    private var currentTouchX = 0f
    private var currentTouchY = 0f
    private var isTouching = false

    private val letterPositions = mutableListOf<PointF>()

    // Paint objects
    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#E0E0E0")
        style = Paint.Style.FILL
    }

    private val circleStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#BDBDBD")
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }

    private val selectedCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FF5722")
        style = Paint.Style.FILL
    }

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FF5722")
        style = Paint.Style.STROKE
        strokeWidth = 12f
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        alpha = 180
    }

    private val currentLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FF9800")
        style = Paint.Style.STROKE
        strokeWidth = 10f
        strokeCap = Paint.Cap.ROUND
        alpha = 150
    }

    private val letterPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 52f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    private val selectedLetterPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 52f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    private val outerCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#F5F5F5")
        style = Paint.Style.FILL
    }

    private val outerCircleStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#E0E0E0")
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    fun setLetters(newLetters: List<String>) {
        letters = newLetters
        selectedIndices.clear()
        calculatePositions()
        invalidate()
    }

    fun clearSelection() {
        selectedIndices.clear()
        isTouching = false
        invalidate()
    }

    private fun calculatePositions() {
        letterPositions.clear()
        if (letters.isEmpty()) return

        val cx = width / 2f
        val cy = height / 2f
        val radius = (min(width, height) / 2f) * 0.65f
        val angleStep = 360f / letters.size

        for (i in letters.indices) {
            val angle = Math.toRadians((angleStep * i - 90).toDouble())
            val x = cx + radius * cos(angle).toFloat()
            val y = cy + radius * sin(angle).toFloat()
            letterPositions.add(PointF(x, y))
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculatePositions()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (letters.isEmpty()) return

        val cx = width / 2f
        val cy = height / 2f
        val outerRadius = (min(width, height) / 2f) * 0.85f

        // Draw outer circle background
        canvas.drawCircle(cx, cy, outerRadius, outerCirclePaint)
        canvas.drawCircle(cx, cy, outerRadius, outerCircleStrokePaint)

        // Draw lines between selected letters
        if (selectedIndices.size > 1) {
            for (i in 0 until selectedIndices.size - 1) {
                val from = letterPositions[selectedIndices[i]]
                val to = letterPositions[selectedIndices[i + 1]]
                canvas.drawLine(from.x, from.y, to.x, to.y, linePaint)
            }
        }

        // Draw line from last selected to current touch
        if (isTouching && selectedIndices.isNotEmpty()) {
            val last = letterPositions[selectedIndices.last()]
            canvas.drawLine(last.x, last.y, currentTouchX, currentTouchY, currentLinePaint)
        }

        // Draw letter circles
        val letterRadius = (min(width, height) / 2f) * 0.13f
        for (i in letters.indices) {
            val pos = letterPositions[i]
            val isSelected = selectedIndices.contains(i)

            if (isSelected) {
                // Selected: bright orange
                canvas.drawCircle(pos.x, pos.y, letterRadius, selectedCirclePaint)
                // Draw order number badge
                val orderIndex = selectedIndices.indexOf(i) + 1
                val badgePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = Color.parseColor("#FFEB3B")
                    style = Paint.Style.FILL
                }
                canvas.drawCircle(pos.x + letterRadius * 0.7f, pos.y - letterRadius * 0.7f, letterRadius * 0.35f, badgePaint)
            } else {
                canvas.drawCircle(pos.x, pos.y, letterRadius, circlePaint)
                canvas.drawCircle(pos.x, pos.y, letterRadius, circleStrokePaint)
            }

            // Draw letter text
            val paint = if (isSelected) selectedLetterPaint else letterPaint.apply { color = Color.parseColor("#333333") }
            val textY = pos.y - (paint.descent() + paint.ascent()) / 2
            canvas.drawText(letters[i], pos.x, textY, if (isSelected) selectedLetterPaint else Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = if (isSelected) Color.WHITE else Color.parseColor("#333333")
                textSize = 52f
                textAlign = Paint.Align.CENTER
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            })
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                selectedIndices.clear()
                isTouching = true
                handleTouch(event.x, event.y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                currentTouchX = event.x
                currentTouchY = event.y
                handleTouch(event.x, event.y)
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isTouching = false
                if (selectedIndices.isNotEmpty()) {
                    val word = selectedIndices.joinToString("") { letters[it] }
                    onWordFormed?.invoke(word)
                }
                selectedIndices.clear()
                invalidate()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun handleTouch(x: Float, y: Float) {
        val letterRadius = (min(width, height) / 2f) * 0.13f
        for (i in letters.indices) {
            val pos = letterPositions[i]
            val dist = sqrt((x - pos.x).pow(2) + (y - pos.y).pow(2))
            if (dist <= letterRadius * 1.3f) {
                // Allow same letter index multiple times (e.g., ALFA: A->L->F->A)
                // But prevent going back to immediately previous letter
                val canAdd = selectedIndices.isEmpty() ||
                        (selectedIndices.last() != i || selectedIndices.size > 1)

                if (canAdd && (selectedIndices.isEmpty() || selectedIndices.last() != i)) {
                    selectedIndices.add(i)
                    onLetterSelected?.invoke(letters[i])
                    invalidate()
                }
                break
            }
        }
    }
}
