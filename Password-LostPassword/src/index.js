const bodyParser = require('body-parser')
const multer = require('multer')
const express = require('express')
const db = require('./database')
const path = require('path')

const upload = multer()
const app = express()

app.set('view engine', 'pug')

// for parsing application/json
app.use(bodyParser.json()); 
// for parsing application/www-form-urlencoded
app.use(bodyParser.urlencoded({ extended: true })); 
// for parsing multipart/form-data
app.use(upload.array()); 

app.get('/:requestId', async function(req, res) {

    var email = await db.getUserEmail(req.params.requestId)

    if (email != null && email != '')
    {
        res.render('index', {
            email:email,
            requestId:req.params.requestId
        })
    }
    else {
        res.render('error')
    }
});

app.post('/:requestId', async function(req, res) {
    console.log('Posted form!')

    console.log(req.body)


    var email = await db.getUserEmail(req.params.requestId)

    if (req.body.password == req.body['confirm-password'] && req.body.password != '') {
        var userAccount = {
            email:email,
            password:req.body.password,
        }
    
        await db.updateUserAccount(userAccount)
        await db.deleteRequest(req.params.requestId)

        res.render('success', {
            email:email
        })
    }
    else {
        let errorMessage = "Password cannot be empty."

        if ((req.body.password != '' && req.body['confirm-password'] != '' ) && req.body.password != req.body['confirm-password']) {
            errorMessage = 'Passwords are not equal.'
        }

        res.render('index', {
            requestId:req.params.requestId,
            email:email,
            showError:true,
            errorMessage:errorMessage
        })
    }
})

// TODO: Load database path from config
try {
    db.openConnection(path.join(__dirname, '../../PasswordManager/PasswordManagerDatabase.db')).then(() => {
        app.listen(3000)
    })
} catch(err) {
    console.log(err)
    return;
}