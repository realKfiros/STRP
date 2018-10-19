# STRP - Single Time Registration Page

This project is an example project that demonstrates how to create a registration page for only one time use and for only one email every time using a Firebase Cloud Firestore, Swing and Kotlin. <br/>
This would be useful if you want to create an app but only for yourself and you want no one else to be able to create a user. <br/>
The user gets his unique "registration key" on email or somewhere else and than he enters it in this wizrard. <br/>
After the user is being created for that email this unique key is being removed from the database so no one will be able to use that key again.