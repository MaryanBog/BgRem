package com.bgrem.presentation.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.withStyledAttributes
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bgrem.app.R
import com.bgrem.app.databinding.ViewErrorBinding
import com.bgrem.domain.common.failure.NoInternetFailure

class ErrorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewErrorBinding by viewBinding()
    private var onErrorActionButtonClickListener: OnErrorActionButtonClickListener? = null

    var viewState: ErrorViewState = ErrorViewState.NO_INTERNET
        set(value) {
            field = value
            applyState(value)
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_error, this, true)
        initView()
        initAttrs(attrs, defStyleAttr)
    }

    fun setOnErrorActionButtonClickListener(listener: OnErrorActionButtonClickListener) {
        onErrorActionButtonClickListener = listener
    }

    fun handleError(error: Throwable) {
        viewState = when (error) {
            is NoInternetFailure -> ErrorViewState.NO_INTERNET
            else -> ErrorViewState.NOT_ABLE_REMOVE_BG
        }
    }

    private fun initView() = with(binding) {
        gravity = Gravity.CENTER
        orientation = VERTICAL
        errorButton.setOnClickListener { onErrorActionButtonClickListener?.onErrorActionClick() }
    }

    private fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int = 0, defStyle: Int = 0) =
        context.withStyledAttributes(
            attrs,
            R.styleable.ErrorView,
            defStyleAttr,
            defStyle
        ) {
            viewState = getInt(R.styleable.ErrorView_ev_state, 0).let {
                ErrorViewState.values()[it]
            }
        }

    private fun applyState(state: ErrorViewState) = with(binding) {
        val data = determineViewState(state)

        errorImage.setImageResource(data.errorImageRes)
        errorText.setText(data.errorTextRes)
    }

    private fun determineViewState(state: ErrorViewState) = when (state) {
        ErrorViewState.NO_INTERNET -> ViewData(
            errorTextRes = R.string.common_error_no_internet,
            errorImageRes = R.drawable.ic_error_no_internet
        )
        ErrorViewState.NOT_ABLE_REMOVE_BG -> ViewData(
            errorTextRes = R.string.common_error_not_able_remove_bg,
            errorImageRes = R.drawable.ic_error_not_able_remove_bg
        )
    }

    enum class ErrorViewState {
        NO_INTERNET, NOT_ABLE_REMOVE_BG
    }

    fun interface OnErrorActionButtonClickListener {
        fun onErrorActionClick()
    }

    private data class ViewData(
        @StringRes
        val errorTextRes: Int,
        @DrawableRes
        val errorImageRes: Int
    )
}