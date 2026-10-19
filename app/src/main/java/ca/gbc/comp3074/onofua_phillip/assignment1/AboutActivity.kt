package ca.gbc.comp3074.onofua_phillip.assignment1

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem

class AboutActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            // Return to the existing MainActivity and retain its inputs/results.
            finish()
            true
        } else super.onOptionsItemSelected(item)
    }
}
