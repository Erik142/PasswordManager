const bodyParser = require('body-parser')
const multer = require('multer')
const express = require('express')
const db = require('./database')
const path = require('path')
const fs = require('fs')

const upload = multer()
const app = express()

const relativeConfigPath = '../../PasswordManager/resources/config.json'

const configPath = path.join(__dirname, relativeConfigPath)

var config = JSON.parse(fs.readFileSync(configPath, 'utf8'));

app.set('view engine', 'pug')

// for parsing application/json
app.use(bodyParser.json()); 
// for parsing application/www-form-urlencoded
app.use(bodyParser.urlencoded({ extended: true })); 
// for parsing multipart/form-data
app.use(upload.array()); 

app.get('/:requestId', async function(req, res) {
    try {
        var email = await db.getUserEmail(req.params.requestId)

        if (email != null && email != '')
        {
            res.render('index', {
                email:email,
                requestId:req.params.requestId
            })
        }
        else {
            res.render('error', {
                errorMessage: "Error! You pressed an invalid reset link"
            })
        }
    }
    catch (err) {
        console.log(err)
        res.render('error', {
            errorMessage: err
        })
    }
});

app.post('/:requestId', async function(req, res) {
    try {
        var email = await db.getUserEmail(req.params.requestId)

        if (email == '') {
            res.render('error', {
                errorMessage: "Error! You pressed an invalid reset link"
            })
        }
        else {
            if (req.body.password == req.body['confirm-password'] && req.body.password.length >= 8) {
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
                else if (req.body.password.length < 8) {
                    errorMessage = 'Password must be at least 8 characters long'
                }

                res.render('index', {
                    requestId:req.params.requestId,
                    email:email,
                    showError:true,
                    errorMessage:errorMessage
                })
            }
        }
    }
    catch (err) {
        console.log(err)
        res.render('error', {
            errorMessage: err
        })
    }
})

db.openConnection(path.join(__dirname, '../../', config.dbPath)).then(() => {
    app.listen(config.webPort, config.serverIp)
}).catch(err => {
    console.log(err)
    return
})