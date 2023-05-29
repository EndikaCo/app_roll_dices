package com.endcodev.roll_dices.presenter.dice

import android.content.res.Configuration
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.endcodev.roll_dices.R
import com.endcodev.roll_dices.databinding.FragmentDicesBinding
import com.google.android.gms.ads.AdRequest
import kotlin.random.Random

class DicesFragment : Fragment(R.layout.fragment_dices) {

    private var _binding: FragmentDicesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DicesFragmentViewModel by viewModels()
    private val dices: ArrayList<ImageView> = ArrayList()
    private var rolling = false
    private var maxDices = 5
    private var sides = 6

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentDicesBinding.inflate(inflater, container, false)
            .apply { _binding = this }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addNewDice()

        screenSize()

        initAdmob()

        initListeners()
    }

    private fun initListeners() {
        binding.linearDices.setOnClickListener {
            rollAllDices()
        }

        binding.viewButtons.addBt.setOnClickListener {
            addNewDice()
        }

        binding.viewButtons.removeBt.setOnClickListener {
            removeLastDice()
        }
    }

    private fun rollAllDices() {
        rolling = true
        for (i in dices.indices) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.dice_move)
            val randomTimer: Long = (50..500).random(Random(System.currentTimeMillis())).toLong()
            animation.startOffset = randomTimer
            dices[i].startAnimation(animation)
            rollDices(sides + 1, dices)
        }
    }

    private fun screenSize() {
        val screenSize = resources.configuration.screenLayout and
                Configuration.SCREENLAYOUT_SIZE_MASK

        maxDices = when (screenSize) {
            Configuration.SCREENLAYOUT_SIZE_LARGE -> 7
            Configuration.SCREENLAYOUT_SIZE_NORMAL -> 6
            Configuration.SCREENLAYOUT_SIZE_SMALL -> 5
            else -> 5
        }
    }

    private fun addNewDice() {
        if (dices.size < maxDices) { // max dices in View
            dices.add(ImageView(context))
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            if (resources.configuration.orientation == 1) // Land
                layoutParams.setMargins(0, 0, 0, 30)
            else
                layoutParams.setMargins(0, 0, 30, 0)

            dices[dices.lastIndex].setBackgroundResource(R.drawable.dice_1)
            binding.linearDices.addView(dices[dices.lastIndex], layoutParams)
        }
    }

    private fun removeLastDice() {
        if (dices.size > 1 && !rolling) {
            binding.linearDices.removeView(dices[dices.lastIndex])
            dices.removeAt(dices.lastIndex)
        }
    }

    private fun rollDices(sides: Int, ivs: ArrayList<ImageView>) {

        for ((index, item) in ivs.withIndex()) {
            println("The element at $index is $item")
            item.setBackgroundResource(R.drawable.dice_spread)
            val diceAnimation = item.background
            if (diceAnimation is Animatable) {
                diceAnimation.start()
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        diceAnimation.stop() // This method will be executed once the timer is over
                        viewModel.rollDices(sides, ivs.size)
                        try {
                            when (viewModel.diceFace.value?.get(index)) {
                                1 -> item.setBackgroundResource(R.drawable.dice_1)
                                2 -> item.setBackgroundResource(R.drawable.dice_2)
                                3 -> item.setBackgroundResource(R.drawable.dice_3)
                                4 -> item.setBackgroundResource(R.drawable.dice_4)
                                5 -> item.setBackgroundResource(R.drawable.dice_5)
                                6 -> item.setBackgroundResource(R.drawable.dice_6)
                            }
                        } catch (_: Exception) {
                        }

                        if (index == ivs.lastIndex)
                            rolling = false
                    },
                    /** the delay should be the total of [R.drawable.dice_spread] duration + 50ms .*/
                    450
                )
            }
        }
    }

    private fun initAdmob() {
        val adRequest: AdRequest = AdRequest.Builder().build()
        binding.homeBanner.loadAd(adRequest)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}