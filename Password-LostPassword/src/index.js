const bodyParser = require('body-parser')
const multer = require('multer')
const express = require('express')
const server = require('./server')

const upload = multer()
const app = express()

app.set('view engine', 'pug')

// for parsing application/json
app.use(bodyParser.json()); 
// for parsing application/www-form-urlencoded
app.use(bodyParser.urlencoded({ extended: true })); 
// for parsing multipart/form-data
app.use(upload.array()); 

server.connectToServer()

app.get('/:requestId', function(req, res) {

    var email = server.getUserEmail(req.params.requestId)

    if (email != '')
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

app.post('/:requestId', function(req, res) {
    console.log('Posted form!')

    console.log(req.body)


    var email = server.getUserEmail(req.params.requestId)

    var userAccount = {
        email:email,
        password:"123"
    }

    server.updateUserAccount(userAccount)
    server.deleteRequest(req.params.requestId)

    res.render('success', {
        email:email
    })
})

app.listen(3000)