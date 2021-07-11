/*
 * Copyright (c) 2021 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.drinkup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.savedstate.SavedStateRegistry

class DrinkFragment : Fragment() {

  private lateinit var drinkTextView: TextView
  private lateinit var incrementButton: Button
  private lateinit var decrementButton: Button
  private lateinit var resetButton: Button

  companion object {
    private const val SAVED_STATE_KEY = "drinks_saved_state"
    private const val DRINKS_KEY = "drinks_key"
    private const val DRINKS_DEFAULT_VALUE = "0"
  }

  private val savedStateProvider = SavedStateRegistry.SavedStateProvider {
    Bundle().apply {
      putString(DRINKS_KEY, drinks)
    }
  }

  private lateinit var drinks: String

  override fun onSaveInstanceState(bundle: Bundle) {
    super.onSaveInstanceState(bundle)
    // Save the user's current drinks count
    bundle.putString(DRINKS_KEY, drinkTextView.text.toString())
  }

  override fun onViewStateRestored(savedInstanceState: Bundle?) {
    super.onViewStateRestored(savedInstanceState)
    if (savedInstanceState != null) {
      // Display the restored number of drinks
      val restoredDrinks = savedInstanceState.getString(DRINKS_KEY, DRINKS_DEFAULT_VALUE)
      drinkTextView.text = restoredDrinks
    } else {
      drinkTextView.text = DRINKS_DEFAULT_VALUE
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // Restore the previously saved state
    drinks = savedStateRegistry
        .consumeRestoredStateForKey(SAVED_STATE_KEY)
        ?.getString(DRINKS_KEY) ?: DRINKS_DEFAULT_VALUE
    // Register for future state changes
    savedStateRegistry
        .registerSavedStateProvider(SAVED_STATE_KEY, savedStateProvider)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_drink, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    drinkTextView = view.findViewById(R.id.drinkTextView)
    incrementButton = view.findViewById(R.id.addButton)
    decrementButton = view.findViewById(R.id.subtractButton)
    resetButton = view.findViewById(R.id.resetButton)

    incrementButton.setOnClickListener { incrementDrinkCount() }
    decrementButton.setOnClickListener { decrementDrinkCount() }
    resetButton.setOnClickListener { resetDrinkCount() }
  }

  private fun incrementDrinkCount() {
    val count = drinkTextView.text.toString().toInt()
    drinkTextView.text = (count + 1).toString()
  }

  private fun decrementDrinkCount() {
    val count = drinkTextView.text.toString().toInt()
    if (count > 0) {
      drinkTextView.text = (count - 1).toString()
    }
  }

  private fun resetDrinkCount() {
    drinkTextView.text = DRINKS_DEFAULT_VALUE
  }
}