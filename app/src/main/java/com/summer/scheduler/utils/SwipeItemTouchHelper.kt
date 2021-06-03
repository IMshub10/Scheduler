package com.summer.scheduler.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.summer.scheduler.R
import com.summer.scheduler.data.model.entity.ReminderEntity
import com.summer.scheduler.data.model.entity.ToDoEntity
import com.summer.scheduler.ui.main.adapter.ReminderListAdapter
import com.summer.scheduler.ui.main.adapter.ToDoListAdapter
import com.summer.scheduler.ui.main.viewmodel.MainViewModel
import com.summer.scheduler.ui.main.viewmodel.ViewModelFactory
import com.summer.scheduler.utils.AndroidUtils.dp
import com.summer.scheduler.utils.listeners.Swipe
import kotlinx.coroutines.launch
import kotlin.math.abs

class SwipeItemTouchHelper(private val context: Context, dragDirs: Int, private val swipeDirs: Int, private val swipe: Swipe) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
    private var dX = 0f
    private var view: View? = null
    private var currentItemViewHolder: RecyclerView.ViewHolder? = null
    private var replyButtonProgress: Float = 0.toFloat()
    private var lastReplyButtonAnimationTime: Long = 0
    private var swipeBack = false
    private var isVibrate = false
    private var startTracking = false

    private lateinit var shareRound: Drawable


    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        view = viewHolder.itemView
        shareRound = if (swipeDirs == ItemTouchHelper.RIGHT) {
            ContextCompat.getDrawable(context, R.drawable.ic_delete)!!
        }else{
            ContextCompat.getDrawable(context,R.drawable.ic_delete)!!
        }
        return ItemTouchHelper.Callback.makeMovementFlags(0, swipeDirs)
    }


    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = false
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            setTouchListener(recyclerView, viewHolder)
        }
        if (view!!.translationX < convertToDp(20) || dX < this.dX) {
            super.onChildDraw(c, recyclerView, viewHolder, dX/3, dY, actionState, isCurrentlyActive)
            this.dX = dX
            startTracking = true
        }
        currentItemViewHolder = viewHolder
        if (swipeDirs == ItemTouchHelper.RIGHT) {
            drawReplyButtonRight(c)
        }
        if (swipeDirs == ItemTouchHelper.LEFT) {
            drawReplyButtonLeft(c)
        }
    }

    private fun drawReplyButtonRight(canvas: Canvas) {
        if (currentItemViewHolder == null) {
            return
        }
        val translationX = view!!.translationX
        val newTime = System.currentTimeMillis()
        val dt = minOf(17, newTime - lastReplyButtonAnimationTime)
        lastReplyButtonAnimationTime = newTime
        val showing = translationX >= convertToDp(30)
        if (showing) {
            if (replyButtonProgress < 1.0f) {
                replyButtonProgress += dt / 180.0f
                if (replyButtonProgress > 1.0f) {
                    replyButtonProgress = 1.0f
                } else {
                    view!!.invalidate()
                }
            }
        } else if (translationX <= 0.0f) {
            replyButtonProgress = 0f
            startTracking = false
            isVibrate = false
        } else {
            if (replyButtonProgress > 0.0f) {
                replyButtonProgress -= dt / 180.0f
                if (replyButtonProgress < 0.1f) {
                    replyButtonProgress = 0f
                } else {
                    view!!.invalidate()
                }
            }
        }
        val alpha: Int
        val scale: Float
        if (showing) {
            scale = if (replyButtonProgress <= 0.8f) {
                1.2f * (replyButtonProgress / 0.8f)
            } else {
                1.2f - 0.2f * ((replyButtonProgress - 0.8f) / 0.2f)
            }
            alpha = minOf(255f, 255 * (replyButtonProgress / 0.8f)).toInt()
        } else {
            scale = replyButtonProgress
            alpha = minOf(255f, 255 * replyButtonProgress).toInt()
        }
        shareRound.alpha = alpha

        if (startTracking) {
            if (!isVibrate && view!!.translationX >= convertToDp(100)) {
                view!!.performHapticFeedback(
                        HapticFeedbackConstants.KEYBOARD_TAP,
                        HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                )
                isVibrate = true
            }
        }

        val x: Int = if (view!!.translationX > convertToDp(130)) {
            convertToDp(130) / 2
        } else {
            (view!!.translationX / 2).toInt()
        }

        val y = (view!!.top + view!!.measuredHeight / 2).toFloat()
        shareRound.colorFilter =
                PorterDuffColorFilter(ContextCompat.getColor(context, R.color.grey_darker_3a3a3c), PorterDuff.Mode.MULTIPLY)

        shareRound.setBounds(
                (x - convertToDp(18) * scale).toInt(),
                (y - convertToDp(18) * scale).toInt(),
                (x + convertToDp(18) * scale).toInt(),
                (y + convertToDp(18) * scale).toInt()
        )
        shareRound.draw(canvas)

        shareRound.alpha = 255
    }

    private fun drawReplyButtonLeft(canvas: Canvas) {
        if (currentItemViewHolder == null) {
            return
        }
        val translationX = view!!.translationX / -1
        val newTime = System.currentTimeMillis()
        val dt = minOf(17, newTime - lastReplyButtonAnimationTime)
        lastReplyButtonAnimationTime = newTime
        val showing = translationX >= convertToDp(30)
        if (showing) {
            if (replyButtonProgress < 1.0f) {
                replyButtonProgress += dt / 180.0f
                if (replyButtonProgress > 1.0f) {
                    replyButtonProgress = 1.0f
                } else {
                    view!!.invalidate()
                }
            }
        } else if (translationX <= 0.0f) {
            replyButtonProgress = 0f
            startTracking = false
            isVibrate = false
        } else {
            if (replyButtonProgress > 0.0f) {
                replyButtonProgress -= dt / 180.0f
                if (replyButtonProgress < 0.1f) {
                    replyButtonProgress = 0f
                } else {
                    view!!.invalidate()
                }
            }
        }
        val alpha: Int
        val scale: Float
        if (showing) {
            scale = if (replyButtonProgress <= 0.8f) {
                1.2f * (replyButtonProgress / 0.8f)
            } else {
                1.2f - 0.2f * ((replyButtonProgress - 0.8f) / 0.2f)
            }
            alpha = minOf(255f, 255 * (replyButtonProgress / 0.8f)).toInt()
        } else {
            scale = replyButtonProgress
            alpha = minOf(255f, 255 * replyButtonProgress).toInt()
        }
        shareRound.alpha = alpha

        if (startTracking) {
            if (!isVibrate && (view!!.translationX / -1) >= convertToDp(100)) {
                view!!.performHapticFeedback(
                        HapticFeedbackConstants.KEYBOARD_TAP,
                        HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                )
                isVibrate = true
            }
        }
        /*
        val x: Int = if (view!!.translationX > convertTodp(130)) {
            convertTodp(130) / 2
        } else {
            (view!!.translationX / 2).toInt()
        }
         */


        val x: Int = if ((view!!.translationX / -1) > convertToDp(130)) {
            view!!.width - convertToDp(65)
        } else {
            view!!.width - (view!!.translationX / -2).toInt()
        }

        val y = (view!!.top + view!!.measuredHeight / 2).toFloat()
        shareRound.colorFilter =
                PorterDuffColorFilter(ContextCompat.getColor(context, R.color.grey_darker_3a3a3c), PorterDuff.Mode.MULTIPLY)

        shareRound.setBounds(
                (x - convertToDp(18) * scale).toInt(),
                (y - convertToDp(18) * scale).toInt(),
                (x + convertToDp(18) * scale).toInt(),
                (y + convertToDp(18) * scale).toInt()
        )

        shareRound.draw(canvas)

        shareRound.alpha = 255
    }

    private lateinit var mainViewModel: MainViewModel

    init {
        mainViewModel = ViewModelProvider(
            context as ViewModelStoreOwner,
            ViewModelFactory(context.applicationContext as Application)
        )
            .get(MainViewModel::class.java)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        recyclerView.setOnTouchListener { _, event ->
            swipeBack = event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP
            if (swipeBack) {
                if (abs(view!!.translationX) >= this.convertToDp(100)) {
                    swipe.rightSwipeDelete(viewHolder.adapterPosition, recyclerView.id)
                    if (recyclerView.id == R.id.to_do_recyclerView) {
                        val toDo: ToDoEntity = (recyclerView.adapter as ToDoListAdapter).currentList[viewHolder.absoluteAdapterPosition]
                        showDeleteToDoDialog(toDo)
                    } else {
                        val reminder: ReminderEntity = (recyclerView.adapter as ReminderListAdapter).currentList[viewHolder.absoluteAdapterPosition]
                        showDeleteReminderDialog(reminder)
                    }
                }
            }
            false
        }
    }

    private fun convertToDp(translationX: Int): Int {
        return dp(translationX.toFloat(), context)
    }

    private fun showDeleteReminderDialog(reminder: ReminderEntity) {
        val dialog = SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Are you sure?")
            .setContentText("This Reminder item will be permanently deleted")
            .setConfirmText("Yes")
            .setCancelText("No")
            .setConfirmClickListener { sweetAlertDialog ->
                sweetAlertDialog.setTitleText("Deleted!")
                    .setContentText("Your Reminder item is successfully deleted!")
                    .setConfirmText("OK")
                    .setConfirmClickListener(null)
                    //.dismissWithAnimation()
                    //.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)

                mainViewModel.viewModelScope.launch {
                    mainViewModel.removeReminder(reminder)
                }
            }
            .setCancelClickListener {
                it.cancel()
            }

        dialog.show()
    }

    private fun showDeleteToDoDialog(toDo: ToDoEntity) {
        val dialog = SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Are you sure?")
            .setContentText("This To Do item will be permanently deleted")
            .setConfirmText("Yes")
            .setCancelText("No")
            .setConfirmClickListener { sweetAlertDialog ->
                sweetAlertDialog.setTitleText("Deleted!")
                    .showCancelButton(false)
                    .setContentText("Your To Do item is successfully deleted!")
                    .setConfirmText("OK")
                    .setConfirmClickListener(null)
                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE)

                mainViewModel.viewModelScope.launch {
                    mainViewModel.removeToDo(toDo)
                }
            }
            .setCancelClickListener {
                it.cancel()
            }

        dialog.show()
    }

}