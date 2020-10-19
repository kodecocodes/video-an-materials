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

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.raywenderlich.android.taskie.App
import com.raywenderlich.android.taskie.R
import com.raywenderlich.android.taskie.model.Success
import com.raywenderlich.android.taskie.model.Task
import com.raywenderlich.android.taskie.networking.NetworkStatusChecker
import com.raywenderlich.android.taskie.ui.notes.dialog.AddTaskDialogFragment
import com.raywenderlich.android.taskie.ui.notes.dialog.TaskOptionsDialogFragment
import com.raywenderlich.android.taskie.utils.gone
import com.raywenderlich.android.taskie.utils.toast
import com.raywenderlich.android.taskie.utils.visible
import kotlinx.android.synthetic.main.fragment_notes.*

/**
 * Fetches and displays notes from the API.
 */
class NotesFragment : Fragment(), AddTaskDialogFragment.TaskAddedListener,
    TaskOptionsDialogFragment.TaskOptionSelectedListener {

  private val adapter by lazy { TaskAdapter(::onItemSelected) }
  private val remoteApi = App.remoteApi
  private val networkStatusChecker by lazy {
    NetworkStatusChecker(activity?.getSystemService(ConnectivityManager::class.java))
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_notes, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initUi()
    initListeners()
  }

  private fun initUi() {
    progress.visible()
    noData.visible()
    tasksRecyclerView.layoutManager = LinearLayoutManager(context)
    tasksRecyclerView.adapter = adapter
    getAllTasks()
  }

  private fun initListeners() {
    addTask.setOnClickListener { addTask() }
  }

  private fun onItemSelected(taskId: String) {
    val dialog = TaskOptionsDialogFragment.newInstance(taskId)
    dialog.setTaskOptionSelectedListener(this)
    dialog.show(childFragmentManager, dialog.tag)
  }

  override fun onTaskAdded(task: Task) {
    adapter.addData(task)
  }

  private fun addTask() {
    val dialog = AddTaskDialogFragment()
    dialog.setTaskAddedListener(this)
    dialog.show(childFragmentManager, dialog.tag)
  }

  private fun getAllTasks() {
    progress.visible()
    networkStatusChecker.performIfConnectedToInternet {
      remoteApi.getTasks { result ->
        if (result is Success) {
          onTaskListReceived(result.data)
        } else {
          onGetTasksFailed()
        }
      }
    }
  }

  private fun checkList(notes: List<Task>) {
    if (notes.isEmpty()) noData.visible() else noData.gone()
  }

  private fun onTasksReceived(tasks: List<Task>) = adapter.setData(tasks)

  private fun onTaskListReceived(tasks: List<Task>) {
    progress.gone()
    checkList(tasks)
    onTasksReceived(tasks)
  }

  private fun onGetTasksFailed() {
    progress.gone()
    activity?.toast("Failed to fetch tasks!")
  }

  override fun onTaskDeleted(taskId: String) {
    adapter.removeTask(taskId)
    activity?.toast("Task deleted!")
  }

  override fun onTaskCompleted(taskId: String) {
    adapter.removeTask(taskId)
    activity?.toast("Task completed!")
  }
}