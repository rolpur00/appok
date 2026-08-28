package com.example.appok

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children

class MainActivity : AppCompatActivity() {

    private lateinit var gridLayout: GridLayout
    private lateinit var searchField: EditText
    private lateinit var exitBtn: ImageButton
    private lateinit var menuBtn: ImageButton
    private var allApps = mutableListOf<AppInfo>()
    private var gridColumns = 3
    private var gridRows = 3
    private var currentPage = 0
    private var totalPages = 1

    data class AppInfo(val label: String, val packageName: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridLayout = findViewById(R.id.app_grid)
        searchField = findViewById(R.id.search_field)
        exitBtn = findViewById(R.id.exit_btn)
        menuBtn = findViewById(R.id.menu_btn)

        loadInstalledApps()
        displayCurrentPage()
        setupListeners()
    }

    private fun loadInstalledApps() {
        val pm = packageManager
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val apps = pm.queryIntentActivities(intent, 0)

        allApps.clear()
        for (app in apps) {
            allApps.add(
                AppInfo(
                    label = app.loadLabel(pm).toString(),
                    packageName = app.activityInfo.packageName
                )
            )
        }
        allApps.sortBy { it.label }
        updateTotalPages()
    }

    private fun displayCurrentPage() {
        gridLayout.removeAllViews()
        gridLayout.columnCount = gridColumns
        gridLayout.rowCount = gridRows

        val appsPerPage = gridColumns * gridRows
        val startIndex = currentPage * appsPerPage
        val endIndex = minOf(startIndex + appsPerPage, allApps.size)

        val appsToShow = allApps.subList(startIndex, endIndex)

        for (app in appsToShow) {
            val appButton = LayoutInflater.from(this)
                .inflate(R.layout.app_item, gridLayout, false) as LinearLayout
            appButton.tag = app.packageName
            appButton.setOnClickListener {
                launchApp(app.packageName)
            }
            gridLayout.addView(appButton)
        }
    }

    private fun launchApp(packageName: String) {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            startActivity(intent)
        }
    }

    private fun setupListeners() {
        exitBtn.setOnClickListener {
            finish()
        }

        menuBtn.setOnClickListener {
            showMenu()
        }

        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterApps(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterApps(query: String) {
        if (query.isBlank()) {
            currentPage = 0
            displayCurrentPage()
        } else {
            gridLayout.removeAllViews()
            gridLayout.columnCount = gridColumns
            val filtered = allApps.filter {
                it.label.contains(query, ignoreCase = true)
            }
            for (app in filtered) {
                val appButton = LayoutInflater.from(this)
                    .inflate(R.layout.app_item, gridLayout, false) as LinearLayout
                appButton.tag = app.packageName
                appButton.setOnClickListener {
                    launchApp(app.packageName)
                }
                gridLayout.addView(appButton)
            }
        }
    }

    private fun showMenu() {
        val options = arrayOf("Elrendezés szerkesztése", "Lapok szerkesztése", "Rács mérete")
        AlertDialog.Builder(this)
            .setTitle("Beállítások")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> editLayout()
                    1 -> editPages()
                    2 -> setGridSize()
                }
            }
            .show()
    }

    private fun editLayout() {
        AlertDialog.Builder(this)
            .setTitle("Elrendezés szerkesztése")
            .setMessage("Ez a funkció a jövőben lesz elérhető.")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun editPages() {
        AlertDialog.Builder(this)
            .setTitle("Lapok szerkesztése")
            .setMessage("Jelenlegi lap: ${currentPage + 1} / $totalPages")
            .setPositiveButton("Előző") { _, _ ->
                if (currentPage > 0) {
                    currentPage--
                    displayCurrentPage()
                }
            }
            .setNegativeButton("Következő") { _, _ ->
                if (currentPage < totalPages - 1) {
                    currentPage++
                    displayCurrentPage()
                }
            }
            .show()
    }

    private fun setGridSize() {
        val options = arrayOf("3×3", "4×4", "5×5")
        AlertDialog.Builder(this)
            .setTitle("Rács mérete")
            .setSingleChoiceItems(options, 0) { dialog, which ->
                gridColumns = when (which) {
                    0 -> 3
                    1 -> 4
                    2 -> 5
                    else -> 3
                }
                gridRows = gridColumns
                updateTotalPages()
                currentPage = 0
                displayCurrentPage()
                dialog.dismiss()
            }
            .show()
    }

    private fun updateTotalPages() {
        val appsPerPage = gridColumns * gridRows
        totalPages = (allApps.size + appsPerPage - 1) / appsPerPage
    }
}
