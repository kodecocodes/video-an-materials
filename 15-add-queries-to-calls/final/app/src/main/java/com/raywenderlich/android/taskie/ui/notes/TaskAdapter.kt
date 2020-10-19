/*
 * Copyright (c) 2020 Razeware LLC
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

package com.raywenderlich.android.taskie.ui.notes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.android.taskie.R
import com.raywenderlich.android.taskie.model.Task

/**
 * Displays the tasks from the API, into a list of items.
 */
class TaskAdapter(
    private val onItemLongClick: (String) -> Unit
) : RecyclerView.Adapter<TaskHolder>() {

  private val data: MutableList<Task> = mutableListOf()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
    return TaskHolder(view)
  }

  override fun getItemCount() = data.size

  override fun onBindViewHolder(holder: TaskHolder, position: Int) {
    holder.bindData(data[position], onItemLongClick)
  }

  fun addData(item: Task) {
    data.add(item)
    notifyItemInserted(data.size)
  }

  fun setData(data: List<Task>) {
    this.data.clear()
    this.data.addAll(data)
    notifyDataSetChanged()
  }

  fun removeTask(taskId: String) {
    val taskIndex = data.indexOfFirst { it.id == taskId }

    if (taskIndex != -1) {
      data.removeAt(taskIndex)
      notifyItemRemoved(taskIndex)
    }
  }
}