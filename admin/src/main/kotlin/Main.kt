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
import java.util.*
import java.util.concurrent.ExecutionException

class Main private constructor() : JFrame("Create STRP") {
    internal var prompt: JLabel
    internal var emailField: JTextField
    internal var submit: JButton

    init {
        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)

        prompt = JLabel("Please enter an email here:")
        prompt.setBounds(20, 20, 260, 30)

        super.add(prompt)

        emailField = JTextField()
        emailField.setBounds(20, 60, 260, 30)

        super.add(emailField)

        submit = JButton("Submit")
        submit.setBounds(100, 100, 100, 30)
        submit.addActionListener {
            try {
                send(emailField.text)
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
    fun send(email: String) {
        val id = getSaltString()
        val strpFuture = FirestoreClient.getFirestore().collection("strp").document(id).set(STRP(email, id))
        JOptionPane.showMessageDialog(this@Main,
                "STRP created successfully",
                "Success!",
                JOptionPane.INFORMATION_MESSAGE)
    }

    protected fun getSaltString():String {
        val SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
        val salt = StringBuilder()
        val rnd = Random()
        while (salt.length < 18)
        { // length of the random string.
            val index = (rnd.nextFloat() * SALTCHARS.length).toInt()
            salt.append(SALTCHARS.get(index))
        }
        val saltStr = salt.toString()
        return saltStr
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
