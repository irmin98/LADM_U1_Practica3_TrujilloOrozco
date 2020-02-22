package mx.edu.ittepic.ladm_u1_practica3_trujilloorozco

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    var arreglo: Array<Int> = Array(10) { 0 }
    var datos = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permisos()


        btnAsignar.setOnClickListener {
            asignarValor()
        }



        btnMostrar.setOnClickListener {
            datos = ""
            (0..9).forEach {
                datos += "${arreglo[it]},"
            }
            mensaje(datos)
            txtMostrar.text = datos
        }

        btnGuardarSD.setOnClickListener {
            if (noSD()) {
                mensaje("Inserte una memoria SD para guardar el archivo")
                return@setOnClickListener
            }

            if (txtNombreG.text.toString().isEmpty()) {
                mensaje("El nombre del archivo no puede estár vacio")
                return@setOnClickListener
            }

            try {
                permisos() //comprobar y solicitar permisos SD

                var rutaSD = Environment.getExternalStorageDirectory()
                var datosArchivo = File(
                    rutaSD.absolutePath,
                    txtNombreG.text.toString() + ".txt"
                ) //file (ruta,nombre)
                var flujoSalida = OutputStreamWriter(FileOutputStream(datosArchivo))
                datos = txtMostrar.text.toString()
                flujoSalida.write(datos)
                flujoSalida.flush() //forzar escritura
                flujoSalida.close()
                mensaje("El archivo fue creado exitosamente")

                txtNombreG.setText("")

            } catch (error: IOException) {
                mensaje(error.message.toString())
            }
        }


        btnLeerSD.setOnClickListener {
            permisos()
            if (noSD()) {
                mensaje("Inserte una memoria SD para leer el archivo")
                return@setOnClickListener
            }

            if (txtNombreL.text.toString().isEmpty()) {
                mensaje("El nombre del archivo no puede estár vacio")
                return@setOnClickListener
            }
          //  Toast.makeText(this,"HOLA",Toast.LENGTH_LONG).show()
            try {
                var rutaSD = Environment.getExternalStorageDirectory()
                var datosArchivo = File(
                    rutaSD.absolutePath,
                    txtNombreL.text.toString() + ".txt"
                )



                var flujoEntrada =
                    BufferedReader(InputStreamReader(FileInputStream(datosArchivo)))

                var data = flujoEntrada.readLine()

                txtMostrar.setText(data)

                var vector = data.split(",")

                (0..9).forEach {
                    arreglo[it] = vector[it].toInt()
                }
                txtNombreL.setText("")

            } catch (error: IOException) {
                mensaje(error.message.toString())
            }

        }








    }//onCreate

    fun permisos() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), 0
            )
        } else {
            //mensaje("LOS PERMISOS YA FUERON OTORGADOS")
        }
    }//permisos

    fun asignarValor() {
        if (txtValor.text.toString().isEmpty() || txtPosicion.text.toString().isEmpty()) {
            mensaje("ERROR,campos vacios")
            return
        }
        if (txtPosicion.text.toString().toInt() < 0 || txtPosicion.text.toString().toInt() > 9) {
            mensaje("ERROR,posición solo de  0 al 9")
            return
        }
        var posicion = txtPosicion.text.toString().toInt()
        var valor = txtValor.text.toString().toInt()
        arreglo[posicion] = valor
        txtValor.setText("")
        txtPosicion.setText("")
    }//asignarValor


    private fun mensaje(m: String) {
        AlertDialog.Builder(this)
            .setTitle("ATENCION")
            .setMessage(m)
            .setPositiveButton("ACEPTAR") { d, i -> }
            .show()
    }//mensaje

    fun noSD(): Boolean {
        var estado = Environment.getExternalStorageState()
        if (estado != Environment.MEDIA_MOUNTED) {
            return true
        }
        return false
    }




}
