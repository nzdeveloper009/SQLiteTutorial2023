package nzcoding.android.sqlitetutorial

import android.content.ContentValues
import android.content.DialogInterface
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView.OnQueryTextListener
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import nzcoding.android.sqlitetutorial.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
	lateinit var binding: ActivityMainBinding
	lateinit var db: SQLiteDatabase
	lateinit var cursor: Cursor
	lateinit var adapter: SimpleCursorAdapter
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		var helper = MyHelper(applicationContext)
		db = helper.readableDatabase
		cursor = db.rawQuery("SELECT * FROM ACTABLE ORDER BY NAME", null)
		var cv = ContentValues()

		binding.firstBtn.setOnClickListener {
			if (cursor.moveToFirst()) {
				binding.nameEt.setText(cursor.getString(1))
				binding.descriptionEt.setText(cursor.getString(2))
			} else
				Toast.makeText(this@MainActivity, "No Data Found", Toast.LENGTH_LONG).show()
		}

		binding.nextBtn.setOnClickListener {
			if (cursor.moveToNext()) {
				binding.nameEt.setText(cursor.getString(1))
				binding.descriptionEt.setText(cursor.getString(2))
			} else if (cursor.moveToFirst()) {
				binding.nameEt.setText(cursor.getString(1))
				binding.descriptionEt.setText(cursor.getString(2))
			} else
				Toast.makeText(this@MainActivity, "No Data Found", Toast.LENGTH_LONG).show()
		}

		binding.prevBtn.setOnClickListener {
			if (cursor.moveToPrevious()) {
				binding.nameEt.setText(cursor.getString(1))
				binding.descriptionEt.setText(cursor.getString(2))
			} else if (cursor.moveToLast()) {
				binding.nameEt.setText(cursor.getString(1))
				binding.descriptionEt.setText(cursor.getString(2))
			} else
				Toast.makeText(this@MainActivity, "No Data Found", Toast.LENGTH_LONG).show()
		}

		binding.lastBtn.setOnClickListener {
			if (cursor.moveToLast()) {
				binding.nameEt.setText(cursor.getString(1))
				binding.descriptionEt.setText(cursor.getString(2))
			} else
				Toast.makeText(this@MainActivity, "No Data Found", Toast.LENGTH_LONG).show()
		}
		binding.insertBtn.setOnClickListener {
			cv.put("NAME", binding.nameEt.text.toString())
			cv.put("MEANING", binding.descriptionEt.text.toString())
			db.insert("ACTABLE", null, cv)
			cursor.requery()

			var ad = AlertDialog.Builder(this)
			ad.setTitle("Add Record")
			ad.setMessage("Record Inserted Successfully..!!")
			ad.setPositiveButton("Yes", object : DialogInterface.OnClickListener {
				override fun onClick(dialog: DialogInterface?, which: Int) {
					binding.nameEt.setText("")
					binding.descriptionEt.setText("")
					binding.nameEt.requestFocus()
				}

			})
			ad.show()
		}

		binding.updateBtn.setOnClickListener {
			cv.put("NAME", binding.nameEt.text.toString())
			cv.put("MEANING", binding.descriptionEt.text.toString())
			db.update("ACTABLE", cv, "_id = ?", arrayOf(cursor.getString(0)))
			cursor.requery()

			var ad = AlertDialog.Builder(this)
			ad.setTitle("Update Record")
			ad.setMessage("Record Updated Successfully..!!")
			ad.setPositiveButton("Yes", object : DialogInterface.OnClickListener {
				override fun onClick(dialog: DialogInterface?, which: Int) {
					if (cursor.moveToFirst()) {
						binding.nameEt.setText(cursor.getString(1))
						binding.descriptionEt.setText(cursor.getString(2))
					}
				}

			})
			ad.show()
		}

		binding.deleteBtn.setOnClickListener {
			db.delete("ACTABLE", "_id = ?", arrayOf(cursor.getString(0)))
			cursor.requery()

			var ad = AlertDialog.Builder(this)
			ad.setTitle("Delete Record")
			ad.setMessage("Delete Updated Successfully..!!")
			ad.setPositiveButton("Yes", object : DialogInterface.OnClickListener {
				override fun onClick(dialog: DialogInterface?, which: Int) {
					if (cursor.moveToFirst()) {
						binding.nameEt.setText(cursor.getString(1))
						binding.descriptionEt.setText(cursor.getString(2))
					} else {
						binding.nameEt.setText("No Data found")
						binding.descriptionEt.setText("No Data found")
					}
				}

			})
			ad.show()

		}

		adapter = SimpleCursorAdapter(
			applicationContext, android.R.layout.simple_expandable_list_item_2, cursor,
			arrayOf("NAME", "MEANING"),
			intArrayOf(android.R.id.text1, android.R.id.text2), 0
		)


		binding.viewAllBtn.setOnClickListener {
			adapter.notifyDataSetChanged()
			binding.searchView.isIconified = false
			binding.searchView.queryHint = "Search Among ${cursor.count} Record"
			binding.searchView.visibility = View.VISIBLE
			binding.listView.visibility = View.VISIBLE
		}

		registerForContextMenu(binding.listView)

		binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
			override fun onQueryTextSubmit(query: String?): Boolean {
				return false
			}

			override fun onQueryTextChange(newText: String?): Boolean {
				cursor = db.rawQuery(
					"SELECT * FROM ACTABLE WHERE NAME LIKE '%${newText}%' or MEANING LIKE '%${newText}%'",
					null
				)
				adapter.changeCursor(cursor)
				return false
			}

		})


		binding.listView.adapter = adapter

		binding.clearBtn.setOnClickListener {
			binding.nameEt.setText("")
			binding.descriptionEt.setText("")
			binding.nameEt.requestFocus()
		}
	}

	override fun onCreateContextMenu(
		menu: ContextMenu?,
		v: View?,
		menuInfo: ContextMenu.ContextMenuInfo?
	) {
		super.onCreateContextMenu(menu, v, menuInfo)
		menu?.add(1, 1, 1, "DELETE")
		menu?.setHeaderTitle("Removing Data")
	}

	override fun onContextItemSelected(item: MenuItem): Boolean {
		if (item.itemId == 1) {
			db.delete("ACTABLE", "_id = ?", arrayOf(cursor.getString(0)))
			cursor.requery()
			binding.searchView.queryHint = "Search Among ${cursor.count} Record"
			adapter.notifyDataSetChanged()
			Toast.makeText(
				applicationContext,
				"You clicked on Delete Context Menu",
				Toast.LENGTH_LONG
			).show()
		}
		return super.onContextItemSelected(item)
	}


}

