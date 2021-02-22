exports.getUserEmail = getUserEmail
exports.updateUserAccount = updateUserAccount
exports.deleteRequest = deleteRequest
exports.openConnection = openConnection

const path = require('path')
const fs = require('fs')
const sqlite3 = require('sqlite3')
const sqlite = require('sqlite')

let dbInstance = null

async function openConnection(databasePath) {
    if (fs.existsSync(databasePath)) {
        dbInstance = await sqlite.open({
            filename:databasePath,
            driver:sqlite3.Database
        })
    }
    else {
        throw new Error('The file path \'' + databasePath + '\' does not exist.')
    }
}

async function getUserEmail(requestId) {
    let query = 'SELECT email FROM ResetRequests WHERE id = ?'

    let result = ''

    return new Promise((resolve, reject) => {
        dbInstance.db.get(query, requestId, (err, rows) => {
            if (err == null && rows != null) {
                console.log('query finished')
                result = rows.email
            } 
            else if (err != null) {
                reject(err)
            }
            
            console.log('Result: ' + result)
            resolve(result)
        })
    })

}

async function updateUserAccount(userAccount) {
    let query = 'UPDATE Accounts SET Password = ? WHERE Email = ?'

    return new Promise((resolve,reject) => {
        try {
            let result = dbInstance.db.run(query, userAccount.password, userAccount.email)
            console.log(result)
            resolve(true)
        }
        catch (err) {
            reject(err)
        }
    })

}

async function deleteRequest(requestId) {
    var requestIdNum = Number(requestId)

    let query = 'DELETE FROM ResetRequests WHERE id = ?'

    return new Promise((resolve, reject) => {
        try {
            let result = dbInstance.db.run(query, requestIdNum)
            console.log(result)
            resolve(true)
        }
        catch(err) {
            reject(err)
        }
    })

}