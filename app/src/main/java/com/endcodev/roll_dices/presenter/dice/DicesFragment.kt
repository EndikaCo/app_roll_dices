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

    /**
     * Initialize views by setting up the diceList, adding dice views
     */
    private fun initViews() {

        val diceList = viewModel.diceList.value
        if (diceList != null) {
            for (item in diceList) {
                addDice(item)
            }
        }
    }

    /**
     * Empty function (placeholders for future implementations if required).
     */
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

    /**
     * call ViewModel's rollDices method
     *
     * set rolling to true to avoid actions while is rolling,
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun rollAllDices() {
        rolling = true
        viewModel.rollDices(sides, dices.size)
        rollDices(dices)
    }

    /**
     * Get the maximum number of dices that can be displayed on the screen based on the screen size.
     */
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

    /**
     * Add a dice with the given number to display the dice view.
     *
     * @param num The number to add as a new dice.
     */
    private fun addDice(num: Int) {
        if (dices.size < getMaxDices()) { // max dices in View
            dices.add(ImageView(context))
            setDiceBackground(num, dices[dices.lastIndex])
            binding.linearDices.addView(dices[dices.lastIndex], getLayoutParams())
        }
    }

    /**
     * Add a new dice to the dice list and display the dice view.
     */
    private fun addNewDice() {
        if (dices.size < getMaxDices()) { // max dices in View
            dices.add(ImageView(context))
            viewModel.addDice(1)
            setDiceBackground(1, dices[dices.lastIndex])
            binding.linearDices.addView(dices[dices.lastIndex], getLayoutParams())
        }
    }

    /**
     * Get the layout params for the dice views based on the screen orientation.
     */
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

    /**
     * Remove the last dice from the view and the ViewModel's diceList.
     */
    private fun removeLastDice() {
        if (dices.size > 1 && !rolling) {
            binding.linearDices.removeView(dices[dices.lastIndex])
            viewModel.removeDice()
            dices.removeAt(dices.lastIndex)
        }
    }

    /**
     * Roll the dice views with animations based on the dice values from the ViewModel's diceList.
     *
     * @param ivs The list of dice views to roll.
     */
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
                            adapter.swapData()

                        } catch (e: Exception) {
                            Log.e(TAG, "Exception: $e")
                        }
                        if (index == ivs.lastIndex)
                            rolling = false
                    },
                    /** the delay should be the total of [R.drawable.dice_spread] duration + 50ms .*/
                    750
                )
            }
        }
    }

    /**
     * Set the background of the dice view based on the provided number.
     *
     * @param num The number representing the dice value.
     * @param item The ImageView representing the dice view.
     */
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

    /**
     * Initialize AdMob by loading an ad into the banner view.
     */
    private fun initAdmob() {
        val adRequest: AdRequest = AdRequest.Builder().build()
        binding.homeBanner.loadAd(adRequest)
    }

    /**
     * Initialize the RecyclerView adapter and set up the layout manager for the dice list.
     *
     * @param list The initial list of dice values to be displayed in the RecyclerView.
     */
    private fun initAdapter(list: MutableList<Int>) {
        adapter = DicesAdapter(list)
        val layoutManager = LinearLayoutManager(context)
        binding.list.layoutManager = layoutManager
        binding.list.adapter = adapter
    }

    /**
     * Clean up the ViewBinding instance to avoid memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}