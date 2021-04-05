package tw.y_studio.ptt.fragment.login

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Paint
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import tw.y_studio.ptt.FragmentTouchListener
import tw.y_studio.ptt.HomeActivity
import tw.y_studio.ptt.R
import tw.y_studio.ptt.databinding.IncludeLoginTabPageBinding
import tw.y_studio.ptt.databinding.LoginPageFragmentBinding
import tw.y_studio.ptt.ui.BaseFragment
import tw.y_studio.ptt.utils.KeyboardUtils
import tw.y_studio.ptt.utils.PreferenceConstants
import tw.y_studio.ptt.utils.observeEventNotNull
import kotlin.math.absoluteValue

class LoginPageFragment : BaseFragment(), FragmentTouchListener, View.OnClickListener {
    private val viewModel by viewModel<LoginPageViewModel>()

    private lateinit var binding: LoginPageFragmentBinding

    private lateinit var loginTabBinding: IncludeLoginTabPageBinding

    private var isShowPassword = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as HomeActivity).fragmentTouchListener = this
    }

    override fun onDetach() {
        (requireActivity() as HomeActivity).fragmentTouchListener = null
        super.onDetach()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginPageFragmentBinding.inflate(inflater, container, false)
        loginTabBinding = IncludeLoginTabPageBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments // 取得Bundle
        val id = currentActivity
            .getSharedPreferences(PreferenceConstants.prefName, Context.MODE_PRIVATE)
            .getString(PreferenceConstants.id, "") ?: ""
        binding.root.addOnLayoutChangeListener { _, _, top, _, bottom, _, oldTop, _, oldBottom ->
            if ((bottom - top).absoluteValue < (oldBottom - oldTop)) {
                binding.scrollLoginPage.post {
                    binding.scrollLoginPage.smoothScrollTo(0, binding.spaceLoginPageTitleToSelector.top)
                }
            }
        }
        binding.apply {
            textLoginPageLoginType.setOnClickListener(this@LoginPageFragment)
            textLoginPageRegisterType.setOnClickListener(this@LoginPageFragment)
        }
        loginTabBinding.apply {
            textLoginPageServiceTerms.paintFlags = textLoginPageServiceTerms.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            editLoginPageAccount.setText(id)
            btnLoginPageLogin.setOnClickListener(this@LoginPageFragment)
            btnLoginPageLogin.isEnabled = false
            btnLoginPageLogin.isClickable = false
            btnLoginPageShowPassword.setOnClickListener(this@LoginPageFragment)
            editLoginPageAccount.addTextChangedListener(
                beforeTextChanged = { char: CharSequence?, start: Int, count: Int, after: Int ->
                },
                onTextChanged = { char: CharSequence?, start: Int, count: Int, after: Int ->
                    textLoginPageAccountMessage.isVisible = false
                    textLoginPagePasswordMessage.isVisible = false
                    val password = editLoginPagePassword.text.toString()
                    val haveAccount = char?.isNotEmpty() == true && password.isNotBlank()
                    loginButtonEnable(haveAccount)
                },
                afterTextChanged = {}
            )
            editLoginPagePassword.addTextChangedListener(
                beforeTextChanged = { char: CharSequence?, start: Int, count: Int, after: Int ->
                },
                onTextChanged = { char: CharSequence?, start: Int, count: Int, after: Int ->
                    textLoginPageAccountMessage.isVisible = false
                    textLoginPagePasswordMessage.isVisible = false
                    val account = editLoginPageAccount.text.toString()
                    val havePassword = char?.isNotEmpty() == true && account.isNotBlank()
                    loginButtonEnable(havePassword)
                },
                afterTextChanged = {}
            )

            if (!isShowPassword) {
                btnLoginPageShowPassword.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_visibility_off_24
                    )
                )
            } else {
                btnLoginPageShowPassword.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_visibility_24
                    )
                )
            }

            viewModel.apply {
                passwordMessage.observe(viewLifecycleOwner) {
                    loginTabBinding.textLoginPagePasswordMessage.setText(it)
                    loginTabBinding.textLoginPagePasswordMessage.isVisible = true
                }
                errorMessage.observeEventNotNull(viewLifecycleOwner) {
                    Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
                }
                loginSuccess.observe(viewLifecycleOwner) {
                    Toast.makeText(requireActivity(), "登入成功！", Toast.LENGTH_SHORT).show()
                    currentActivity.onBackPressed()
                }
                loginTypeTabColor.observe(viewLifecycleOwner) {
                    binding.textLoginPageLoginType.setTextColor(ContextCompat.getColor(requireContext(), it))
                }
                registerTypeTabColor.observe(viewLifecycleOwner) {
                    binding.textLoginPageRegisterType.setTextColor(ContextCompat.getColor(requireContext(), it))
                }
            }
        }
    }

    private fun loginButtonEnable(isEnable: Boolean) {
        if (isEnable) {
            loginTabBinding.btnLoginPageLogin.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.tangerine)
            )
            loginTabBinding.btnLoginPageLogin.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        } else {
            loginTabBinding.btnLoginPageLogin.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.black)
            )
            loginTabBinding.btnLoginPageLogin.setTextColor(ContextCompat.getColor(requireContext(), R.color.tangerine))
        }
        loginTabBinding.btnLoginPageLogin.isEnabled = isEnable
        loginTabBinding.btnLoginPageLogin.isClickable = isEnable
    }

    private fun showLoginTabPage(isVisible: Boolean) {
        loginTabBinding.apply {
            editLoginPageAccount.isVisible = isVisible
            spaceLoginPageAccountToPassword.isVisible = isVisible
            textLoginPageAccountMessage.isVisible = isVisible
            editLoginPagePassword.isVisible = isVisible
            spaceLoginPagePasswordToLogin.isVisible = isVisible
            spaceLoginPageLoginToForgotDivider.isVisible = isVisible
            textLoginPagePasswordMessage.isVisible = isVisible
            btnLoginPageShowPassword.isVisible = isVisible
            btnLoginPageLogin.isVisible = isVisible
            btnLoginPageForgot.isVisible = isVisible
            textLoginPageServiceTerms.isVisible = isVisible
            dividerLoginPageForgot.isVisible = isVisible
        }
    }

    override fun onAnimOver() {}

    override fun onDestroyView() {
        super.onDestroyView()
        KeyboardUtils.hideSoftInput(requireActivity())
    }

    override fun onTouchEvent(event: MotionEvent, defaultTouchEvent: Boolean): Boolean {
        return defaultTouchEvent
    }

    companion object {
        fun newInstance(): LoginPageFragment {
            val args = Bundle()
            val fragment = LoginPageFragment()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(args: Bundle?): LoginPageFragment {
            val fragment = LoginPageFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.textLoginPageLoginType -> {
                viewModel.selectType(v.id)
                showLoginTabPage(true)
            }
            R.id.textLoginPageRegisterType -> {
                viewModel.selectType(v.id)
                showLoginTabPage(false)
            }
            R.id.btnLoginPageLogin -> {
                KeyboardUtils.hideSoftInput(requireActivity())
                val account = loginTabBinding.editLoginPageAccount.text.toString()
                val password = loginTabBinding.editLoginPagePassword.text.toString()
                viewModel.checkLoginLegal(requireContext(), account, password)
            }
            R.id.btnLoginPageShowPassword -> {
                if (isShowPassword) {
                    loginTabBinding.btnLoginPageShowPassword.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_baseline_visibility_off_24
                        )
                    )
                    loginTabBinding.editLoginPagePassword.transformationMethod = PasswordTransformationMethod.getInstance()
                } else {
                    loginTabBinding.btnLoginPageShowPassword.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_baseline_visibility_24
                        )
                    )
                    loginTabBinding.editLoginPagePassword.transformationMethod = null
                }
                loginTabBinding.editLoginPagePassword.text?.length?.let { length ->
                    loginTabBinding.editLoginPagePassword.setSelection(length)
                }
                isShowPassword = !isShowPassword
            }
        }
    }
}
