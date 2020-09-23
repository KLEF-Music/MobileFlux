package com.klefmusic.flux.ui

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.textChanges
import com.klefmusic.flux.R
import com.klefmusic.flux.ui.success.SuccessActivity
import com.klefmusic.flux.validate.EmailValidResult
import com.klefmusic.flux.validate.PasswordValidResult
import com.klefmusic.rxfluxcore.EffectMapper
import com.klefmusic.rxfluxcore.StoreView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.merge
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity(), StoreView<LoginState, LoginEvents, LoginEffects> {

    override val store: LoginStore by inject()

    override val flow: Observable<LoginState> = store.updates

    override fun effectMapper(): LoginEffectMapper = get()

    private lateinit var disposeable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")
        setContentView(R.layout.activity_login)
        disposeable = connect(AndroidSchedulers.mainThread())
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeable.dispose()
    }

    override fun events(): Observable<LoginEvents> {
        val onLoginClick = signInButton.clicks().map {
            LoginEvents.LoginClicked(
                emailEntry.text.toString(),
                passwordEntry.text.toString()
            )
        }

        // text entry is debounced to prevent lots of events
        val debounceTextEntry: Long = 300

        // emit EmailChanged event on text entry
        val onEmailChanged = emailEntry.textChanges()
            .skipInitialValue()
            .debounce(debounceTextEntry, TimeUnit.MILLISECONDS)
            .map { LoginEvents.EmailChanged(it.toString()) }

        // emit PasswordChanged event on text entry
        val onPasswordChanged = passwordEntry.textChanges()
            .skipInitialValue()
            .debounce(debounceTextEntry, TimeUnit.MILLISECONDS)
            .map { LoginEvents.PasswordChanged(it.toString()) }

        // merge all events in to single observable
        return listOf(
            onLoginClick,
            onEmailChanged,
            onPasswordChanged
        ).merge()
    }

    override fun render(viewState: LoginState) {
        emailEntry.rxSetText(viewState.email)
        passwordEntry.rxSetText(viewState.password)


        emailEntryContainer.error = when (viewState.emailValid) {
            EmailValidResult.Valid -> ""
            EmailValidResult.TooShort -> "Email Too short :("
            EmailValidResult.BadlyFormatted -> "Email badly formatted Short :("
        }

        passwordEntryContainer.error = when (viewState.passwordValid) {
            PasswordValidResult.Valid -> ""
            PasswordValidResult.TooShort -> "Password too short :("
        }

        signInButton.isEnabled = viewState.canSignIn
        signInButton.isInvisible = viewState.loading
        progressBar.isInvisible = !viewState.loading
    }

    override fun effects(effects: LoginEffects) {
        when (effects) {
            LoginEffects.OpenLoggedIn -> startActivity(SuccessActivity.newIntent(this))
        }
    }

}

fun EditText.rxSetText(newText: CharSequence) {
    if (this.text.toString() != newText.toString()) {
        this.setText(newText)
    }
}

