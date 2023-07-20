package com.endcodev.roll_dices.presenter.dice

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.endcodev.roll_dices.R
import com.endcodev.roll_dices.databinding.FragmentDicesBinding
import com.google.android.gms.ads.AdRequest

class DicesFragment : Fragment(R.layout.fragment_dices) {

    companion object {
        const val TAG = "DicesFragment ***"
    }

    private var _binding: FragmentDicesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DicesFragmentViewModel by viewModels()
    private lateinit var adapter: DicesAdapter


    private val dices: ArrayList<ImageView> = ArrayList()
    private var rolling = false
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

        initAdapter(viewModel.sumList.value!!)

        initViews()

        initAdmob()

        initListeners()

        initObservers()
    }

    private fun initViews() {

        val diceList = viewModel.diceList.value
        if (diceList != null) {
            for (item in diceList) {
                addDice(item)
            }
        }
    }

    private fun initObservers() {}

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

    @SuppressLint("NotifyDataSetChanged")
    private fun rollAllDices() {
        rolling = true
        viewModel.rollDices(sides, dices.size)
        //adapter.notifyDataSetChanged()
        adapter.swapData()
        //adapter.notifyItemInserted(viewModel.sumList.value!!.lastIndex)
        rollDices(dices)
    }

    private fun getMaxDices(): Int {
        val screenSize = resources.configuration.screenLayout and
                Configuration.SCREENLAYOUT_SIZE_MASK

        val maxDices = when (screenSize) {
            Configuration.SCREENLAYOUT_SIZE_LARGE -> 7
            Configuration.SCREENLAYOUT_SIZE_NORMAL -> 6
            Configuration.SCREENLAYOUT_SIZE_SMALL -> 5
            else -> 5
        }
        return maxDices
    }


    private fun addDice(num: Int) {
        if (dices.size < getMaxDices()) { // max dices in View
            dices.add(ImageView(context))
            setDiceBackground(num, dices[dices.lastIndex])
            binding.linearDices.addView(dices[dices.lastIndex], getLayoutParams())
        }
    }

    private fun addNewDice() {
        if (dices.size < getMaxDices()) { // max dices in View
            dices.add(ImageView(context))
            viewModel.addDice(1)
            setDiceBackground(1, dices[dices.lastIndex])
            binding.linearDices.addView(dices[dices.lastIndex], getLayoutParams())
        }
    }

    private fun getLayoutParams(): LinearLayout.LayoutParams {
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        if (resources.configuration.orientation == 1) // Land
            layoutParams.setMargins(0, 0, 0, 30)
        else
            layoutParams.setMargins(0, 0, 30, 0)
        return layoutParams
    }

    private fun removeLastDice() {
        if (dices.size > 1 && !rolling) {
            binding.linearDices.removeView(dices[dices.lastIndex])
            viewModel.removeDice()
            dices.removeAt(dices.lastIndex)
        }
    }

    private fun rollDices(ivs: ArrayList<ImageView>) {

        for ((index, item) in ivs.withIndex()) {

            val dice = viewModel.diceList.value!![index]
            Log.v(TAG, "index:$index dice:${dice} \n")
            if (dice == 1 || dice == 3 || dice == 6) {

                item.setBackgroundResource(R.drawable.dice_spread)
            }else
                item.setBackgroundResource(R.drawable.dice_spread_2)

            val diceAnimation = item.background
            if (diceAnimation is Animatable) {
                diceAnimation.start()
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        diceAnimation.stop() // This method will be executed once the timer is over
                        try {
                            setDiceBackground(viewModel.diceList.value?.get(index)!!, item)
                        } catch (e: Exception) {
                            Log.e(TAG, "Exception: $e")
                        }
                        if (index == ivs.lastIndex)
                            rolling = false
                    },
                    /** the delay should be the total of [R.drawable.dice_spread] duration + 50ms .*/
                    650
                )
            }
        }
    }

    private fun setDiceBackground(num: Int, item: ImageView) {
        when (num) {
            1 -> item.setBackgroundResource(R.drawable.dice_1)
            2 -> item.setBackgroundResource(R.drawable.dice_2)
            3 -> item.setBackgroundResource(R.drawable.dice_3)
            4 -> item.setBackgroundResource(R.drawable.dice_4)
            5 -> item.setBackgroundResource(R.drawable.dice_5)
            6 -> item.setBackgroundResource(R.drawable.dice_6)
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

    private fun initAdapter(list: MutableList<Int>) {
        adapter = DicesAdapter(list)

        val layoutManager = LinearLayoutManager(context)

        //binding.list.smoothScrollToPosition(adapter.itemCount-1)

        binding.list.layoutManager = layoutManager
        binding.list.adapter = adapter
    }
}