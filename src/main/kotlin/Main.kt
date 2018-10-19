import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserRecord
import com.google.firebase.cloud.FirestoreClient
import java.awt.Desktop

import javax.swing.*
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.net.URI
import java.util.concurrent.ExecutionException

class Main private constructor() : JFrame("Create a user") {
    internal var prompt: JLabel
    internal var registrationIdField: JTextField
    internal var submit: JButton

    init {
        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)

        prompt = JLabel("Please enter your registration id here:")
        prompt.setBounds(20, 20, 260, 30)

        super.add(prompt)

        registrationIdField = JTextField()
        registrationIdField.setBounds(20, 60, 260, 30)

        super.add(registrationIdField)

        submit = JButton("Submit")
        submit.setBounds(100, 100, 100, 30)
        submit.addActionListener {
            try {
                check(registrationIdField.text)
            } catch (e1: ExecutionException) {
                e1.printStackTrace()
            } catch (e1: InterruptedException) {
                e1.printStackTrace()
            }
        }

        super.add(submit)

        super.setSize(300, 170)
        super.setLocationRelativeTo(null);
        super.setLayout(null)
        super.setVisible(true)
    }

    @Throws(ExecutionException::class, InterruptedException::class)
    private fun check(id: String) {
        val strpFuture = FirestoreClient.getFirestore().collection("strp").document(id).get()
        val strpSnap = strpFuture.get()
        if (strpSnap.exists()) {
            super.remove(prompt)
            super.remove(registrationIdField)
            super.remove(submit)

            val strp = strpSnap.toObject(STRP::class.java)

            val email = JLabel("Email: " + strp!!.email!!)
            email.setBounds(20, 20, 460, 30)

            super.add(email)

            val fullNameHint = JLabel("Enter your full name: ")
            fullNameHint.setBounds(20, 60, 200, 30)

            super.add(fullNameHint)

            val fullNameField = JTextField()
            fullNameField.setBounds(200, 60, 200, 30)

            super.add(fullNameField)

            val usernameHint = JLabel("Enter your new username: ")
            usernameHint.setBounds(20, 100, 200, 30)

            super.add(usernameHint)

            val usernameField = JTextField()
            usernameField.setBounds(200, 100, 200, 30)

            super.add(usernameField)

            val passwordHint = JLabel("Enter your new password: ")
            passwordHint.setBounds(20, 140, 200, 30)

            super.add(passwordHint)

            val passwordField = JPasswordField()
            passwordField.setBounds(200, 140, 200, 30)

            super.add(passwordField)

            val repeatHint = JLabel("Retype the password: ")
            repeatHint.setBounds(20, 180, 200, 30)

            super.add(repeatHint)

            val repeatField = JPasswordField()
            repeatField.setBounds(200, 180, 200, 30)

            super.add(repeatField)

            val registerBtn = JButton("Submit")
            registerBtn.setBounds(160, 220, 100, 30)
            registerBtn.addActionListener {
                val fullName = fullNameField.text.length > 3
                val username = usernameField.text.length >= 3
                val password = String(passwordField.password).length >= 6
                val retype = String(passwordField.password) == String(repeatField.password)
                if (!fullName) {
                    JOptionPane.showMessageDialog(this@Main,
                            "Full name must be longer than 3 characters",
                            "Inane error",
                            JOptionPane.ERROR_MESSAGE)
                } else if (!username) {
                    JOptionPane.showMessageDialog(this@Main,
                            "Username must be 3 characters or more",
                            "Inane error",
                            JOptionPane.ERROR_MESSAGE)
                } else if (!password) {
                    JOptionPane.showMessageDialog(this@Main,
                            "Password must be 6 characters or more",
                            "Inane error",
                            JOptionPane.ERROR_MESSAGE)
                } else if (!retype) {
                    JOptionPane.showMessageDialog(this@Main,
                            "Passwords not identical",
                            "Inane error",
                            JOptionPane.ERROR_MESSAGE)
                } else {
                    val request = UserRecord.CreateRequest()
                    request.setEmail(strp.email!!)
                    request.setEmailVerified(true)
                    request.setPassword(String(passwordField.password))
                    try {
                        val userRecord = FirebaseAuth.getInstance().createUser(request)
                        val user = User(fullNameField.text, userRecord.uid, usernameField.text)
                        FirestoreClient.getFirestore().collection("users").document(user.uid!!).set(user)
                        FirestoreClient.getFirestore().collection("strp").document(strp.id!!).delete()
                        JOptionPane.showMessageDialog(this@Main,
                                "User created successfully",
                                "Success!",
                                JOptionPane.INFORMATION_MESSAGE)
                        super@Main.setVisible(false)
                        System.exit(0)
                    } catch (e1: FirebaseAuthException) {
                        e1.printStackTrace()
                    }

                }
            }

            super.add(registerBtn)

            super.setSize(420, 290)
        } else {
            JOptionPane.showMessageDialog(this@Main,
                    "Can't find sign up request!",
                    "Inane error",
                    JOptionPane.ERROR_MESSAGE)
            super.setVisible(false)
            System.exit(0)
        }
    }

    companion object {

        @Throws(IOException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            val serviceAccount = FileInputStream(File("serviceAccountKey.json").absolutePath)

            val options = FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build()
            FirebaseApp.initializeApp(options)
            Main()
        }
    }
}
