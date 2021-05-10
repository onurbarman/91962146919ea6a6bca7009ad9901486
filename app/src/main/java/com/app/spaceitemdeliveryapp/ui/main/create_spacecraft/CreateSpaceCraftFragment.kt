package com.app.spaceitemdeliveryapp.ui.main.create_spacecraft

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.SeekBar
import androidx.fragment.app.viewModels
import com.app.spaceitemdeliveryapp.R
import com.app.spaceitemdeliveryapp.data.room.spacecraft.Spacecrafts
import com.app.spaceitemdeliveryapp.databinding.FragmentCreateSpaceCraftBinding
import com.app.spaceitemdeliveryapp.ui.main.MainActivity
import com.app.spaceitemdeliveryapp.ui.main.space_travel.SpaceTravelFragment
import com.app.spaceitemdeliveryapp.utils.Constants
import com.app.spaceitemdeliveryapp.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateSpaceCraftFragment : Fragment(R.layout.fragment_create_space_craft), SeekBar.OnSeekBarChangeListener {

    private lateinit var binding: FragmentCreateSpaceCraftBinding

    private val viewModel: CreateSpaceCraftViewModel by viewModels()
    private val listSeekBar : ArrayList<SeekBar> = arrayListOf()
    private val listSeekBarValues : ArrayList<Int> = arrayListOf()
    private var totalValue = 12

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreateSpaceCraftBinding.bind(view)

        initView()
        initClick()
        listenSpacecraftAddData()
        listenSpacecraftData()
    }

    private fun initView() {
        listSeekBar.run {
            add(binding.seekBarDurability)
            add(binding.seekBarSpeed)
            add(binding.seekBarCapacity)
        }

        listSeekBarValues.run{
            for (i in 0..2)
                add(0)
        }

        binding.seekBarDurability.setOnSeekBarChangeListener(this)
        binding.seekBarSpeed.setOnSeekBarChangeListener(this)
        binding.seekBarCapacity.setOnSeekBarChangeListener(this)
    }

    private fun initClick() {
        binding.btnContinue.setOnClickListener {
            if (binding.editSpaceCraftName.text.isNotEmpty()){
                val spacecraft = Spacecrafts(
                    name = binding.editSpaceCraftName.text.toString(),
                    durability = listSeekBarValues[0] + 1,
                    speed = listSeekBarValues[1] + 1,
                    capacity = listSeekBarValues[2] + 1,
                    spacesuitCount = (listSeekBarValues[2] + 1) * 10000,
                    universalSpaceTime = (listSeekBarValues[1] + 1) * 20,
                    durabilityTime = (listSeekBarValues[0] + 1) * 10000,
                    currentLocationId = 1,
                    currentLocation = "Dünya"
                )
                viewModel.addToSpacecrafts(spacecraft)
            }
            else{
                Utils.showToast(requireContext(),getString(R.string.warning_spacecraft_name))
            }
        }
    }

    private fun listenSpacecraftAddData(){
        viewModel.postDbResponse.observe(requireActivity(),{
            it?.let{ id ->
                viewModel.getSpacecraft(id)
            }

        })
    }

    private fun listenSpacecraftData(){
        viewModel.postSpacecraft.observe(requireActivity(),{
            it?.let { spacecraft ->
                Utils.setIsSpacecraftCreated(requireContext(),true)
                Utils.setSpacecraftId(requireContext(),spacecraft.id)
                Utils.showToast(requireContext(),getString(R.string.spacecarft_created_successfully))
                val spaceTravelFragment = SpaceTravelFragment()
                (activity as MainActivity).replaceFragment(spaceTravelFragment,"SpaceTravelFragment")
            }
        })
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        val which = whichIsIt(seekBar!!)
        val storedProgress = listSeekBarValues[which]

        if (progress > storedProgress) {
            val remaining = remaining()
            if (remaining == 0) {
                seekBar.progress = storedProgress
                return
            } else {
                if (storedProgress + remaining >= progress) {
                    listSeekBarValues[which] = progress

                } else {
                    listSeekBarValues[which] = storedProgress + remaining
                }
                remaining()
            }
        } else if (progress <= storedProgress) {
            listSeekBarValues[which] = progress
        }
        when (which) {
            0 -> binding.textDurability.text = (listSeekBarValues[0] + 1).toString()
            1 -> binding.textSpeed.text = (listSeekBarValues[1] + 1).toString()
            2 -> binding.textCapacity.text = (listSeekBarValues[2] + 1).toString()
        }
        var total = 15
        for (i in 0..2) {
            total -= listSeekBarValues[i]+1
        }
        binding.textTotalPoint.text=(total).toString()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    private fun remaining() : Int {
        var remaining = totalValue
        for (i in 0..2) {
            remaining -= listSeekBarValues[i]
        }
        if (remaining >= 14) {
            remaining = 14
        } else if (remaining <= 0) {
            remaining = 0
        }
        return remaining
    }

    private fun whichIsIt(seekBar: SeekBar) : Int {
        return when(seekBar){
            binding.seekBarDurability -> 0
            binding.seekBarSpeed -> 1
            binding.seekBarCapacity-> 2
            else -> 0
        }
    }
}