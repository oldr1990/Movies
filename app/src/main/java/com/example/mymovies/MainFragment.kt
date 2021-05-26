package com.example.mymovies

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.size.Scale
import com.example.mymovies.animations.WaitingAnimation
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var waitingAnimation: WaitingAnimation
    private lateinit var viewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }
	
    @SuppressLint("ResourceType", "SetTextI18n", "ShowToast")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        backgroundImageMain.load(R.drawable.movie_info_searcher){
            scale(Scale.FILL)
        }
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java) 
        waitingAnimation = WaitingAnimation(imageViewWaiting,cardViewWaitingAnimation)	
        spinnerSetup()																	
        updateUI()
		
		//обработчик ошибки: нет интернета
        viewModel.errorMessage.observe(viewLifecycleOwner, {							
            if (it == false) {
                Toast.makeText(
                    view?.context,
                    "Internet connection is failed. Please check it.",
                    Toast.LENGTH_SHORT
                ).show()
                updateUI()
            }

        })
		
		//обработчик ошибки запроса
        viewModel.wrongRequest.observe(viewLifecycleOwner, {							
            if (it == true) {
                viewModel.currentPage = 1
                viewModel.currentYear = ""
                viewModel.currentType = 0
                viewModel.numberOfResult.postValue(1)
                input_text.setText("")
                editTextYear.setText("")
                search_type_spinner.setSelection(0)
                Toast.makeText(
                    view?.context,
                    "Your request is wrong. Please check it.",
                    Toast.LENGTH_SHORT
                ).show()
                updateUI()
            }
        })
		
		//слушатель кнопки поиск
        recyclerview?.layoutManager = LinearLayoutManager(requireContext())
        search_button.setOnClickListener {												
            viewModel.currentPage = 1
            requestData()
            disablingUI()

        }
    }

//выключает интерфейс
    private fun disablingUI() {			
        search_button.isClickable = false
        cardViewLastPageNavigation.isClickable = false
        cardViewBackPageNavigation.isClickable = false
        cardViewNextPageNavigation.isClickable = false
        cardViewFirstPageNavigation.isClickable = false
        waitingAnimation.turnOnAnimation()

    }


    //Запрашивает данные с сервера
    private fun requestData() {
        Log.e("!@#", "requestData was called")
        viewModel.getData(
            input_text.text.toString(),
            editTextYear.text.toString(),
            search_type_spinner.selectedItem.toString(),
            viewModel.currentPage
        )
    }
//выпадающий список фильтра типа
    private fun spinnerSetup() {
        viewModel?.currentYear?.let {
            editTextYear.setText(it)
        }
        search_type_spinner.setSelection(viewModel.currentType)
        val types = resources.getStringArray(R.array.type)
        if (search_type_spinner != null) {
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, types)
            search_type_spinner.adapter = arrayAdapter
            search_type_spinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        viewModel.currentType = position
                        Log.e("!@#", "id = ${position}")
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
        }
    }
	
	//обновляем интерфейс
    @SuppressLint("SetTextI18n")
    private fun updateUI() {
        Log.e("!@#", "Update UI!")
        waitingAnimation.turnOffAnimation()
        search_button.isClickable = true
        viewModel.numberOfResult.observe(viewLifecycleOwner, {
            if (it != null && it != 0) {
                val pages = when (it % 10) {
                    0 -> it / 10
                    in 1..9 -> it / 10 + 1
                    else -> 1
                }
                textViewResultNumber.text =
                    resources.getString(R.string.result_of_searching_label) + it.toString()
                cardViewResultNumber.visibility = CardView.VISIBLE
                bottomNavigation(viewModel.currentPage, pages)
            }
        })
        Log.e("!@#", "updateUI was called!")
        viewModel.movieData.observe(viewLifecycleOwner, {
            it?.let {
                recyclerview.adapter = MovieAdapter(it)
                waitingAnimation.turnOffAnimation()
                search_button.isClickable = true
            }
        })
    }
	//вывод постраничной навигации вызывается только из UpdateUI()
    @SuppressLint("SetTextI18n")
    private fun bottomNavigation(currentPage: Int, pages: Int) {
        if (pages >= 2) {
            textViewPageNavigation.text = "$currentPage/$pages pages"
            cardViewBottomNavigation.visibility = CardView.VISIBLE
            if (currentPage > 1) {
                pageFirstPageNavigation.setTextColor(Color.BLACK)
                pageBackNavigation.setTextColor(Color.BLACK)
                cardViewFirstPageNavigation.setOnClickListener {
                    viewModel.currentPage = 1
                    requestData()
                    disablingUI()
                }
                cardViewBackPageNavigation.setOnClickListener {
                    viewModel.currentPage--
                    requestData()
                    disablingUI()
                }
            } else {
                pageFirstPageNavigation.setTextColor(Color.GRAY)
                pageBackNavigation.setTextColor(Color.GRAY)
                cardViewBackPageNavigation.isClickable = false
                cardViewFirstPageNavigation.isClickable = false
            }
            if (currentPage < pages) {
                pageNextPageNavigation.setTextColor(Color.BLACK)
                pageLastPageNavigation.setTextColor(Color.BLACK)
                cardViewNextPageNavigation.setOnClickListener {
                    viewModel.currentPage++
                    requestData()
                    disablingUI()
                }
                cardViewLastPageNavigation.setOnClickListener {
                    viewModel.currentPage = pages
                    requestData()
                    disablingUI()
                }
            } else {
                pageNextPageNavigation.setTextColor(Color.GRAY)
                pageLastPageNavigation.setTextColor(Color.GRAY)
                cardViewLastPageNavigation.isClickable = false
                cardViewNextPageNavigation.isClickable = false
            }
        } else cardViewBottomNavigation.visibility = CardView.GONE


    }
}