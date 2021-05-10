package com.app.spaceitemdeliveryapp.ui.main.space_travel.ui.planets

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.app.spaceitemdeliveryapp.R
import com.app.spaceitemdeliveryapp.data.room.planets.Planets
import com.app.spaceitemdeliveryapp.data.room.spacecraft.Spacecrafts
import com.app.spaceitemdeliveryapp.databinding.FragmentPlanetsBinding
import com.app.spaceitemdeliveryapp.ui.main.MainActivity
import com.app.spaceitemdeliveryapp.ui.main.create_spacecraft.CreateSpaceCraftFragment
import com.app.spaceitemdeliveryapp.ui.main.space_travel.SpaceTravelFragment
import com.app.spaceitemdeliveryapp.utils.TravelInterface
import com.app.spaceitemdeliveryapp.utils.Utils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PlanetsFragment : Fragment(R.layout.fragment_planets), TravelInterface {
    private val viewModel: PlanetsViewModel by viewModels()
    private lateinit var binding: FragmentPlanetsBinding
    private lateinit var planetsAdapter: PlanetsAdapter
    private var spacecraftId: Long = -1
    private var isTextChanged: Boolean = false
    private lateinit var countDownTimer: CountDownTimer
    private var currentSpacecraft: Spacecrafts? = null
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var isInitted = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPlanetsBinding.bind(view)

        initView()
        initClick()
        listenSpacecraftData()
        listenPlanetsData()
        listenLocalPlanetsData()
        listenSpacecraftUpdateData()
    }

    private fun initView() {
        spacecraftId = Utils.getSpacecraftId(requireContext())
        planetsAdapter = PlanetsAdapter(null, null, mutableListOf(), listOf(), this)
        binding.run {
            linearLayoutManager = LinearLayoutManager(
                requireContext(),
                RecyclerView.HORIZONTAL,
                false
            )
            recyclerViewPlanets.layoutManager = linearLayoutManager
            recyclerViewPlanets.adapter = planetsAdapter
            val helper: SnapHelper = LinearSnapHelper()
            helper.attachToRecyclerView(recyclerViewPlanets)

            editSearchPlanet.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!editSearchPlanet.text.isNullOrEmpty()) {
                        isTextChanged = true
                        planetsAdapter.filter.filter(editSearchPlanet.text.toString())
                        return@OnEditorActionListener true
                    } else {
                        planetsAdapter.clearFilter()
                        isTextChanged = false
                    }

                }
                false
            })

            editSearchPlanet.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    isTextChanged = true
                    if (s.isEmpty()) {
                        planetsAdapter.clearFilter()
                        isTextChanged = false

                    } else if (s.isNotEmpty() && isTextChanged) {
                        planetsAdapter.filter.filter(editSearchPlanet.text.toString())
                    }
                }
            })
        }

        viewModel.run {
            getSpacecraft(spacecraftId)
        }

    }

    private fun initClick() {
        binding.btnBackward.setOnClickListener {
            if (this::linearLayoutManager.isInitialized) {
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() <= (planetsAdapter.itemCount - 1)) {
                    linearLayoutManager.scrollToPosition(linearLayoutManager.findLastCompletelyVisibleItemPosition() - 1)
                }
            }
        }

        binding.btnForward.setOnClickListener {
            if (this::linearLayoutManager.isInitialized) {
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() < (planetsAdapter.itemCount - 1)) {
                    linearLayoutManager.scrollToPosition(linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1)
                }
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun listenSpacecraftData() {
        viewModel.postSpacecraft.observe(requireActivity(), { spacecraft ->
            currentSpacecraft = spacecraft
            if (!isInitted) {
                if (Utils.getIsPlanetCalled(requireContext()))
                    viewModel.getLocalPlanets()
                else
                    viewModel.getPlanets()
                isInitted=true
            }
            else
                viewModel.getLocalPlanets()
            spacecraft?.run {
                binding.run {
                    Utils.setIsPlanetCalled(requireContext(), true)
                    textUgs.text = "UGS: $spacesuitCount"
                    textEus.text = "EUS: $universalSpaceTime"
                    textDs.text = "DS: $durabilityTime"
                    textSpacecraftName.text = name
                    textDurabilityTime.text = (durability * 10).toString() + "s"
                    textRemainingDurability.text = durabilityTime.toString()
                    textCurrentPlanetName.text = currentLocation

                    if (!this@PlanetsFragment::countDownTimer.isInitialized)
                        createTimer(durability * 10000)
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun createTimer(_durability: Int) {
        var timeInt = _durability / 1000
        countDownTimer = object : CountDownTimer(_durability.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeInt--
                binding.textDurabilityTime.text = "$timeInt s"
            }

            override fun onFinish() {
                currentSpacecraft?.run {
                    val spacecraft = Spacecrafts(
                        id = id,
                        name = name,
                        durability = durability,
                        speed = speed,
                        capacity = capacity,
                        spacesuitCount = spacesuitCount,
                        universalSpaceTime = universalSpaceTime,
                        durabilityTime = durabilityTime - 10,
                        currentLocationId = currentLocationId,
                        currentLocation = currentLocation
                    )
                    viewModel.updateSpacecraft(spacecraft)
                    timeInt = _durability / 1000
                    if (durabilityTime - 10 == 0)
                        buildCompleteDialog(
                            "Dayanıklılık Bitti.",
                            "Dayanıklılık Kalmadı. Dünyaya dönüyoruz."
                        )
                    else
                        countDownTimer.start()
                }

            }
        }
        countDownTimer.start()
    }

    private fun listenPlanetsData() {
        viewModel.postPlanets.observe(requireActivity(), { planets ->
            planets?.let {
                if (it.isNotEmpty()) {
                    for (item in it) {
                        val planet = Planets(
                            name = item.name,
                            coordinateX = item.coordinateX,
                            coordinateY = item.coordinateY,
                            capacity = item.capacity,
                            stock = item.stock,
                            need = item.need,
                            isComplete = 0,
                            isFavorite = 0
                        )
                        viewModel.addPlanet(planet)
                    }
                    viewModel.getLocalPlanets()
                }
            }
        })
    }

    private fun listenLocalPlanetsData() {
        viewModel.postLocalPlanets.observe(requireActivity(), { planets ->
            planets?.let {
                var currentPlanet: Planets? = null
                currentSpacecraft?.let { spacecraft ->
                    var allComplete: Boolean = true
                    var allDistancesAreNotEnough: Boolean = true
                    for (item in it) {
                        Log.d("planet",spacecraft.currentLocationId.toString()+".."+item.id)
                        if (spacecraft.currentLocationId == item.id) {
                            currentPlanet = item
                            break
                        }
                    }

                    if (currentPlanet == null)
                        allDistancesAreNotEnough = false
                    else {
                        for (i in it) {
                            if (spacecraft.currentLocationId != i.id && i.isComplete == 0)
                                allComplete = false
                            val distance = Utils.calculateDistance(
                                currentPlanet!!.coordinateX, i.coordinateX,
                                currentPlanet!!.coordinateY, i.coordinateY
                            )
                            if (spacecraft.universalSpaceTime >= distance)
                                allDistancesAreNotEnough = false
                        }
                    }

                    if (allComplete) {
                        Utils.setIsPlanetCalled(requireContext(), false)
                        buildCompleteDialog(
                            "Yolculuk Bitti",
                            "Tüm gezegenler tamamlandı. Dünyaya dönüyoruz."
                        )
                    }
                    else if (allDistancesAreNotEnough)
                        buildCompleteDialog("Süre Kalmadı", "Süre bitti. Dünyaya dönüyoruz.")

                    //if (!isPlanetsGetted)
                        planetsAdapter.updatePlanets(spacecraft, currentPlanet, it)
                }
            }
        })
    }

    private fun listenSpacecraftUpdateData() {
        viewModel.postSpacecraftDbResponse.observe(requireActivity(), {
            viewModel.getSpacecraft(spacecraftId)
        })
    }

    override fun travelClick(distance: Int, planet: Planets) {
        currentSpacecraft?.run {
            val _newStock: Int
            val _newNeed: Int
            val _isComplete: Int
            val _newSpacesuitCount: Int
            val _newUniversalSpaceTime = universalSpaceTime - distance

            if (planet.need > spacesuitCount) {
                _newStock = planet.stock + spacesuitCount
                _newNeed = planet.need - _newStock
                _isComplete = 0
                _newSpacesuitCount = 0

            } else {
                _newStock = planet.stock + planet.need
                _newNeed = 0
                _isComplete = 1
                _newSpacesuitCount = spacesuitCount - planet.need
            }

            val updatedSpacecraft = Spacecrafts(
                id = id,
                name = name,
                durability = durability,
                speed = speed,
                capacity = capacity,
                spacesuitCount = _newSpacesuitCount,
                universalSpaceTime = _newUniversalSpaceTime,
                durabilityTime = durabilityTime,
                currentLocationId = planet.id,
                currentLocation = planet.name
            )
            viewModel.updateSpacecraft(updatedSpacecraft)

            planet.run {
                val updatedPlanet = Planets(
                    id = id,
                    name = name,
                    coordinateX = coordinateX,
                    coordinateY = coordinateY,
                    capacity = capacity,
                    stock = _newStock,
                    need = _newNeed,
                    isComplete = _isComplete,
                    isFavorite = isFavorite
                )
                viewModel.updatePlanet(updatedPlanet)
            }

            if (_newSpacesuitCount == 0) {
                buildCompleteDialog("Malzeme Bitti", "Malzeme Kalmadı. Dünyaya dönüyoruz.")
            } else if (_newUniversalSpaceTime == 0) {
                buildCompleteDialog("Süre Bitti", "Süre Kalmadı. Dünyaya dönüyoruz.")
            }
        }

    }

    private fun buildCompleteDialog(title: String, description: String) {
        val builder = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(description)
            .setCancelable(false)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton("Dünyaya Dön") { dialogInterface, _ ->
                Utils.setIsSpacecraftCreated(requireContext(), false)
                dialogInterface.cancel()
                val createSpaceCraftFragment = CreateSpaceCraftFragment()
                (activity as MainActivity).replaceFragment(
                    createSpaceCraftFragment,
                    "CreateSpaceCraftFragment"
                )

            }


        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    override fun favoriteClick(planet: Planets) {
        planet.run {
            val updatedPlanet = Planets(
                id = id,
                name = name,
                coordinateX = coordinateX,
                coordinateY = coordinateY,
                capacity = capacity,
                stock = stock,
                need = need,
                isComplete = isComplete,
                isFavorite = isFavorite
            )
            viewModel.updatePlanet(updatedPlanet)
        }
    }

    override fun onDetach() {
        if (this::countDownTimer.isInitialized)
            countDownTimer.cancel()
        super.onDetach()
    }
}