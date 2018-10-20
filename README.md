*NOTE: THIS PROJECT IS A WORKING EXAMPLE THAT YOU CAN TRY YOURSELF IF YOU WANT, BUT IT'S NOT INTENDED TO DO THAT. YOU CAN USE THIS PROJECT TO BUILD YOUR OWN STRP SYSTEM, BUT REMEMBER THAT IT'S JUST AN EXAMPLE.*
# STRP - Single Time Registration Page

This project is an example project that demonstrates how to create a registration page for only one time use and for only one email every time using a Firebase Cloud Firestore, Swing and Kotlin. <br/>
I also used in this project Firebase Cloud Functions in order to send an email with the registration key. I wrote the function with TypeScript. I'm sending the email from the function using the great Node.JS module [Nodemailer](https://github.com/nodemailer/nodemailer).<br/>
This would be useful if you want to create an app but only for yourself and you want no one else to be able to create a user. <br/>
The user gets his unique "registration key" on email or somewhere else and then he enters it in this wizard. <br/>
After the user is being created for that email, this unique key is being removed from the database so no one will be able to use that key again.

## Project map
There are 3 parts in this example:
1) admin - the swing app for the app admin, in order to create an STRP
2) backend - the cloud function that sends an email with the registration key
3) client - the client registration wizard
