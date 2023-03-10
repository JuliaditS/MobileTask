package ac.id.unikom.codelabs.radio.mvvm

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import ac.id.unikom.codelabs.radio.R
import kotlinx.android.synthetic.main.splash_screen.*
import android.content.Intent
import ac.id.unikom.codelabs.radio.mvvm.main.MainActivity


class SplashScreen : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        splashscreen_proggressbar.indeterminateDrawable.colorFilter

        Handler().postDelayed({
            startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            finish()
        }, 1000L)
    }


}