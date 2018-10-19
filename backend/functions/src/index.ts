import * as functions from 'firebase-functions';
import * as nodemailer from 'nodemailer';

const mailTransport = nodemailer.createTransport({
    service: 'gmail',
    auth: {
        user: 'some.email.for.demonstration@gmail.com',
        pass: 'somepassword'
    }
});

export const send = functions.firestore.document('/strp/{id}')
    .onCreate((snapshot, context) => {
        return sendSTRP(snapshot.get('email'), context.params.id);
})

function sendSTRP(email: string, id: string) {
    const mailOptions = {
        from: `Demonstration <noreply@example.com>`,
        to: email,
        subject: 'Signup',
        text: `Hello! Here's your signup id: ${id}`,
        html: `
        <p style="font-size:30px;">
            Hello,<br/>Here's your signup id: <b>${id}</b><br/>Your Team
        </p>`
    }

    return mailTransport.sendMail(mailOptions).then(() => {
        return console.log('Single time registration page code sent to: ', email);
    });
}